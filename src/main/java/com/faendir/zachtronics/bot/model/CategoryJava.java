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

package com.faendir.zachtronics.bot.model;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface CategoryJava<C extends Enum<C> & Category, S extends Score<C>, M extends MetricJava<S>, T extends Type> extends Category {
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    default List<Metric> getMetrics() {
        return (List<Metric>) (List<?>) metrics();
    }

    /** Typed metrics, java-kotlin can't talk to each other how they should */
    @NotNull List<M> metrics();
    @NotNull Comparator<S> getScoreComparator();
    @NotNull Set<T> getSupportedTypes();
    int getScoreFormatId();

    boolean supportsScore(@NotNull S score);

    default Comparator<S> makeCategoryComparator(@NotNull List<M> metrics) {
        return metrics.stream()
                      .map(M::getExtract)
                      .filter(Objects::nonNull)
                      .map(Comparator::comparingInt)
                      .reduce(Comparator::thenComparing)
                      .orElseThrow();
    }
}
