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
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import io.github.btkelly.gandalf.Gandalf;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;

/**
 * Helper functions for displaying a dialog for different bootstrap events
 */
public final class BootstrapDialogUtil {

    public interface BootstrapDialogListener {
        void continueLoading();
    }

    private BootstrapDialogUtil() {

    }

    public static void showRequiredUpdateDialog(@NonNull final Activity activity,
                                                @NonNull final Gandalf gandalf,
                                                @NonNull RequiredUpdate requiredUpdate) {

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    PlayStoreUtil.openPlayStoreToUpdate(activity, gandalf.getPackageName());
                } else {
                    if (ActivityStateUtil.isActivityValid(activity)) {
                        activity.finish();
                    }
                }
            }
        };

        if (ActivityStateUtil.isActivityValid(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Update Required")
                    .setMessage(requiredUpdate.getMessage())
                    .setPositiveButton("Download Update", onClickListener)
                    .setNegativeButton("Close App", onClickListener)
                    .setCancelable(false)
                    .show();
        }
    }

    public static void showOptionalUpdateDialog(@NonNull final Activity activity,
                                                @NonNull final Gandalf gandalf,
                                                @NonNull final OptionalUpdate optionalUpdate,
                                                @NonNull final BootstrapDialogListener bootstrapDialogListener) {

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    PlayStoreUtil.openPlayStoreToUpdate(activity, gandalf.getPackageName());
                } else if (which == DialogInterface.BUTTON_NEUTRAL) {
                    gandalf.save(optionalUpdate);
                    bootstrapDialogListener.continueLoading();
                } else {
                    if (ActivityStateUtil.isActivityValid(activity)) {
                        activity.finish();
                    }
                }
            }
        };

        if (ActivityStateUtil.isActivityValid(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Update Available")
                    .setMessage(optionalUpdate.getMessage())
                    .setPositiveButton("Download Update", onClickListener)
                    .setNeutralButton("Skip Update", onClickListener)
                    .setNegativeButton("Close App", onClickListener)
                    .setCancelable(false)
                    .show();
        }
    }

    public static void showAlertDialog(@NonNull final Activity activity,
                                                @NonNull final Gandalf gandalf,
                                                @NonNull final Alert alert,
                                                @NonNull final BootstrapDialogListener bootstrapDialogListener) {

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityStateUtil.isActivityValid(activity)) {
                    if (alert.isBlocking()) {
                        activity.finish();
                    } else {
                        gandalf.save(alert);
                        dialog.dismiss();
                        bootstrapDialogListener.continueLoading();
                    }
                }
            }
        };

        if (ActivityStateUtil.isActivityValid(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Alert")
                    .setMessage(alert.getMessage())
                    .setNeutralButton(alert.isBlocking() ? "Close App" : "OK", onClickListener)
                    .setCancelable(false)
                    .show();
        }
    }

}
