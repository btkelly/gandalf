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
package io.github.btkelly.gandalf.checker;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;

/**
 * Interface to facilitate checking the previously
 * seen {@link OptionalUpdate} and {@link Alert}.
 */
public interface HistoryChecker {

    /**
     * Checks provided {@link Alert} against the last seen one.
     * @param alert item to compare against the history
     * @return {@code true} if provided alert matches the history
     */
    boolean contains(Alert alert);

    /**
     * Checks provided {@link OptionalUpdate} against the last seen one.
     * @param optionalUpdate item to compare against the history
     * @return {@code true} if provided update matches the history
     */
    boolean contains(OptionalUpdate optionalUpdate);

}
