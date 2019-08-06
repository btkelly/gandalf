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
import androidx.annotation.Nullable;

/**
 * Model to hold the required update information
 */
public class RequiredUpdate implements Parcelable {

    private final String minimumVersion;
    private final String message;

    @Nullable
    public String getMinimumVersion() {
        return minimumVersion;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RequiredUpdate{"
                + "minimumVersion='" + minimumVersion + '\''
                + ", message='" + message + '\''
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.minimumVersion);
        dest.writeString(this.message);
    }

    RequiredUpdate(String message, String minimumVersion) {
        this.message = message;
        this.minimumVersion = minimumVersion;
    }

    protected RequiredUpdate(Parcel in) {
        this.minimumVersion = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<RequiredUpdate> CREATOR = new Parcelable.Creator<RequiredUpdate>() {
        public RequiredUpdate createFromParcel(Parcel source) {
            return new RequiredUpdate(source);
        }

        public RequiredUpdate[] newArray(int size) {
            return new RequiredUpdate[size];
        }
    };
}
