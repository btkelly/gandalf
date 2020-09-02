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
package io.github.btkelly.gandalf.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import io.github.btkelly.gandalf.example.R;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * A utility class to help with creating a mock web server to return sample bootstrap files
 */
public final class MockWebServerUtil {

    private static final String KEY_BOOTSTRAP_RES_ID = "KEY_BOOTSTRAP_RES_ID";
    private static MockWebServer mockWebServer;

    /**
     * Starts a mock web server to return the bootstrap file set using setMockBootstrapRes(Context context, int rawResourceId)
     * @param context
     * @return
     */
    public static String startMockWebServer(@NonNull final Context context) {

        shutdownWebServer();
        mockWebServer = new MockWebServer();

        try {
            mockWebServer.start();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            int mockBootstrapResId = sharedPreferences.getInt(KEY_BOOTSTRAP_RES_ID, R.raw.no_action_bootstrap);

            String mockBootstrapJsonBody = getMockJsonBootstrap(context, mockBootstrapResId);

            MockResponse mockResponse = new MockResponse();
            mockResponse.setResponseCode(200);
            mockResponse.setBody(mockBootstrapJsonBody);
            mockResponse.setBodyDelay(2, TimeUnit.SECONDS);

            mockWebServer.enqueue(mockResponse);

        } catch (IOException e) {
            throw new RuntimeException("Problem starting mock web server");
        }


        return String.valueOf(mockWebServer.url(""));
    }

    /**
     * Updates the raw resource file id that the mock web server should use when calling startMockWebServer(Context context)
     * @param context
     * @param rawResourceId
     */
    public static void setMockBootstrapRes(@NonNull Context context, @RawRes int rawResourceId) {

        shutdownWebServer();

        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .edit()
                .putInt(KEY_BOOTSTRAP_RES_ID, rawResourceId)
                .commit();
    }

    private static void shutdownWebServer() {
        if (mockWebServer != null) {
            try {
                mockWebServer.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mockWebServer = null;
            }
        }
    }

    private static String getMockJsonBootstrap(@NonNull Context context, @RawRes int rawRes) throws IOException {

        InputStream inputStream = context.getResources().openRawResource(rawRes);

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        int bufferData;

        while ((bufferData = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, bufferData);
        }

        inputStream.close();

        return writer.toString();
    }

}
