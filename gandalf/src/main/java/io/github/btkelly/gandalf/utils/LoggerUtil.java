/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.github.btkelly.gandalf.utils;

import android.util.Log;

/**
 * Simple log cat wrapper for different log levels
 */
public final class LoggerUtil {

    private static final String LOGGER_TAG = "Gandalf";
    private static LogLevel level = LogLevel.NONE;

    public enum LogLevel {
        NONE,
        ERROR,
        DEBUG
    }

    private LoggerUtil() {

    }

    /**
     * Set the maximum priority {@link io.github.btkelly.gandalf.utils.LoggerUtil.LogLevel}
     * @param level the maximum level that will be logged
     */
    public static void setLogLevel(final LogLevel level) {
        LoggerUtil.level = level;
    }

    public static void logD(String message) {
        if (level.ordinal() >= LogLevel.DEBUG.ordinal()) {
            Log.d(LOGGER_TAG, message);
        }
    }

    public static void logE(String message) {
        if (level.ordinal() >= LogLevel.ERROR.ordinal()) {
            Log.e(LOGGER_TAG, message);
        }
    }
}
