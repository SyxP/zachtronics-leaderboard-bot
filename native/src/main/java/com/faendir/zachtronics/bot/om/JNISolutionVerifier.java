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

package com.faendir.zachtronics.bot.om;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JNISolutionVerifier implements Closeable {
    static {
        NativeLoader.loadLibrary(JNISolutionVerifier.class.getClassLoader(), "omsim");
        NativeLoader.loadLibrary(JNISolutionVerifier.class.getClassLoader(), "native");
    }

    private final byte[] puzzle;
    private final byte[] solution;
    private Long verifier = null;

    private Integer errorCycle = null;

    private static native long prepareVerifier(byte[] puzzle, byte[] solution);

    private static native void closeVerifier(long verifier);

    private static native int getMetric(long verifier, String name);

    private static native int getErrorCycle(long verifier);

    public static JNISolutionVerifier open(byte[] puzzle, byte[] solution) {
        return new JNISolutionVerifier(puzzle, solution);
    }

    public int getMetric(OmSimMetric metric) {
        if (verifier == null) verifier = prepareVerifier(puzzle, solution);
        try {
            int result = getMetric(verifier, metric.getId());
            errorCycle = null;
            return result;
        } catch (Throwable t) {
            errorCycle = getErrorCycle(verifier);
            close();
            throw t;
        }
    }

    public int getErrorCycle() {
        if (errorCycle != null) {
            return errorCycle;
        } else {
            throw new IllegalStateException("Tried to get error cycle but there was no error state");
        }
    }

    @Override
    public void close() {
        if (verifier != null) {
            closeVerifier(verifier);
            verifier = null;
        }
    }

}
