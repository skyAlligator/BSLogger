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

    private static final String TAG = "LoggerManager";
    public static final String LOGCAT_CMD = "logcat -v time";
    private static LogThread logThread;
    private boolean stopLog;
    private LogListener logListener;
    private static LoggerManager instance;
    private int index;
    private ArrayList<String> lines = new ArrayList<>();

    public interface LogListener {

        /**
         * this update the log line from the logcat stream.
         *
         * @param log log line from logcat
         */
        void updateLog(String log);

        /**
         * called when logcat stopped or interrupted by exception
         */
        void logStopped();

        /**
         * clear the log in the buffer
         */
        void clearLog();

    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    public LoggerManager setStopLog(boolean stopLog) {
        if (!stopLog) {
            instance = null;
            startLog();
        }
        this.stopLog = stopLog;
        return instance;
    }

    public static LoggerManager startLog() {
        if (instance == null || !instance.isAlive()) {
            instance = new LoggerManager();
            logThread = instance.new LogThread();
            logThread.start();
            instance.start();
        } else
            Log.d(TAG, "log already Running");
        return instance;
    }

    @Override
    public void run() {
        startPrintLog();
    }

    private void startPrintLog() {
        while (!stopLog) {

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
        if (logListener != null) logListener.logStopped();
    }

    class LogThread extends Thread {

        @Override
        public void run() {
            while (!stopLog) {
                getLog();
                lines.clear();
                index = 0;
                if (logListener != null) logListener.clearLog();
            }
        }

        private void getLog() {
            Log.d(TAG, "log started");
            BufferedReader bufferedReader = null;
            try {
                Process process = Runtime.getRuntime().exec(LOGCAT_CMD);
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.isEmpty())
                        lines.add(line + "\n");
                    if (stopLog) break;
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
            Log.d(TAG, "log closed");
        }
    }

}
