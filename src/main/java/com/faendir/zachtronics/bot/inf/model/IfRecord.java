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

package com.faendir.zachtronics.bot.inf.model;

import com.faendir.zachtronics.bot.model.DisplayContext;
import com.faendir.zachtronics.bot.model.Record;
import com.faendir.zachtronics.bot.utils.Markdown;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

@Value
public class IfRecord implements Record<IfCategory> {
    @NotNull IfPuzzle puzzle;
    @NotNull IfScore score;
    @NotNull String author;
    @NotNull List<String> displayLinks;
    String dataLink;
    Path dataPath;

    @Nullable
    @Override
    public String getDisplayLink() {
        return displayLinks.isEmpty() ? null : displayLinks.get(0);
    }

    @NotNull
    @Override
    public String toDisplayString(@NotNull DisplayContext<IfCategory> context) {
        String result = Markdown.fileLinkOrEmpty(dataLink) +
                        Markdown.linkOrText(score.toDisplayString(context) + " " + author, getDisplayLink());
        if (displayLinks.size() > 1) {
            for (int i = 1; i < displayLinks.size(); i++) {
                result += " " + Markdown.link("^[" + (i + 1) + "]", displayLinks.get(i));
            }
        }
        return result;
    }
}