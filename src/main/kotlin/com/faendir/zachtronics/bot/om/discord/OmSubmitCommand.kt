package com.faendir.zachtronics.bot.om.discord

import com.faendir.discord4j.command.annotation.ApplicationCommand
import com.faendir.discord4j.command.annotation.Converter
import com.faendir.discord4j.command.annotation.Description
import com.faendir.zachtronics.bot.generic.discord.AbstractSubmitCommand
import com.faendir.zachtronics.bot.model.Leaderboard
import com.faendir.zachtronics.bot.om.model.*
import discord4j.core.`object`.command.ApplicationCommandInteractionOption
import discord4j.core.`object`.entity.User
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples

@Component
class OmSubmitCommand(override val leaderboards: List<Leaderboard<*, OmPuzzle, OmRecord>>) :
    AbstractSubmitCommand<OmPuzzle, OmRecord>() {

    override fun buildData() = SubmitParser.buildData()

    override fun parseSubmission(options: List<ApplicationCommandInteractionOption>, user: User): Mono<Tuple2<OmPuzzle, OmRecord>> {
        return Mono.fromCallable { SubmitParser.parse(options) }
            .map { Tuples.of(it.puzzle, OmRecord(OmScore.parse(it.puzzle, it.score).copy(modifier = it.modifier), it.link, user.username)) }
    }
}

@ApplicationCommand(description = "Submit a solution", subCommand = true)
data class Submit(
    @Description("Puzzle name. Can be shortened or abbreviated. E.g. `stab water`, `PMO`")
    @Converter(PuzzleConverter::class)
    val puzzle: OmPuzzle,
    @Description("Puzzle score. E.g. `100/32/14/22`, `3.5w/32c/100g`")
    val score: String,
    @Description("Link to your solution gif/mp4")
    val link: String,
    @Description("Metric Modifier")
    val modifier: OmModifier?
)