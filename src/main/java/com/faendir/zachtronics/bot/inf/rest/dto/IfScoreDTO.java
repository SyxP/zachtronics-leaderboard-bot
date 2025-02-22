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

package com.faendir.zachtronics.bot.inf.rest.dto;

import com.faendir.zachtronics.bot.inf.model.IfScore;
import lombok.Value;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Value
public class IfScoreDTO {
    int cycles;
    int footprint;
    int blocks;

    @Accessors(fluent = true)
    boolean usesGRA;

    @NotNull
    public static IfScoreDTO fromScore(@NotNull IfScore score) {
        return new IfScoreDTO(score.getCycles(), score.getFootprint(), score.getBlocks(), score.usesGRA());
    }
}

