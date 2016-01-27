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
package io.github.btkelly.gandalf;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.github.btkelly.gandalf.checker.DefaultVersionChecker;
import io.github.btkelly.gandalf.checker.GateKeeper;
import io.github.btkelly.gandalf.checker.HistoryChecker;
import io.github.btkelly.gandalf.checker.VersionChecker;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.Bootstrap;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.network.BootstrapApi;
import io.github.btkelly.gandalf.prefs.RecordKeeper;
import io.github.btkelly.gandalf.utils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This is the public api that allows consumers to configure the Gandalf library.
 */
public final class Gandalf {

    private static Gandalf gandalfInstance;

    private Context context;
    private String bootstrapUrl;
    private GateKeeper gateKeeper;
    private VersionChecker versionChecker;
    private HistoryChecker historyChecker;

    private Gandalf() {

    }

    public static Gandalf get() {
        synchronized (Gandalf.class) {
            if (gandalfInstance == null) {
                throw new IllegalStateException("You must first install a version of the Gandalf class using the Installer class");
            }
        }

        return gandalfInstance;
    }

    private static Gandalf createInstance() {
        return new Gandalf();
    }

    /**
     * Saves the provided {@link OptionalUpdate} to the history checker.
     * @param optionalUpdate the provided current optional update information
     * @return {@code true} if {@code optionalUpdate} was successfully saved
     */
    boolean save(@NonNull final OptionalUpdate optionalUpdate) {
        return this.historyChecker.save(optionalUpdate);
    }

    /**
     * Saves the provided {@link Alert} to the history checker.
     * @param alert this should be the current alert
     * @return {@code true} if {@code alert} was successfully saved
     */
    public boolean save(@NonNull final Alert alert) {
        return this.historyChecker.save(alert);
    }

    /**
     * Starts the flow on checking a remote file and determining if any updates or alerts are available.
     * @param gandalfCallback - a callback interface to respond to events from the bootstrap check
     */
    public void shallIPass(final GandalfCallback gandalfCallback) {
        new BootstrapApi(context, bootstrapUrl)
                .fetchBootstrap(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //In any error case we should let the user in as to not block based on a bug
                        gandalfCallback.onNoActionRequired();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            Gson gson = new GsonBuilder()
                                    .create();

                            Bootstrap bootstrap = gson.fromJson(response.body().toString(), Bootstrap.class);

                            if (gateKeeper.updateIsRequired(bootstrap)) {
                                gandalfCallback.onRequiredUpdate(Gandalf.this, bootstrap.getRequiredUpdate());
                            } else if (gateKeeper.updateIsOptional(bootstrap)) {
                                gandalfCallback.onOptionalUpdate(Gandalf.this, bootstrap.getOptionalUpdate());
                            } else if (gateKeeper.showAlert(bootstrap)) {
                                gandalfCallback.onAlert(Gandalf.this, bootstrap.getAlert());
                            } else {
                                gandalfCallback.onNoActionRequired();
                            }
                        } catch (Exception e) {
                            //In any error case we should let the user in as to not block based on a bug
                            e.printStackTrace();
                            gandalfCallback.onNoActionRequired();
                        }

                    }
                });
    }

    /**
     * Basically a builder that can be used once to `install` the singleton Gandalf instance used by the library
     */
    public static class Installer {

        private Context context;
        private String bootstrapUrl;

        public Installer setContext(Context context) {
            this.context = context;
            return this;
        }

        public Installer setBootstrapUrl(@NonNull String bootstrapUrl) {
            this.bootstrapUrl = bootstrapUrl;
            return this;
        }

        public void install() {

            synchronized (Gandalf.class) {
                if (gandalfInstance != null) {
                    throw new IllegalStateException("Install can only be called once and should be called inside your application onCreate()");
                }

                if (this.context == null) {
                    throw new IllegalStateException("You must supply a valid context");
                }

                if (StringUtils.isBlank(this.bootstrapUrl)) {
                    throw new IllegalStateException("You must supply a bootstrap url");
                }

                Gandalf gandalf = createInstance();

                gandalf.context = this.context;
                gandalf.bootstrapUrl = this.bootstrapUrl;
                gandalf.versionChecker = new DefaultVersionChecker();
                gandalf.historyChecker = new RecordKeeper(this.context);
                gandalf.gateKeeper = new GateKeeper(this.context, gandalf.versionChecker, gandalf.historyChecker);

                gandalfInstance = gandalf;
            }
        }
    }

}
