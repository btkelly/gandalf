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
package io.github.btkelly.gandalf.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import io.github.btkelly.gandalf.R;

/**
 * Holds strings that will be displayed in the AlertDialog built by BootstrapDialogUtil
 */
public class DialogStringsHolder {

    private Context context;
    private String updateAvailableTitle;
    private @Nullable String updateAvailableMessage;
    private String updateRequiredTitle;
    private @Nullable String updateRequiredMessage;
    private String alertTitle;
    private @Nullable String alertMessage;
    private String downloadUpdateButton;
    private String skipUpdateButton;
    private String closeAppButton;
    private String okButton;

    public DialogStringsHolder(Context context) {
        this.context = context;
    }

    public String getUpdateAvailableTitle() {
        if (updateAvailableTitle == null) {
            setUpdateAvailableTitle(R.string.update_available_title);
        }
        return updateAvailableTitle;
    }

    @Nullable public String getUpdateAvailableMessage() {
        return updateAvailableMessage;
    }

    public String getUpdateRequiredTitle() {
        if (updateRequiredTitle == null) {
            setUpdateRequiredTitle(R.string.update_required_title);
        }
        return updateRequiredTitle;
    }

    @Nullable public String getUpdateRequiredMessage() {
        return updateRequiredMessage;
    }

    public String getAlertTitle() {
        if (alertTitle == null) {
            setUpdateAvailableTitle(R.string.alert_title);
        }
        return alertTitle;
    }

    @Nullable public String getAlertMessage() {
        return alertMessage;
    }

    public String getDownloadUpdateButtonText() {
        if (downloadUpdateButton == null) {
            setDownloadUpdateButtonText(R.string.download_update_button);
        }
        return downloadUpdateButton;
    }

    public String getSkipUpdateButtonText() {
        if (skipUpdateButton == null) {
            setSkipUpdateButtonText(R.string.skip_update_button);
        }
        return skipUpdateButton;
    }

    public String getCloseAppButtonText() {
        if (closeAppButton == null) {
            setCloseAppButtonText(R.string.close_app_button);
        }
        return closeAppButton;
    }

    public String getOkButtonText() {
        if (okButton == null) {
            setOkButtonText(R.string.ok_button);
        }
        return okButton;
    }

    public void setUpdateAvailableTitle(@StringRes int updateAvailableTitle) {
        this.updateAvailableTitle = context.getResources().getString(updateAvailableTitle);
    }

    public void setUpdateAvailableTitle(String updateAvailableTitle) {
        this.updateAvailableTitle = updateAvailableTitle;
    }

    public void setUpdateAvailableMessage(@StringRes int updateAvailableMessage) {
        this.updateAvailableMessage = context.getResources().getString(updateAvailableMessage);
    }

    public void setUpdateAvailableMessage(@Nullable String updateAvailableMessage) {
        this.updateAvailableMessage = updateAvailableMessage;
    }

    public void setUpdateRequiredTitle(@StringRes int updateRequiredTitle) {
        this.updateRequiredTitle = context.getResources().getString(updateRequiredTitle);
    }

    public void setUpdateRequiredTitle(String updateRequiredTitle) {
        this.updateRequiredTitle = updateRequiredTitle;
    }

    public void setUpdateRequiredMessage(@StringRes int updateRequiredMessage) {
        this.updateRequiredMessage = context.getResources().getString(updateRequiredMessage);
    }

    public void setUpdateRequiredMessage(@Nullable String updateRequiredMessage) {
        this.updateRequiredMessage = updateRequiredMessage;
    }

    public void setDownloadUpdateButtonText(@StringRes int downloadUpdateButton) {
        this.downloadUpdateButton = context.getResources().getString(downloadUpdateButton);
    }

    public void setDownloadUpdateButtonText(String downloadUpdateButton) {
        this.downloadUpdateButton = downloadUpdateButton;
    }

    public void setSkipUpdateButtonText(@StringRes int skipUpdateButton) {
        this.skipUpdateButton = context.getResources().getString(skipUpdateButton);
    }

    public void setSkipUpdateButtonText(String skipUpdateButton) {
        this.skipUpdateButton = skipUpdateButton;
    }

    public void setCloseAppButtonText(@StringRes int closeAppButton) {
        this.closeAppButton = context.getResources().getString(closeAppButton);
    }

    public void setCloseAppButtonText(String closeAppButton) {
        this.closeAppButton = closeAppButton;
    }

    public void setAlertTitle(@StringRes int alertTitle) {
        this.alertTitle = context.getResources().getString(alertTitle);
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public void setAlertMessage(@StringRes int alertMessage) {
        this.alertMessage = context.getResources().getString(alertMessage);
    }

    public void setAlertMessage(@Nullable String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public void setOkButtonText(@StringRes int okButton) {
        this.okButton = context.getResources().getString(okButton);
    }

    public void setOkButtonText(String okButton) {
        this.okButton = okButton;
    }
}
