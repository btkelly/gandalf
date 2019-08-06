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
 * Model to hold the alert information
 */
public class Alert implements Parcelable {

    private final String message;
    private final boolean blocking;

    @Nullable
    public String getMessage() {
        return message;
    }

    public boolean isBlocking() {
        return blocking;
    }

    @Override
    public String toString() {
        return "Alert{"
                + "message='" + message + '\''
                + ", blocking=" + blocking
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeByte(blocking ? (byte) 1 : (byte) 0);
    }

    Alert(String message, boolean blocking) {
        this.message = message;
        this.blocking = blocking;
    }

    protected Alert(Parcel in) {
        this.message = in.readString();
        this.blocking = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        public Alert createFromParcel(Parcel source) {
            return new Alert(source);
        }

        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}
