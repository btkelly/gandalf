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
 * Model to hold the Android specific bootstrap information
 */
public class Bootstrap implements Parcelable {

    private Alert alert;
    private OptionalUpdate optionalUpdate;
    private RequiredUpdate requiredUpdate;

    @Nullable
    public Alert getAlert() {
        return alert;
    }

    @Nullable
    public OptionalUpdate getOptionalUpdate() {
        return optionalUpdate;
    }

    @Nullable
    public RequiredUpdate getRequiredUpdate() {
        return requiredUpdate;
    }

    @Override
    public String toString() {
        return "Bootstrap{"
                + "alert=" + alert
                + ", optionalUpdate=" + optionalUpdate
                + ", requiredUpdate=" + requiredUpdate
                + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.alert, flags);
        dest.writeParcelable(this.optionalUpdate, flags);
        dest.writeParcelable(this.requiredUpdate, flags);
    }

    private Bootstrap() {

    }

    protected Bootstrap(Parcel in) {
        this.alert = in.readParcelable(Alert.class.getClassLoader());
        this.optionalUpdate = in.readParcelable(OptionalUpdate.class.getClassLoader());
        this.requiredUpdate = in.readParcelable(RequiredUpdate.class.getClassLoader());
    }

    public static final Parcelable.Creator<Bootstrap> CREATOR = new Parcelable.Creator<Bootstrap>() {
        public Bootstrap createFromParcel(Parcel source) {
            return new Bootstrap(source);
        }

        public Bootstrap[] newArray(int size) {
            return new Bootstrap[size];
        }
    };
}
