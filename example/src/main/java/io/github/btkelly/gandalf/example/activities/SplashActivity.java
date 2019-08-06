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
package io.github.btkelly.gandalf.example.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import androidx.annotation.RawRes;
import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

import io.github.btkelly.gandalf.activities.GandalfActivity;
import io.github.btkelly.gandalf.example.R;
import io.github.btkelly.gandalf.example.utils.MockWebServerUtil;

/**
 * An example splash activity that demonstrates the simplest way to add Gandalf to a project
 */
public class SplashActivity extends GandalfActivity {

    @Override
    public void youShallPass() {
        //After a successful bootstrap check we change the content view
        setContentView(R.layout.activity_splash_finished_loading);
    }

    @Override
    public int contentView() {
        //While the bootstrap check is running we provide a loading layout to be displayed
        return R.layout.activity_splash_loading;
    }

    //** Methods below are just for allowing switching of bootstrap file, not related to Gandalf implementation **//

    @Override
    protected void onStop() {
        super.onStop();
        MockWebServerUtil.setMockBootstrapRes(this, R.raw.no_action_bootstrap);
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuRestartAlert:
                restartApp(R.raw.alert_bootstrap);
                break;
            case R.id.menuRestartNoAction:
                restartApp(R.raw.no_action_bootstrap);
                break;
            case R.id.menuRestartOptional:
                restartApp(R.raw.update_optional_bootstrap);
                break;
            case R.id.menuRestartRequired:
                restartApp(R.raw.update_required_bootstrap);
                break;
            case R.id.menuResetState:

                PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
                        .edit()
                        .clear()
                        .commit();

                restartApp(R.raw.no_action_bootstrap);
                break;
        }

        return true;
    }

    @SuppressLint("CommitPrefEdits")
    private void restartApp(@RawRes int bootstrapRes) {
        MockWebServerUtil.setMockBootstrapRes(this, bootstrapRes);

        Intent restartApplication = new Intent(this, SplashActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 123456, restartApplication, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, mPendingIntent);

        System.exit(0);
    }
}
