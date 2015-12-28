package com.ironn.bslogger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;


public class LoggerActivity extends Activity {

    private static final String SERVICE_STARTED = "serviceStarted";
    private static final String TAG = "LoggerActivity";
//    private boolean run;

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
                    EditText cmdEditTxt = (EditText) findViewById(R.id.command_EditText);
                    EditText filterEditTxt = (EditText) findViewById(R.id.textFilterEditTxt);

                    LoggerManager.LOGCAT_CMD = cmdEditTxt.getText().toString();
                    LoggerManager.FILTER_TEXT = filterEditTxt.getText().toString();

                    startService(service);
                    preferences.edit().putBoolean(SERVICE_STARTED, true).apply();
                } else {
                    stopService(service);
                    preferences.edit().putBoolean(SERVICE_STARTED, false).apply();
                }

            }
        });

    }

  /* private void checkRootCommand() {
        try {
            Process p = Runtime.getRuntime().exec("mkdir testdir2", new String[]{"su"}, Environment.getExternalStorageDirectory());
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runCMD() {
        try {
            Runtime.getRuntime().exec("logcat -c").waitFor();
            Process process = Runtime.getRuntime().exec("logcat -v long *:*");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            run = true;
            while (run) {
                String nextLine = reader.readLine();
                if (!nextLine.contains("LogWatcher-D")) {
                    Log.w("LogWatcher-D", "See: " + nextLine);
                }

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }*/

    //    check permission
//    check same certificate
//    check same user id in manifest
//    check system partion installed
//    check version < 4.1+
//    check root access granted permission
  /*
    private void requestReadLogPermission() {
        String pname = getPackageName();
        if (getPackageManager().checkPermission(android.Manifest.permission.READ_LOGS, pname) != 0) {
            Log.d(TAG, "we do not have the READ_LOGS permission!");
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                Log.d(TAG, "Working around JellyBeans 'feature'...");
                try {
                    // format the commandline parameter
                    String[] CMDLINE_GRANTPERMS = {"su"};
                    String cmd = String.format("grant %s android.permission.READ_LOGS", pname);
                    java.lang.Process p = Runtime.getRuntime().exec(new String[]{cmd},new String[]{"su","pm"});
                    int res = p.waitFor();
                    Log.d(TAG, "exec returned: " + res);
                    if (res != 0)
                        throw new Exception("failed to become root");
                } catch (Exception e) {
                    Log.d(TAG, "exec(): " + e);
                    Toast.makeText(this, "Failed to obtain READ_LOGS permission", Toast.LENGTH_LONG).show();
                }
            }
        } else
            Log.d(TAG, "we have the READ_LOGS permission already!");
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        run = false;
    }
}
