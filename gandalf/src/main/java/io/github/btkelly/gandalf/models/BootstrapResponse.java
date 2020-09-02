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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Wrapper class around the bootstrap file. Used to allow multiple platform definitions in the bootstrap
 * response.
 */
public class BootstrapResponse implements Parcelable {

    public static final Parcelable.Creator<BootstrapResponse> CREATOR = new Parcelable.Creator<BootstrapResponse>() {
        public BootstrapResponse createFromParcel(Parcel source) {
            return new BootstrapResponse(source);
        }

        public BootstrapResponse[] newArray(int size) {
            return new BootstrapResponse[size];
        }
    };
    private Bootstrap android;

    private BootstrapResponse() {

    }

    protected BootstrapResponse(Parcel in) {
        this.android = in.readParcelable(Bootstrap.class.getClassLoader());
    }

    public Bootstrap getAndroid() {
        return android;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.android, flags);
    }

    @Override
    public String toString() {
        return "BootstrapResponse{"
                + "android=" + android
                + '}';
    }
}
