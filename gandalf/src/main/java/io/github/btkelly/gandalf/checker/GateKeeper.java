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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.AppVersionDetails;
import io.github.btkelly.gandalf.models.Bootstrap;
import io.github.btkelly.gandalf.models.BootstrapException;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;

/**
 * Checks the application's version information against a provided {@link Bootstrap}.
 */
public class GateKeeper {

    private final VersionGate versionGate;
    private final HistoryGate historyGate;
    private final AppVersionDetails appVersionDetails;

    public GateKeeper(@NonNull final Context context, @NonNull VersionGate versionGate, @NonNull HistoryGate historyGate) {
        this.versionGate = versionGate;
        this.historyGate = historyGate;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionDetails = new AppVersionDetails(packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            throw new BootstrapException("VersionCheckerGate: Problem with context package", e);
        }

    }

    /**
     * Checks version of installed app against the {@link Bootstrap}.
     * @param bootstrap - provided version requirements
     * @return true if the app's version is lower than required
     */
    public boolean mustUpdate(@NonNull final Bootstrap bootstrap) {
        final RequiredUpdate requiredUpdate = bootstrap.getRequiredUpdate();

        return requiredUpdate != null
                && versionGate.showRequiredUpdate(requiredUpdate, appVersionDetails);
    }

    /**
     * Checks version of installed app against the {@link Bootstrap}.
     * @param bootstrap - provided version requirements
     * @return true if an update is available
     */
    public boolean canUpdate(@NonNull final Bootstrap bootstrap) {
        final OptionalUpdate optionalUpdate = bootstrap.getOptionalUpdate();

        return optionalUpdate != null
                && versionGate.showOptionalUpdate(optionalUpdate, appVersionDetails)
                && historyGate.matches(optionalUpdate);
    }

    /**
     * Checks previously seen {@link Alert} against the {@link Bootstrap}
     * @param bootstrap - provided version requirements
     * @return true if alert has not been seen or is blocking
     */
    public boolean showAlert(@NonNull final Bootstrap bootstrap) {
        final Alert alert = bootstrap.getAlert();

        return alert != null
                && (versionGate.showAlert(alert) || historyGate.matches(alert));
    }

}
