/**
 * Copyright 2020 Bryan Kelly
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
package io.github.btkelly.gandalf.network;

import io.github.btkelly.gandalf.models.Bootstrap;

/**
 * Callback to be used when making a network request to fetch the bootstrap file. Callbacks should be
 * executed on the main thread.
 */
public interface BootstrapCallback {

    void onSuccess(Bootstrap bootstrap);

    void onError(Exception e);

}
