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

import java.io.IOException;

import io.github.btkelly.gandalf.Gandalf;
import okhttp3.mockwebserver.MockWebServer;

/**
 * TODO: Add a class header comment!
 */
public class App extends Application {

    private static MockWebServer mockWebServer;

    @Override
    public void onCreate() {
        super.onCreate();

        mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
        } catch (IOException e) {
            throw new RuntimeException("Problem starting mock web server");
        }

        new Gandalf.Installer()
                .setContext(this)
                .setBootstrapUrl(String.valueOf(mockWebServer.url("")))
                .install();

    }

    public static MockWebServer getMockWebServer() {
        return mockWebServer;
    }
}
