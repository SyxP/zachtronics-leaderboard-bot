/*
 * Copyright (c) 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.faendir.zachtronics.bot.om.discord


import com.faendir.zachtronics.bot.discord.command.Command
import com.faendir.zachtronics.bot.discord.command.option.CommandOptionBuilder
import com.faendir.zachtronics.bot.discord.command.option.enumOptionBuilder
import com.faendir.zachtronics.bot.discord.command.security.DiscordUser
import com.faendir.zachtronics.bot.discord.command.security.DiscordUserSecured
import com.faendir.zachtronics.bot.model.DisplayContext
import com.faendir.zachtronics.bot.om.JNISolutionVerifier
import com.faendir.zachtronics.bot.om.OmQualifier
import com.faendir.zachtronics.bot.om.OmSimMetric
import com.faendir.zachtronics.bot.om.model.OmPuzzle
import com.faendir.zachtronics.bot.om.model.OmRecord
import com.faendir.zachtronics.bot.om.model.OmScore
import com.faendir.zachtronics.bot.om.model.OmScorePart
import com.faendir.zachtronics.bot.om.model.OmType
import com.faendir.zachtronics.bot.om.omPuzzleOptionBuilder
import com.faendir.zachtronics.bot.om.omScoreOptionBuilder
import com.faendir.zachtronics.bot.om.repository.OmSolutionRepository
import com.faendir.zachtronics.bot.utils.MultiMessageSafeEmbedMessageBuilder
import com.faendir.zachtronics.bot.utils.SafeMessageBuilder
import com.faendir.zachtronics.bot.utils.ceil
import com.faendir.zachtronics.bot.utils.orEmpty
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import org.springframework.stereotype.Component
import kotlin.io.path.readBytes

@Component
@OmQualifier
class OmReverifyCommand(private val repository: OmSolutionRepository) : Command.BasicLeaf() {
    override val name = "reverify"
    override val description: String = "Recompute metrics based on filters"

    private val typeOption = enumOptionBuilder<OmType>("type") { displayName }.build()
    private val puzzleOption = omPuzzleOptionBuilder().build()
    private val partOption = enumOptionBuilder<OmScorePart>("part") { displayName }.build()
    private val scoreOption = omScoreOptionBuilder().build()
    private val overrideExistingOption = CommandOptionBuilder.boolean("override-existing").build()
    override val options = listOf(typeOption, puzzleOption, partOption, scoreOption, overrideExistingOption)

    override val secured = DiscordUserSecured(DiscordUser.BOT_OWNERS)

    override fun handleEvent(event: ChatInputInteractionEvent): SafeMessageBuilder {
        val puzzleIn = puzzleOption.get(event)
        val type = typeOption.get(event)
        val part = partOption.get(event)
        val score = scoreOption.get(event)
        val overrideExisting = overrideExistingOption.get(event) == true
        val puzzles = (puzzleIn?.let { listOf(it) } ?: OmPuzzle.values().toList()).filter {
            type == null || type == it.type
        }
        val parts = part?.let { listOf(it) } ?: OmScorePart.values().toList()
        val overrideRecords = mutableListOf<Pair<OmRecord, OmScore>>()
        val errors = mutableListOf<String>()
        for (puzzle in puzzles) {
            val puzzleFile = puzzle.file

            val records = repository.findCategoryHolders(puzzle, true)
                .filter { (record, _) -> score == null || record.score.toDisplayString().equals(score, ignoreCase = true) }
            for ((record, _) in records) {
                if (record.dataPath == null) {
                    errors.add("No solution file for ${record.toDisplayString(DisplayContext.discord())}")
                    continue
                }

                fun JNISolutionVerifier.getMetricSafe(metric: OmSimMetric) = try {
                    getMetric(metric)
                } catch (e: Exception) {
                    errors.add("Failed to get ${metric::class.java.simpleName} for ${record.toDisplayString(DisplayContext.discord())}: ${e.message}")
                    null
                }

                if (overrideExisting || parts.any { it.getValue(record.score) == null }) {
                    val computed = JNISolutionVerifier.open(puzzleFile.readBytes(), record.dataPath.readBytes())
                        .use { verifier ->
                            listOf(
                                OmScorePart.COST to verifier.getMetricSafe(OmSimMetric.COST),
                                OmScorePart.CYCLES to verifier.getMetricSafe(OmSimMetric.CYCLES),
                                OmScorePart.AREA to verifier.getMetricSafe(OmSimMetric.AREA_APPROXIMATE),
                                OmScorePart.INSTRUCTIONS to verifier.getMetricSafe(OmSimMetric.INSTRUCTIONS),
                                OmScorePart.HEIGHT to verifier.getMetricSafe(OmSimMetric.HEIGHT),
                                OmScorePart.WIDTH to verifier.getMetricSafe(OmSimMetric.WIDTH_TIMES_TWO)?.let { it.toDouble() / 2 },
                                OmScorePart.RATE to verifier.getMetricSafe(OmSimMetric.THROUGHPUT_CYCLES)?.let {
                                    (it.toDouble() / (verifier.getMetricSafe(OmSimMetric.THROUGHPUT_OUTPUTS) ?: 0)).ceil(precision = 2)
                                },
                            ).filter { it.second != null }.toMap()
                        }
                    if (computed.size < parts.size) {
                        errors.add(
                            "Wanted ${parts.joinToString { it.displayName }}, but got ${computed.keys.joinToString { it.displayName }} for ${
                                record.toDisplayString(
                                    DisplayContext.discord()
                                )
                            }"
                        )
                    }
                    val newScore = if (overrideExisting) {
                        OmScore(
                            cost = computed[OmScorePart.COST]?.toInt() ?: record.score.cost,
                            cycles = computed[OmScorePart.CYCLES]?.toInt() ?: record.score.cycles,
                            area = computed[OmScorePart.AREA]?.toInt() ?: record.score.area,
                            instructions = computed[OmScorePart.INSTRUCTIONS]?.toInt() ?: record.score.instructions,
                            height = computed[OmScorePart.HEIGHT]?.toInt() ?: record.score.height,
                            width = computed[OmScorePart.WIDTH]?.toDouble() ?: record.score.width,
                            rate = computed[OmScorePart.RATE]?.toDouble() ?: record.score.rate,
                            trackless = record.score.trackless,
                            overlap = record.score.overlap,
                        )
                    } else {
                        OmScore(
                            cost = record.score.cost ?: computed[OmScorePart.COST]?.toInt(),
                            cycles = record.score.cycles ?: computed[OmScorePart.CYCLES]?.toInt(),
                            area = record.score.area ?: computed[OmScorePart.AREA]?.toInt(),
                            instructions = record.score.instructions ?: computed[OmScorePart.INSTRUCTIONS]?.toInt(),
                            height = record.score.height ?: computed[OmScorePart.HEIGHT]?.toInt(),
                            width = record.score.width ?: computed[OmScorePart.WIDTH]?.toDouble(),
                            rate = record.score.rate ?: computed[OmScorePart.RATE]?.toDouble(),
                            trackless = record.score.trackless,
                            overlap = record.score.overlap,
                        )
                    }
                    if (newScore != record.score) {
                        overrideRecords.add(record to newScore)
                    }
                }
            }
        }
        if (overrideRecords.isNotEmpty()) {
            repository.overrideScores(overrideRecords)
        }
        return MultiMessageSafeEmbedMessageBuilder()
            .title(
                "Reverify" +
                        type?.displayName.orEmpty(" ") +
                        puzzleIn?.displayName.orEmpty(" ") +
                        part?.displayName.orEmpty(" ") +
                        if (overrideExisting) " overriding existing values!" else ""
            )
            .description("**Modified Records:** ${overrideRecords.size}\n\n**Errors:**\n${errors.joinToString("\n")}")
    }
}
