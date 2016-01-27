package io.github.btkelly.gandalf.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.btkelly.gandalf.Gandalf;
import io.github.btkelly.gandalf.GandalfCallback;
import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;

/**
 * Abstract activity that should be extended for the simplest use of the Gandalf library
 */
public abstract class GandalfActivity extends AppCompatActivity implements GandalfCallback {

    /**
     * Called when either no action is required or the user has performed an action to skip an update
     */
    public abstract void youShallPass();

    /**
     * @return content view resource id to be shown while the Gandalf checks are in progress
     */
    @LayoutRes
    public abstract int contentView();

    private Gandalf gandalf;

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
    public final void onRequiredUpdate(Gandalf gandalf, RequiredUpdate requiredUpdate) {
        //TODO show in a dialog using dialog util
    }

    @Override
    public final void onOptionalUpdate(Gandalf gandalf, OptionalUpdate optionalUpdate) {
        //TODO show in a dialog using dialog util
    }

    @Override
    public final void onAlert(Gandalf gandalf, Alert alert) {
        //TODO show in a dialog using dialog util
    }

    @Override
    public final void onNoActionRequired() {
        youShallPass();
    }
}
