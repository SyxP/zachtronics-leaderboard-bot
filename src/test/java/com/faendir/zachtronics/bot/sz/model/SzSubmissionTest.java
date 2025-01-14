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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SzSubmissionTest {

    @Test
    void testBadData() {
        // typical HTML (non raw) page
        String data = """
                      <!DOCTYPE html>
                      <html lang="en" data-color-mode="auto" data-light-theme="light" data-dark-theme="dark">
                      """;
        assertThrows(IllegalArgumentException.class, () -> SzSubmission.fromData(data, "837951602"));
    }

    @Test
    public void testCopyBad() {
        String baseContent = """
                [name] TITLE
                [puzzle] Sz000
                [production-cost] 600
                [power-usage] 57
                [lines-of-code] 8
                """;
        for (String title : new String[]{"title (Copy)", "title (Copy) (Copy)"}) {
            String content = baseContent.replace("TITLE", title);
            SzSubmission result = SzSubmission.fromData(content, "12345ieee");
            SzSubmission expected = new SzSubmission(SzPuzzle.Sz000, new SzScore(6, 57, 8),
                                                     "12345ieee", content.replace(" (Copy)", ""));
            assertEquals(expected, result);
        }
    }
}