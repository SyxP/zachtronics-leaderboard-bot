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

package com.faendir.zachtronics.bot.om.rest.dto

import com.faendir.zachtronics.bot.model.DisplayContext
import com.faendir.zachtronics.bot.model.StringFormat
import com.faendir.zachtronics.bot.om.model.OmCategory
import com.faendir.zachtronics.bot.om.model.OmPuzzle
import com.faendir.zachtronics.bot.om.model.OmRecord
import com.faendir.zachtronics.bot.repository.CategoryRecord
import com.faendir.zachtronics.bot.rest.dto.RecordDTO
import com.faendir.zachtronics.bot.utils.smartFormat
import com.faendir.zachtronics.bot.utils.toMetricsTree
import kotlinx.datetime.toJavaInstant

val OmRecord.id get() = score.toDisplayString(DisplayContext.fileName())

data class OmRecordDTO(
    val id: String?,
    val puzzle: OmPuzzleDTO,
    override val score: OmScoreDTO?,
    val smartFormattedScore: String?,
    override val fullFormattedScore: String?,
    override val gif: String?,
    override val solution: String?,
    val categoryIds: List<String>?,
    override val smartFormattedCategories: String?,
    val lastModified: java.time.Instant?,
) : RecordDTO<OmScoreDTO> {
    override val author: String? = null
}

fun CategoryRecord<OmRecord, OmCategory>.toDTO() =
    OmRecordDTO(
        id = record.id,
        puzzle = record.puzzle.toDTO(),
        score = record.score.toDTO(),
        smartFormattedScore = record.score.toDisplayString(DisplayContext(StringFormat.PLAIN_TEXT, categories)),
        fullFormattedScore = record.score.toDisplayString(DisplayContext.plainText()),
        gif = record.displayLink,
        solution = record.dataLink,
        categoryIds = categories.map { it.name },
        smartFormattedCategories = categories.smartFormat(record.puzzle.supportedCategories.toMetricsTree()),
        lastModified = record.lastModified?.toJavaInstant(),
    )

fun OmRecord.toDTO() =
    OmRecordDTO(
        id = id,
        puzzle = puzzle.toDTO(),
        score = score.toDTO(),
        smartFormattedScore = null,
        fullFormattedScore = score.toDisplayString(DisplayContext.plainText()),
        gif = displayLink,
        solution = dataLink,
        categoryIds = null,
        smartFormattedCategories = null,
        lastModified = null,
    )

fun emptyRecord(puzzle: OmPuzzle) = OmRecordDTO(
    id = null,
    puzzle = puzzle.toDTO(),
    score = null,
    smartFormattedScore = null,
    fullFormattedScore = null,
    gif = null,
    solution = null,
    categoryIds = null,
    smartFormattedCategories = null,
    lastModified = null,
)