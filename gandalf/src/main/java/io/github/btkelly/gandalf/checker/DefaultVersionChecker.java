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

import android.support.annotation.NonNull;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.AppVersionDetails;
import io.github.btkelly.gandalf.models.BootstrapException;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;
import io.github.btkelly.gandalf.utils.StringUtils;

/**
 * Default implementation of {@link VersionChecker}.
 */
public class DefaultVersionChecker implements VersionChecker {

    /**
     * Checks provided {@link RequiredUpdate} against {@link AppVersionDetails} of the currently installed
     * app.
     * @param requiredUpdate current required version information
     * @param appVersionDetails details about the current version of the install app
     * @return {@code true} if app's version is less than the required version
     */
    @Override
    public boolean showRequiredUpdate(@NonNull final RequiredUpdate requiredUpdate, @NonNull final AppVersionDetails appVersionDetails) {

        final String minimumVersionString = requiredUpdate.getMinimumVersion();

        if (StringUtils.isBlank(minimumVersionString)) {
            return false;
        }

        try {
            final Integer minimumVersion = Integer.valueOf(minimumVersionString);
            return appVersionDetails.getVersionCode() < minimumVersion;

        } catch (NumberFormatException e) {
            throw new BootstrapException("Invalid number format on RequiredUpdate version", e);
        }
    }

    /**
     * Checks provided {@link OptionalUpdate} against {@link AppVersionDetails} of the currently installed
     * app.
     * @param optionalUpdate current optional version information
     * @param appVersionDetails details about the current version of the installed app
     * @return {@code true} if app's version is behind the optional version
     */
    @Override
    public boolean showOptionalUpdate(@NonNull final OptionalUpdate optionalUpdate, @NonNull final AppVersionDetails appVersionDetails) {
        final String optionalVersionString = optionalUpdate.getOptionalVersion();

        if (StringUtils.isBlank(optionalVersionString)) {
            return false;
        }

        try {
            final Integer optionalVersionCode = Integer.valueOf(optionalVersionString);
            return appVersionDetails.getVersionCode() < optionalVersionCode;

        } catch (NumberFormatException e) {
            throw new BootstrapException("Invalid number format on OptionalUpdate version", e);
        }
    }

    /**
     * Checks provided {@link Alert}.
     * @param alert current alert information
     * @return {@code true} if alert should block
     */
    @Override
    public boolean showAlert(@NonNull final Alert alert) {
        final String message = alert.getMessage();

        return !StringUtils.isBlank(message) && alert.isBlocking();
    }

}
