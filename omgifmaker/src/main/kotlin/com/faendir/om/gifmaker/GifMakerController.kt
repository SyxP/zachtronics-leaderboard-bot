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

package com.faendir.om.gifmaker

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GifMakerController(private val gifRecorderService: GifRecorderService, private val imgurService: ImgurService) {

    @PostMapping("creategif", produces = [MediaType.IMAGE_GIF_VALUE])
    fun createAndUploadGif(
        @RequestBody solution: ByteArray,
        @RequestParam(required = false) start: Int?,
        @RequestParam(required = false) end: Int?
    ): String? {
        val gif = gifRecorderService.createGif(solution, start, end) ?: return null
        return imgurService.upload(gif)
    }
}