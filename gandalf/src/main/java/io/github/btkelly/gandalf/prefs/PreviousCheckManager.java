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
package io.github.btkelly.gandalf.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import io.github.btkelly.gandalf.checker.HistoryGate;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;

/**
 * Accesses {@link SharedPreferences} to determine if items have been
 * marked as previously viewed, and updates items with newly viewed versions.
 */
public class PreviousCheckManager implements HistoryGate {

    private static final String SHARED_PREFS_NAME = "io.github.btkelly.gandalf";

    public static final String KEY_OPTIONAL_UPDATE = "optionalUpdate";
    public static final String KEY_ALERT = "alert";

    private final SharedPreferences prefs;

    public PreviousCheckManager(@NonNull final Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean matches(@NonNull final OptionalUpdate optionalUpdate) {
        final String optionalVersion = optionalUpdate.getOptionalVersion();
        final String storedValue = prefs.getString(KEY_OPTIONAL_UPDATE, "");
        return storedValue.equals(optionalVersion);
    }

    public boolean save(@NonNull final OptionalUpdate optionalUpdate) {
        return prefs.edit().putString(KEY_OPTIONAL_UPDATE, optionalUpdate.getOptionalVersion()).commit();
    }

    public boolean matches(@NonNull final Alert alert) {
        final String message = alert.getMessage();
        final String storedValue = prefs.getString(KEY_ALERT, "");
        return storedValue.equals(message);
    }

    public boolean save(@NonNull final Alert alert) {
        return prefs.edit().putString(KEY_ALERT, alert.getMessage()).commit();
    }
}
