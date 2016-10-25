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
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by bryankelly on 5/17/16.
 */
public class FileDownloadUpdateListener implements OnUpdateSelectedListener {

    private final Context context;
    private final Uri fileUri;

    public FileDownloadUpdateListener(@NonNull Context context, @NonNull Uri fileUri) {
        this.context = context.getApplicationContext();
        this.fileUri = fileUri;
    }

    @Override
    public void onUpdateSelected(@NonNull Activity activity) {
        downloadFile();
        if (ActivityStateUtil.isActivityValid(activity)) {
            activity.finish();
        }
    }

    @Nullable
    protected Map<String, String> requestHeaders() {
        return null;
    }

    @NonNull
    protected String mimeType() {
        return "application/vnd.android.package-archive";
    }

    @SuppressWarnings("ConstantConditions")
    protected void downloadFile() {
        String fileName = fileUri.getLastPathSegment();

        DownloadManager.Request request = new DownloadManager.Request(fileUri)
                .setTitle(fileName)
                .setMimeType(mimeType())
                .setDestinationInExternalFilesDir(context, null, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        if (requestHeaders() != null) {
            for (Map.Entry<String, String> entry : requestHeaders().entrySet()) {
                request.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }
}
