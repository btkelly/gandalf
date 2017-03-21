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
import android.support.annotation.Nullable;

import com.google.gson.JsonDeserializer;

import io.github.btkelly.gandalf.checker.DefaultHistoryChecker;
import io.github.btkelly.gandalf.checker.DefaultVersionChecker;
import io.github.btkelly.gandalf.checker.GateKeeper;
import io.github.btkelly.gandalf.checker.HistoryChecker;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.Bootstrap;
import io.github.btkelly.gandalf.models.GandalfException;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.network.BootstrapApi;
import io.github.btkelly.gandalf.network.BootstrapCallback;
import io.github.btkelly.gandalf.holders.DialogStringsHolder;
import io.github.btkelly.gandalf.utils.LoggerUtil;
import io.github.btkelly.gandalf.utils.LoggerUtil.LogLevel;
import io.github.btkelly.gandalf.utils.OnUpdateSelectedListener;
import io.github.btkelly.gandalf.utils.PlayStoreUpdateListener;
import io.github.btkelly.gandalf.utils.StringUtils;
import okhttp3.OkHttpClient;

/**
 * This is the public api that allows consumers to configure the Gandalf library.
 */
public final class Gandalf {

    private static Gandalf gandalfInstance;

    private final Context context;
    private final OkHttpClient okHttpClient;
    private final String bootstrapUrl;
    private final HistoryChecker historyChecker;
    private final GateKeeper gateKeeper;
    private final OnUpdateSelectedListener onUpdateSelectedListener;
    private final JsonDeserializer<Bootstrap> customDeserializer;
    private final DialogStringsHolder dialogStringsHolder;

    private Gandalf(Context context, OkHttpClient okHttpClient, String bootstrapUrl, HistoryChecker historyChecker, GateKeeper gateKeeper,
                    OnUpdateSelectedListener onUpdateSelectedListener, JsonDeserializer<Bootstrap> customDeserializer,
                    DialogStringsHolder dialogStringsHolder) {
        this.context = context;
        this.okHttpClient = okHttpClient;
        this.bootstrapUrl = bootstrapUrl;
        this.historyChecker = historyChecker;
        this.gateKeeper = gateKeeper;
        this.onUpdateSelectedListener = onUpdateSelectedListener;
        this.customDeserializer = customDeserializer;
        this.dialogStringsHolder = dialogStringsHolder;
    }

    public static Gandalf get() {
        synchronized (Gandalf.class) {
            if (gandalfInstance == null) {
                throw new IllegalStateException("You must first install a version of the Gandalf class using the Installer class");
            }
        }

        return gandalfInstance;
    }

    private static Gandalf createInstance(@NonNull final Context context,
                                          @NonNull final OkHttpClient okHttpClient,
                                          @NonNull final String bootstrapUrl,
                                          @NonNull final HistoryChecker historyChecker,
                                          @NonNull final GateKeeper gateKeeper,
                                          @NonNull final OnUpdateSelectedListener onUpdateSelectedListener,
                                          @Nullable final JsonDeserializer<Bootstrap> customDeserializer,
                                          @NonNull final DialogStringsHolder dialogStringsHolder) {
        return new Gandalf(context,
                okHttpClient,
                bootstrapUrl,
                historyChecker,
                gateKeeper,
                onUpdateSelectedListener,
                customDeserializer,
                dialogStringsHolder);
    }

    /**
     * Returns the OnUpdateSelectedListener set during the Gandalf install
     * @return the onUpdateSelectedListener set during install
     */
    public OnUpdateSelectedListener getOnUpdateSelectedListener() {
        return this.onUpdateSelectedListener;
    }

    /**
     * Returns the DialogStringsHolder instance set during the Gandalf Install
     * @return the DialogStringsHolder instance set during install
     */
    public DialogStringsHolder getDialogStringsHolder() {
        return dialogStringsHolder;
    }

    /**
     * Saves the provided {@link OptionalUpdate} to the history checker.
     * @param optionalUpdate the provided current optional update information
     * @return {@code true} if {@code optionalUpdate} was successfully saved
     */
    public boolean save(@NonNull final OptionalUpdate optionalUpdate) {
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

        LoggerUtil.logD("Fetching bootstrap");

        new BootstrapApi(context, okHttpClient, bootstrapUrl, customDeserializer)
                .fetchBootstrap(new BootstrapCallback() {

                    @Override
                    public void onSuccess(Bootstrap bootstrap) {

                        LoggerUtil.logD("Fetched bootstrap: " + bootstrap);

                        try {
                            if (gateKeeper.updateIsRequired(bootstrap)) {
                                LoggerUtil.logD("Update is required");
                                gandalfCallback.onRequiredUpdate(bootstrap.getRequiredUpdate());
                            } else if (gateKeeper.showAlert(bootstrap)) {
                                LoggerUtil.logD("Alert");
                                gandalfCallback.onAlert(bootstrap.getAlert());
                            } else if (gateKeeper.updateIsOptional(bootstrap)) {
                                LoggerUtil.logD("Update is optional");
                                gandalfCallback.onOptionalUpdate(bootstrap.getOptionalUpdate());
                            } else {
                                LoggerUtil.logD("No action is required");
                                gandalfCallback.onNoActionRequired();
                            }
                        } catch (GandalfException exception) {
                            gandalfCallback.onError(exception);
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        LoggerUtil.logE("Error fetching bootstrap: " + e.getMessage());
                        //In any error case we should let the user in as to not block based on a bug
                        gandalfCallback.onError(new GandalfException(e));
                    }
                });
    }

    /**
     * Basically a builder that can be used once to `install` the singleton Gandalf instance used by the library
     */
    public static class Installer {

        private Context context;
        private OkHttpClient okHttpClient;
        private String bootstrapUrl;
        private String packageName;
        private OnUpdateSelectedListener onUpdateSelectedListener;
        private JsonDeserializer<Bootstrap> customDeserializer;
        @LogLevel private int logLevel = LoggerUtil.NONE;

        private DialogStringsHolder dialogStringsHolder;

        public Installer setContext(Context context) {
            this.context = context;
            return this;
        }

        public Installer setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Installer setBootstrapUrl(@NonNull String bootstrapUrl) {
            this.bootstrapUrl = bootstrapUrl;
            return this;
        }

        public Installer setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Installer setOnUpdateSelectedListener(OnUpdateSelectedListener onUpdateSelectedListener) {
            this.onUpdateSelectedListener = onUpdateSelectedListener;
            return this;
        }

        public Installer setCustomDeserializer(JsonDeserializer<Bootstrap> customDeserializer) {
            this.customDeserializer = customDeserializer;
            return this;
        }

        public Installer setLogLevel(@LogLevel final int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Installer setDialogStringsHolder(DialogStringsHolder dialogStringsHolder) {
            this.dialogStringsHolder = dialogStringsHolder;
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

                if (StringUtils.isBlank(this.packageName) && this.onUpdateSelectedListener == null) {
                    throw new IllegalStateException("You must supply a package name or a custom OnUpdateSelectedListener");
                }

                if (this.dialogStringsHolder == null) {
                    this.dialogStringsHolder = new DialogStringsHolder(context);
                }

                if (this.onUpdateSelectedListener == null) {
                    this.onUpdateSelectedListener = new PlayStoreUpdateListener(this.packageName);
                }

                HistoryChecker historyChecker = new DefaultHistoryChecker(this.context);

                LoggerUtil.setLogLevel(logLevel);

                gandalfInstance = createInstance(
                        this.context,
                        this.okHttpClient,
                        this.bootstrapUrl,
                        historyChecker,
                        new GateKeeper(this.context, new DefaultVersionChecker(), historyChecker),
                        this.onUpdateSelectedListener,
                        this.customDeserializer,
                        this.dialogStringsHolder
                );
            }
        }
    }

}
