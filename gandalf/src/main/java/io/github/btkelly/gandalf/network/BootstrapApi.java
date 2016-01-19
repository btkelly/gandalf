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
package io.github.btkelly.gandalf.network;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.github.btkelly.gandalf.Gandalf;
import io.github.btkelly.gandalf.utils.StringUtils;
import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Network class for fetching the bootstrap file
 */
public class BootstrapApi {

    private static final int CACHE_SIZE = 1024;
    private static final long CONNECTION_TIMEOUT_SECONDS = 30;

    private final Gandalf gandalf;
    private final OkHttpClient okHttpClient;

    /**
     * Creates a bootstrap api class
     * @param context - Android context used for setting up http cache directory
     * @param gandalf - Gandalf used to read the current configuration
     */
    public BootstrapApi(Context context, Gandalf gandalf) {
        this.gandalf = gandalf;

        File cacheDir = context.getCacheDir();
        Cache cache = new Cache(cacheDir, CACHE_SIZE);

        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Will execute a request to fetch the application bootstrap file specified by the bootstrap
     * url set on the Gandalf class. Request is executed on a background thread.
     * @throws IllegalStateException - throws if the bootstrap url has not been set
     * @param callback - an OkHttp Callback to receive call backs for the network call
     */
    public void fetchBootstrap(Callback callback) {

        if (StringUtils.isBlank(gandalf.getBootstrapUrl())) {
            throw new IllegalStateException("You must supply a bootstrap url in the Gandalf builder");
        }

        Request request = new Request.Builder()
                .url(gandalf.getBootstrapUrl())
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

}
