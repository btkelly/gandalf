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
import android.support.annotation.RawRes;
import android.view.View;

import io.github.btkelly.gandalf.activities.GandalfActivity;
import io.github.btkelly.gandalf.example.App;
import io.github.btkelly.gandalf.example.R;

/**
 * TODO: Add a class header comment!
 */
public class SplashActivity extends GandalfActivity implements View.OnClickListener {

    @Override
    public void youShallPass() {
        setContentView(R.layout.activity_splash_finished_loading);
        findViewById(R.id.btnRestartAlert).setOnClickListener(this);
        findViewById(R.id.btnRestartNoAction).setOnClickListener(this);
        findViewById(R.id.btnRestartOptional).setOnClickListener(this);
        findViewById(R.id.btnRestartRequired).setOnClickListener(this);
    }

    @Override
    public int contentView() {
        return R.layout.activity_splash_loading;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRestartAlert:
                restartApp(R.raw.alert_bootstrap);
                break;
            case R.id.btnRestartNoAction:
                restartApp(R.raw.no_action_bootstrap);
                break;
            case R.id.btnRestartOptional:
                restartApp(R.raw.update_optional_bootstrap);
                break;
            case R.id.btnRestartRequired:
                restartApp(R.raw.update_required_bootstrap);
                break;
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void restartApp(@RawRes int bootstrapRes) {

        PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
            .edit()
            .putInt(App.KEY_RES_ID, bootstrapRes)
            .commit();

        Intent restartApplication = new Intent(this, SplashActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 123456, restartApplication, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, mPendingIntent);

        System.exit(0);
    }
}
