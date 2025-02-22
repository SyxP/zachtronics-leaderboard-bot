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

package com.faendir.zachtronics.bot.om.rest

import com.faendir.zachtronics.bot.om.createSubmission
import com.faendir.zachtronics.bot.om.model.OmCategory
import com.faendir.zachtronics.bot.om.model.OmGroup
import com.faendir.zachtronics.bot.om.model.OmPuzzle
import com.faendir.zachtronics.bot.om.notifyOf
import com.faendir.zachtronics.bot.om.repository.OmSolutionRepository
import com.faendir.zachtronics.bot.om.rest.dto.OmCategoryDTO
import com.faendir.zachtronics.bot.om.rest.dto.OmGroupDTO
import com.faendir.zachtronics.bot.om.rest.dto.OmPuzzleDTO
import com.faendir.zachtronics.bot.om.rest.dto.OmRecordChangeDTO
import com.faendir.zachtronics.bot.om.rest.dto.OmRecordDTO
import com.faendir.zachtronics.bot.om.rest.dto.OmSubmissionDTO
import com.faendir.zachtronics.bot.om.rest.dto.emptyRecord
import com.faendir.zachtronics.bot.om.rest.dto.id
import com.faendir.zachtronics.bot.om.rest.dto.toDTO
import com.faendir.zachtronics.bot.om.withCategory
import com.faendir.zachtronics.bot.repository.SubmitResult
import com.faendir.zachtronics.bot.rest.GameRestController
import com.faendir.zachtronics.bot.rest.dto.SubmitResultType
import com.faendir.zachtronics.bot.utils.isValidLink
import discord4j.core.GatewayDiscordClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinInstant
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.annotation.PreDestroy
import kotlin.io.path.readBytes

@RestController
@RequestMapping("/om")
class OmController(private val repository: OmSolutionRepository, private val discordClient: GatewayDiscordClient) :
    GameRestController<OmGroupDTO, OmPuzzleDTO, OmCategoryDTO, OmRecordDTO> {
    private val discordScope = CoroutineScope(Dispatchers.Default)

    @PreDestroy
    fun shutdown() {
        discordScope.cancel()
    }

    override val groups: List<OmGroupDTO> = OmGroup.values().map { it.toDTO() }

    override val puzzles: List<OmPuzzleDTO> = OmPuzzle.values().map { it.toDTO() }

    override fun getPuzzle(puzzleId: String): OmPuzzleDTO = findPuzzle(puzzleId).toDTO()

    @GetMapping("/puzzle/{puzzleId}/file")
    fun getPuzzleFile(@PathVariable puzzleId: String): ByteArray = findPuzzle(puzzleId).file.readBytes()

    override fun listPuzzlesByGroup(groupId: String): List<OmPuzzleDTO> {
        val group = findGroup(groupId)
        return OmPuzzle.values().filter { it.group == group }.map { it.toDTO() }
    }

    override val categories: List<OmCategoryDTO> = OmCategory.values().map { it.toDTO() }

    override fun getCategory(categoryId: String): OmCategoryDTO = findCategory(categoryId).toDTO()

    override fun listRecords(puzzleId: String, includeFrontier: Boolean?): List<OmRecordDTO> {
        val puzzle = findPuzzle(puzzleId)
        return repository.findCategoryHolders(puzzle, includeFrontier ?: false).map { it.toDTO() }
    }

    @GetMapping(path = ["/category/{categoryId}/records"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listRecords(@PathVariable categoryId: String): List<OmRecordDTO> {
        val category = findCategory(categoryId)
        return repository.findAll(category).entries.sortedBy { it.key }.map { it.value?.withCategory(category)?.toDTO() ?: emptyRecord(it.key) }
    }

    override fun getRecord(puzzleId: String, categoryId: String): OmRecordDTO? {
        val puzzle = findPuzzle(puzzleId)
        val category = findCategory(categoryId)
        return repository.find(puzzle, category)?.withCategory(category)?.toDTO()
    }

    @GetMapping("puzzle/{puzzleId}/record/{recordId}/file")
    fun getRecordFile(@PathVariable puzzleId: String, @PathVariable recordId: String): ByteArray =
        repository.findCategoryHolders(findPuzzle(puzzleId), includeFrontier = true)
            .find { recordId == it.record.id }
            ?.record?.dataPath?.readBytes()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Record $recordId not found.")

    @PostMapping(path = ["/submit"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun submit(@ModelAttribute submissionDTO: OmSubmissionDTO): SubmitResultType {
        if (submissionDTO.gif != null && !isValidLink(submissionDTO.gif)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid gif")
        if (submissionDTO.gif == null && submissionDTO.gifData == null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "no gif")
        val submission = createSubmission(submissionDTO.gif, submissionDTO.gifData?.bytes, submissionDTO.author, submissionDTO.solution.bytes)
        val result = repository.submit(submission)
        discordScope.launch { discordClient.notifyOf(result) }
        return when (result) {
            is SubmitResult.Success,is SubmitResult.Updated  -> SubmitResultType.SUCCESS
            is SubmitResult.Failure -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.message)
            is SubmitResult.NothingBeaten -> SubmitResultType.NOTHING_BEATEN
            is SubmitResult.AlreadyPresent -> SubmitResultType.ALREADY_PRESENT
        }
    }

    @GetMapping(path = ["/records/changes/{since}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRecordChanges(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) since: java.time.Instant): List<OmRecordChangeDTO> {
        return repository.computeChangesSince(since.toKotlinInstant()).map { it.toDTO() }
    }


    @GetMapping(path = ["/records/new/{since}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getNewRecords(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) since: java.time.Instant): List<OmRecordDTO> {
        val kSince = since.toKotlinInstant()
        return repository.records.filter { it.record.lastModified != null && it.record.lastModified >= kSince }.sortedBy { it.record.lastModified }
            .map { it.toDTO() }
    }
}

private fun findPuzzle(puzzleId: String) =
    OmPuzzle.values().find { it.id.equals(puzzleId, ignoreCase = true) } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Puzzle $puzzleId not found.")

private fun findCategory(categoryId: String) = OmCategory.values().find { it.name.equals(categoryId, ignoreCase = true) } ?: throw ResponseStatusException(
    HttpStatus.NOT_FOUND,
    "Category $categoryId not found."
)

private fun findGroup(groupId: String) =
    OmGroup.values().find { it.name.equals(groupId, ignoreCase = true) } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Group $groupId not found.")

