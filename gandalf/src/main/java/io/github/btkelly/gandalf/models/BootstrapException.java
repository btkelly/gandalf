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
package io.github.btkelly.gandalf.models;

public class BootstrapException extends RuntimeException {

    public static final long serialVersionUID = 4326259;

    public BootstrapException() {
        super();
    }

    public BootstrapException(final String detailMessage) {
        super(detailMessage);
    }

    public BootstrapException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BootstrapException(final Throwable throwable) {
        super(throwable);
    }
}
