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

import android.support.annotation.NonNull;

import io.github.btkelly.gandalf.utils.StringUtils;

/**
 * This is the public api that allows consumers to configure the Gandalf library.
 */
public class Gandalf {

    private static Gandalf gandalfInstance;

    private String bootstrapUrl;

    public static Gandalf get() {
        synchronized (Gandalf.class) {
            if (gandalfInstance == null) {
                throw new IllegalStateException("You must first install a version of the Gandalf class using the Installer class");
            }
        }

        return gandalfInstance;
    }

    public String getBootstrapUrl() {
        return bootstrapUrl;
    }

    /**
     * Basically a builder that can be used once to `install` the singleton Gandalf instance used by the library
     */
    public static class Installer {

        private String bootstrapUrl;

        public Installer setBootstrapUrl(@NonNull String bootstrapUrl) {
            this.bootstrapUrl = bootstrapUrl;
            return this;
        }

        public void install() {

            synchronized (Gandalf.class) {
                if (gandalfInstance != null) {
                    throw new IllegalStateException("Install can only be called once and should be called inside your application onCreate()");
                }

                if (StringUtils.isBlank(this.bootstrapUrl)) {
                    throw new IllegalStateException("You must supply a bootstrap url");
                }

                Gandalf gandalf = new Gandalf();

                gandalf.bootstrapUrl = this.bootstrapUrl;

                gandalfInstance = gandalf;
            }
        }
    }

}
