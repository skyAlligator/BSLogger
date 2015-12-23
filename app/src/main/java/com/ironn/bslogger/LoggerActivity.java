package com.ironn.bslogger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class LoggerActivity extends Activity {

    private static final String SERVICE_STARTED = "serviceStarted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoggerActivity.this);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        toggleButton.setChecked(preferences.getBoolean(SERVICE_STARTED, false));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {


                Intent service = new Intent(LoggerActivity.this, BubbleLoggerService.class);

                if (checked) {
                    startService(service);
                    preferences.edit().putBoolean(SERVICE_STARTED, true).apply();
                } else {
                    stopService(service);
                    preferences.edit().putBoolean(SERVICE_STARTED, false).apply();
                }

            }
        });

    }

}
