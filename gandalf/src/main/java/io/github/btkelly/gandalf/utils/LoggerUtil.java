/**
 * Copyright 2020 Bryan Kelly
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * <p>
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.github.btkelly.gandalf.utils;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Simple log cat wrapper for different log levels
 */
public final class LoggerUtil {

    public static final int NONE = 0;
    public static final int ERROR = 1;
    public static final int DEBUG = 2;
    private static final String LOGGER_TAG = "Gandalf";
    @LogLevel
    private static int logLevel = NONE;

    private LoggerUtil() {

    }

    /**
     * Set the maximum priority {@link io.github.btkelly.gandalf.utils.LoggerUtil.LogLevel}
     *
     * @param level the maximum level that will be logged
     */
    public static void setLogLevel(@LogLevel final int level) {
        LoggerUtil.logLevel = level;
    }

    public static void logD(String message) {
        if (logLevel >= DEBUG) {
            Log.d(LOGGER_TAG, message);
        }
    }

    public static void logE(String message) {
        if (logLevel >= ERROR) {
            Log.e(LOGGER_TAG, message);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({NONE, ERROR, DEBUG})
    public @interface LogLevel {
    }
}
