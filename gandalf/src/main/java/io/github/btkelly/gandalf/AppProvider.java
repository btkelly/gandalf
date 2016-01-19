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

import android.app.Application;

/**
 * Class to provide a static accessor to the Application context
 */
public final class AppProvider {

    private static Application application;

    private AppProvider() {

    }

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        AppProvider.application = application;
    }
}
