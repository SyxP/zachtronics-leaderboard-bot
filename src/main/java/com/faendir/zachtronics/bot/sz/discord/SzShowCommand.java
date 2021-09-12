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

package com.faendir.zachtronics.bot.sz.discord;

import com.faendir.discord4j.command.annotation.ApplicationCommand;
import com.faendir.discord4j.command.annotation.Converter;
import com.faendir.zachtronics.bot.discord.command.AbstractShowCommand;
import com.faendir.zachtronics.bot.leaderboards.Leaderboard;
import com.faendir.zachtronics.bot.sz.SzQualifier;
import com.faendir.zachtronics.bot.sz.model.SzCategory;
import com.faendir.zachtronics.bot.sz.model.SzPuzzle;
import com.faendir.zachtronics.bot.sz.model.SzRecord;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import kotlin.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@SzQualifier
public class SzShowCommand extends AbstractShowCommand<SzCategory, SzPuzzle, SzRecord> {
    @Getter
    private final List<Leaderboard<SzCategory, SzPuzzle, SzRecord>> leaderboards;

    @NotNull
    @Override
    public Pair<SzPuzzle, SzCategory> findPuzzleAndCategory(@NotNull SlashCommandEvent interaction) {
        Data data = SzShowCommand$DataParser.parse(interaction);
        return new Pair<>(data.puzzle, data.category);
    }

    @NotNull
    @Override
    public ApplicationCommandOptionData buildData() {
        return SzShowCommand$DataParser.buildData();
    }

    @ApplicationCommand(name = "show", subCommand = true)
    @Value
    public static class Data {
        @NonNull SzPuzzle puzzle;
        @NonNull SzCategory category;

        public Data(@Converter(SzPuzzleConverter.class) @NonNull SzPuzzle puzzle, @NonNull SzCategory category) {
            this.puzzle = puzzle;
            this.category = category;
        }
    }
}
