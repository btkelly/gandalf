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

    public enum LogLevel {
        DEBUG,
        ERROR,
        NONE
    }

    private LoggerUtil() {

    }

    public static void logD(String message) {
        //TODO add log level config to gandalf class
        Log.d(LOGGER_TAG, message);
    }

    public static void logE(String message) {
        //TODO add log level config to gandalf class
        Log.e(LOGGER_TAG, message);
    }


}
