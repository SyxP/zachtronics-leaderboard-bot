/*
 * Copyright (c) 2021
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
import com.faendir.zachtronics.bot.discord.command.GameCommand
import com.faendir.zachtronics.bot.om.OmQualifier
import org.springframework.stereotype.Component

@Component
class OmCommand(@OmQualifier override val commands: List<Command>) : GameCommand {
    override val displayName = "Opus Magnum"
    override val commandName = "om"
}