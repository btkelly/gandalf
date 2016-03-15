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
package io.github.btkelly.gandalf.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Model to hold the optional update information
 */
public class OptionalUpdate implements Parcelable {

    private final String optionalVersion;
    private String message;

    @Nullable
    public String getOptionalVersion() {
        return optionalVersion;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OptionalUpdate{"
                + "optionalVersion='" + optionalVersion + '\''
                + ", message='" + message + '\''
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionalVersion);
        dest.writeString(this.message);
    }

    OptionalUpdate(String message, String optionalVersion) {
        this.message = message;
        this.optionalVersion = optionalVersion;
    }

    protected OptionalUpdate(Parcel in) {
        this.optionalVersion = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<OptionalUpdate> CREATOR = new Parcelable.Creator<OptionalUpdate>() {
        public OptionalUpdate createFromParcel(Parcel source) {
            return new OptionalUpdate(source);
        }

        public OptionalUpdate[] newArray(int size) {
            return new OptionalUpdate[size];
        }
    };
}
