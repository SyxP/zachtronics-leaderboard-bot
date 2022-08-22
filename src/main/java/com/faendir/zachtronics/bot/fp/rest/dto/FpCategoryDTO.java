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

package com.faendir.zachtronics.bot.fp.rest.dto;

import com.faendir.zachtronics.bot.fp.model.FpCategory;
import com.faendir.zachtronics.bot.model.Metric;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Value
public class FpCategoryDTO {
    @NotNull String id;
    @NotNull String displayName;
    @NotNull List<String> metrics;
    @NotNull List<String> puzzleTypes;

    @NotNull
    public static FpCategoryDTO fromCategory(@NotNull FpCategory category) {
        return new FpCategoryDTO(category.name(),
                                 category.getDisplayName(),
                                 category.getMetrics().stream().map(Metric::getDisplayName).toList(),
                                 category.getSupportedTypes().stream().map(Enum::name).toList());
    }
}