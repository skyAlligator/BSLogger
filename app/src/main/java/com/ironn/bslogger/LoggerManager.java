package com.ironn.bslogger;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Sky_Alligator on 12/23/2015.
 * org SkyWarpTechnology
 */
public class LoggerManager extends Thread {

    public static final String LOGCAT_CMD = "logcat";
    private static final String TAG = "LoggerManager";
    private static LoggerManager loggerManager;
    private boolean runLog = true;
    private LogListener logListener;
    private int index;
    private ArrayList<String> lines = new ArrayList<>();

    public static LoggerManager runLog(boolean runLog, LogListener logListener) {
        if (!runLog) {
            if (loggerManager != null) {
                loggerManager.runLog = false;
                loggerManager.logListener = null;
                loggerManager = null;
                return null;
            }

        } else if (loggerManager == null || !loggerManager.isAlive()) {
            loggerManager = new LoggerManager();
            loggerManager.logListener = logListener;
            loggerManager.new LogThread().start();
            loggerManager.start();
        } else
            Log.d(TAG, "log already Running");

        return loggerManager;
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    @Override
    public void run() {
        startPrintLog();
    }

    private void startPrintLog() {
        while (runLog) {

            int size = lines.size();
            if (logListener != null)
                for (; index < size; index++) {
                    logListener.updateLog(lines.get(index));
                }
            try {
                sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

    }

    public interface LogListener {

        /**
         * this update the log line from the logcat stream.
         *
         * @param log log line from logcat
         */
        void updateLog(String log);

    }

    class LogThread extends Thread {

        @Override
        public void run() {
            while (runLog) {
                getLog();
                lines.clear();
                index = 0;
                try {
                    sleep(500);
                } catch (InterruptedException ignored) {
                }
//                if (logListener != null) logListener.clearLog();
            }
        }

        private void getLog() {
            BufferedReader bufferedReader = null;
            try {
                Process process = Runtime.getRuntime().exec(LOGCAT_CMD);
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.isEmpty())
                        lines.add(line + "\n");
                    if (!runLog) break;
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            } finally {
                if (bufferedReader != null)
                    try {
                        bufferedReader.close();
                    } catch (IOException ignored) {
                    }
            }
            Log.d(TAG, "stream closed ");
        }
    }

}
