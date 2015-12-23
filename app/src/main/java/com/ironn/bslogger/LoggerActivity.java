package com.ironn.bslogger;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class LoggerActivity extends Activity implements LoggerManager.LogListener {

    private static final String TAG = "LoggerActivity";
    private TextView logTextView;
    private LoggerManager loggerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_main);
        logTextView = (TextView) findViewById(R.id.main_logTextView);
        CheckBox enableLogChbx = (CheckBox) findViewById(R.id.main_checkBox);

        loggerManager = LoggerManager.startLog();
        loggerManager.setLogListener(this);

        enableLogChbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean stopLog) {
                loggerManager = loggerManager.setStopLog(stopLog);
                loggerManager.setLogListener(LoggerActivity.this);
            }
        });

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d(TAG, "my debug log testing");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();*/
        startService(new Intent(this, BubbleLoggerService.class));
    }

    @Override
    public void updateLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.append(log);
                logTextView.append("\n");
            }
        });
    }

    @Override
    public void logStopped() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.append("Log Stopped");
                logTextView.append("\n");
            }
        });
    }

    @Override
    public void clearLog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loggerManager.setStopLog(true);
        stopService(new Intent(this, BubbleLoggerService.class));
    }
}
