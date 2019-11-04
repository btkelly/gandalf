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
package io.github.btkelly.gandalf.activities;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.btkelly.gandalf.Gandalf;
import io.github.btkelly.gandalf.GandalfCallback;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;
import io.github.btkelly.gandalf.utils.BootstrapDialogUtil;

/**
 * Abstract activity that should be extended for the simplest use of the Gandalf library
 */
public abstract class GandalfActivity extends AppCompatActivity implements GandalfCallback, BootstrapDialogUtil.BootstrapDialogListener {

    private Gandalf gandalf;

    /**
     * Called when either no action is required or the user has performed an action to skip an update
     */
    public abstract void youShallPass();

    /**
     * @return content view resource id to be shown while the Gandalf checks are in progress
     */
    @LayoutRes
    public abstract int contentView();

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(contentView());

        this.gandalf = Gandalf.get();
        this.gandalf.shallIPass(this);
    }

    @Override
    protected final void onStart() {
        super.onStart();
    }

    @Override
    protected final void onResume() {
        super.onResume();
    }

    @Override
    public final void onRequiredUpdate(RequiredUpdate requiredUpdate) {
        BootstrapDialogUtil.showRequiredUpdateDialog(
                this,
                this.gandalf,
                requiredUpdate
        );
    }

    @Override
    public final void onOptionalUpdate(OptionalUpdate optionalUpdate) {
        BootstrapDialogUtil.showOptionalUpdateDialog(
                this,
                this.gandalf,
                optionalUpdate,
                this
        );
    }

    @Override
    public final void onAlert(Alert alert) {
        BootstrapDialogUtil.showAlertDialog(
                this,
                this.gandalf,
                alert,
                this
        );
    }

    @Override
    public final void onNoActionRequired() {
        youShallPass();
    }

    @Override
    public void continueLoading() {
        youShallPass();
    }
}
