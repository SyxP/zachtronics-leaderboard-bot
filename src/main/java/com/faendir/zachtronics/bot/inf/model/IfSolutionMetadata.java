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

import lombok.Value;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
class IfSolutionMetadata {
    /** InputRate.4-3.1 = 1 */
    private static final Pattern INPUT_RATE_PATTERN = Pattern.compile("InputRate.(?<id>\\d+-\\db?).(?<slot>\\d) = \\d+");
    /** Solution.4-3.1 = AAAA== */
    private static final Pattern SOLUTION_PATTERN = Pattern.compile("Solution.(?<id>\\d+-\\db?).(?<slot>\\d) = [\\w+/]+=*");

    IfPuzzle puzzle;

    @NotNull
    @Contract("_ -> new")
    public static IfSolutionMetadata fromData(@NotNull String data) {
        Iterator<String> it = Pattern.compile("\r?\n").splitAsStream(data).dropWhile(String::isBlank).iterator();
        String inputRateLine = it.next();
        Matcher m = INPUT_RATE_PATTERN.matcher(inputRateLine);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid solution file, first line: \"" + inputRateLine + "\"");

        String id = m.group("id");
        String slot = m.group("slot");
        IfPuzzle puzzle = Arrays.stream(IfPuzzle.values()).filter(p -> p.getId().equals(id)).findFirst().orElseThrow();

        String solutionLine = it.next();
        m = SOLUTION_PATTERN.matcher(solutionLine);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid solution file, solution line: \"" + solutionLine + "\"");

        if (!m.group("id").equals(id) || !m.group("slot").equals(slot))
            throw new IllegalArgumentException("Incoherent solution lines");

        return new IfSolutionMetadata(puzzle);
    }

    public IfSubmission extendToSubmission(@NotNull String author, @NotNull IfScore score,
                                           @NotNull List<String> displayLinks, @NotNull String data) {
        data = data.replaceAll("\\." + puzzle.getId() + "\\.\\d", "." + puzzle.getId() + ".0"); // slot normaliztion
        return new IfSubmission(puzzle, score, author, displayLinks, data);
    }
}
