/**
 * Copyright 2016 Bryan Kelly
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
package io.github.btkelly.gandalf.checker;

import androidx.annotation.NonNull;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;

/**
 * Interface to facilitate checking the previously
 * seen {@link OptionalUpdate} and {@link Alert}.
 */
public interface HistoryChecker {

    /**
     * Checks provided {@link Alert} against the last seen one.
     *
     * @param alert item to compare against the history
     * @return {@code true} if provided alert matches the history
     */
    boolean contains(@NonNull Alert alert);

    /**
     * Saves the provided {@link OptionalUpdate} to shared preferences.
     *
     * @param optionalUpdate the provided current optional update information
     * @return {@code true} if {@code optionalUpdate} was successfully saved
     */
    boolean save(@NonNull OptionalUpdate optionalUpdate);

    /**
     * Checks provided {@link OptionalUpdate} against the last seen one.
     *
     * @param optionalUpdate item to compare against the history
     * @return {@code true} if provided update matches the history
     */
    boolean contains(@NonNull OptionalUpdate optionalUpdate);

    /**
     * Saves the provided {@link Alert} to shared preferences.
     *
     * @param alert this should be the current alert
     * @return {@code true} if {@code alert} was successfully saved
     */
    boolean save(@NonNull Alert alert);

}
