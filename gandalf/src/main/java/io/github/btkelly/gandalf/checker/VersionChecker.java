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
import io.github.btkelly.gandalf.models.AppVersionDetails;
import io.github.btkelly.gandalf.models.GandalfException;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;

/**
 * Checks for needed updates.
 */
public interface VersionChecker {

    /**
     * Checks if the {@link RequiredUpdate} should be shown.
     * @param requiredUpdate current required version information
     * @param appVersionDetails details about the current version of the install app
     * @return {@code true} if {@code requiredUpdate} should be shown
     * @throws GandalfException the versionCode is not a valid integer
     */
    boolean showRequiredUpdate(RequiredUpdate requiredUpdate, AppVersionDetails appVersionDetails) throws GandalfException;

    /**
     * Checks if the {@link OptionalUpdate} should be shown.
     * @param optionalUpdate current optional version information
     * @param appVersionDetails details about the current version of the installed app
     * @return {@code true} if {@code optionalUpdate} should be shown
     * @throws GandalfException the versionCode is not a valid integer
     */
    boolean showOptionalUpdate(OptionalUpdate optionalUpdate, AppVersionDetails appVersionDetails) throws GandalfException;

    /**
     * Checks if the {@link Alert} should be shown.
     * @param alert current alert information
     * @return {@code true} if {@code alert} should be shown
     */
    boolean showAlert(Alert alert);

}
