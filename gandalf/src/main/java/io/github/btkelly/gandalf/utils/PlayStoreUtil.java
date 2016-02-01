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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.content.Intent.ACTION_VIEW;

public final class PlayStoreUtil {

    private static final String GOOGLE_PLAY_STORE_URI_PREFIX = "https://play.google.com/store/apps/details?id=";

    private PlayStoreUtil() {

    }

    public static void openPlayStoreToUpdate(@NonNull final Activity activity, @NonNull final String packageName) {
        if (ActivityStateUtil.isActivityValid(activity)) {

            activity.startActivity(
                    new Intent(
                            ACTION_VIEW,
                            getGooglePlayStoreUriForPackageName(packageName)
                    )
            );

            activity.finish();
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @NonNull
    private static Uri getGooglePlayStoreUriForPackageName(@NonNull final String packageName) {
        return Uri.parse(GOOGLE_PLAY_STORE_URI_PREFIX + packageName);
    }

}
