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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.github.btkelly.gandalf.Gandalf;
import io.github.btkelly.gandalf.example.utils.MockWebServerUtil;
import io.github.btkelly.gandalf.models.Bootstrap;
import io.github.btkelly.gandalf.utils.LoggerUtil;

/**
 * TODO: Add a class header comment!
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String mockBootstrapUrl = MockWebServerUtil.startMockWebServer(this);

        new Gandalf.Installer()
                .setContext(this)
                .setPackageName("com.github.stkent.bugshaker")
                .setBootstrapUrl(mockBootstrapUrl)
                //.setCustomDeserializer(this.customDeserializer) //Uncomment this line to include a custom deserializers to allow for a custom JSON structure.
                .install();

        LoggerUtil.logD("Mock server started at " + mockBootstrapUrl);
    }

    private JsonDeserializer<Bootstrap> customDeserializer = new JsonDeserializer<Bootstrap>() {
        @Override
        public Bootstrap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            //Inspect the json to retrieve the pieces of the Bootstrap file and return using the builder like below

            return new Bootstrap.Builder()
                    .setAlertBlocking(false)
                    .setAlertMessage("Down for maintenance.")
                    .setOptionalVersion("8")
                    .setOptionalMessage("There is a newer version of the app, please update below.")
                    .setMinimumVersion("6")
                    .setRequiredMessage("You must update to the latest version of the app.")
                    .build();
        }
    };
}
