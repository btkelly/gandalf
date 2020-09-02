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

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.utils.StringUtils;


/**
 * Accesses {@link SharedPreferences} to determine if items have been
 * marked as previously viewed, and updates items with newly viewed versions.
 */
public class DefaultHistoryChecker implements HistoryChecker {

    public static final String KEY_OPTIONAL_UPDATE = "optionalUpdate";
    public static final String KEY_ALERT = "alert";

    private final SharedPreferences prefs;

    public DefaultHistoryChecker(@NonNull final Context context) {
        prefs = context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE
        );
    }

    /**
     * Checks if the previous check matches the current {@link OptionalUpdate}.
     * @param optionalUpdate this should be the current optional update information
     * @return true if {@code optionalUpdate} matches the last viewed update to be recorded.
     */
    @Override
    public boolean contains(@NonNull final OptionalUpdate optionalUpdate) {
        final String optionalVersion = optionalUpdate.getOptionalVersion();
        final String storedValue = prefs.getString(KEY_OPTIONAL_UPDATE, "");
        return storedValue.equals(optionalVersion);
    }

    /**
     * Saves the provided {@link OptionalUpdate} to shared preferences.
     * @param optionalUpdate the provided current optional update information
     * @return {@code true} if {@code optionalUpdate} was successfully saved
     */
    public boolean save(@NonNull final OptionalUpdate optionalUpdate) {
        if (StringUtils.isBlank(optionalUpdate.getOptionalVersion())) {
            return false;
        }

        return prefs.edit().putString(KEY_OPTIONAL_UPDATE, optionalUpdate.getOptionalVersion()).commit();
    }

    /**
     * Checks if the provided {@link Alert} matches the last one to be seen.
     * @param alert the current optional update information
     * @return true if {@code alert} matches the last viewed alert to be recorded.
     */
    @Override
    public boolean contains(@NonNull final Alert alert) {
        final String message = alert.getMessage();
        final String storedValue = prefs.getString(KEY_ALERT, "");
        return storedValue.equals(message);
    }

    /**
     * Saves the provided {@link Alert} to shared preferences.
     * @param alert this should be the current alert
     * @return {@code true} if {@code alert} was successfully saved
     */
    public boolean save(@NonNull final Alert alert) {
        if (StringUtils.isBlank(alert.getMessage())) {
            return false;
        }

        return prefs.edit().putString(KEY_ALERT, alert.getMessage()).commit();
    }
}
