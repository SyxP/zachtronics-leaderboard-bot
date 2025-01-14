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

package com.faendir.zachtronics.bot.sz.model;

import com.faendir.zachtronics.bot.model.DisplayContext;
import com.faendir.zachtronics.bot.model.Record;
import com.faendir.zachtronics.bot.utils.Markdown;
import kotlin.Pair;
import lombok.SneakyThrows;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Value
public class SzRecord implements Record<SzCategory> {
    @NotNull SzPuzzle puzzle;
    @NotNull SzScore score;
    @NotNull String author;
    String dataLink;
    Path dataPath;

    @Override
    public String getDisplayLink() {
        return null;
    }

    @NotNull
    @Override
    public String toDisplayString(@NotNull DisplayContext<SzCategory> context) {
        String scoreAuthor = "(" + score.toDisplayString(context) + ")" + " " + author;
        return Markdown.linkOrText(scoreAuthor, dataLink);
    }

    @SneakyThrows
    @NotNull
    @Override
    public List<Pair<String, InputStream>> attachments() {
        if (dataPath != null)
            return Collections.singletonList(new Pair<>(dataPath.getFileName().toString(), Files.newInputStream(dataPath)));
        else
            return Collections.emptyList();
    }
}