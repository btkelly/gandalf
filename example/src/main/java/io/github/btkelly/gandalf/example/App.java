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
package io.github.btkelly.gandalf.example;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import io.github.btkelly.gandalf.Gandalf;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * TODO: Add a class header comment!
 */
public class App extends Application {

    public static final String KEY_RES_ID = "KEY_RES_ID";

    @Override
    public void onCreate() {
        super.onCreate();

        final MockWebServer mockWebServer = new MockWebServer();

        Thread startMockServer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mockWebServer.start();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.this);

                    int mockBootstrapResId = sharedPreferences.getInt(KEY_RES_ID, R.raw.no_action_bootstrap);

                    String mockBootstrapJsonBody = getMockJsonBootstrap(mockBootstrapResId);

                    MockResponse mockResponse = new MockResponse();
                    mockResponse.setResponseCode(200);
                    mockResponse.setBody(mockBootstrapJsonBody);
                    mockWebServer.enqueue(mockResponse);

                } catch (IOException e) {
                    throw new RuntimeException("Problem starting mock web server");
                }
            }
        });

        startMockServer.start();

        try {
            startMockServer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Gandalf.Installer()
                .setContext(this)
                .setBootstrapUrl(String.valueOf(mockWebServer.url("")))
                .install();
    }

    private String getMockJsonBootstrap(@RawRes int rawRes) throws IOException {

        InputStream inputStream = getResources().openRawResource(rawRes);

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        int bufferData;

        while ((bufferData = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, bufferData);
        }

        inputStream.close();

        return writer.toString();
    }
}
