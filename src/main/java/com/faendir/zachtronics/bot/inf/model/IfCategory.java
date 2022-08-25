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

import com.faendir.zachtronics.bot.model.CategoryJava;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.faendir.zachtronics.bot.inf.model.IfMetric.*;
import static com.faendir.zachtronics.bot.inf.model.IfType.STANDARD;

@Getter
public enum IfCategory implements CategoryJava<IfCategory, IfScore, IfMetric, IfType> {
    C("C", List.of(CYCLES, FOOTPRINT, BLOCKS, ANY_FLAG), 0b100),
    CNG("CNG", List.of(CYCLES, FOOTPRINT, BLOCKS, NO_GRA), 0b100),

    F("F", List.of(FOOTPRINT, CYCLES, BLOCKS), 0b010),

    B("B", List.of(BLOCKS, CYCLES, FOOTPRINT), 0b001);

    /** contains <tt>%d%s%d%s%d%s</tt> plus a bunch of <tt>*</tt> most likely */
    static final String[] FORMAT_STRINGS = {"%d%s%d%s%d%s", "%d%s%d%s**%d**%s", "%d%s**%d**%s%d%s", null, "**%d**%s%d%s%d%s"};

    private final String displayName;
    @Accessors(fluent = true)
    private final List<IfMetric> metrics;
    private final Comparator<IfScore> scoreComparator;
    private final Set<IfType> supportedTypes = Collections.singleton(STANDARD);
    private final int scoreFormatId;

    IfCategory(String displayName, @NotNull List<IfMetric> metrics, int scoreFormatId) {
        this.displayName = displayName;
        this.metrics = metrics;
        this.scoreComparator = makeCategoryComparator(metrics);
        this.scoreFormatId = scoreFormatId;
    }

    @Override
    public boolean supportsScore(@NotNull IfScore score) {
        return !(this == CNG && score.usesGRA());
    }
}
