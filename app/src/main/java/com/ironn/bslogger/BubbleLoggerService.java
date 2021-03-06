package com.ironn.bslogger;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class BubbleLoggerService extends Service implements LoggerManager.LogListener, LogBubble.LongHoldListener {

    private static final String TAG = "BubbleLoggerService";
    private WindowManager windowManager;
    private LogBubble logBubble;
    private TextView logTextView;
    private View layout;
    private Handler handler;
    private boolean logViewAdded;

    public BubbleLoggerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        windowManager = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        initBubble();
        createLogView();
    }

    private void initBubble() {
        logBubble = new LogBubble(this);
        logBubble.setBackgroundColor(Color.TRANSPARENT);
        logBubble.setLongHoldListener(this);
        windowManager.addView(logBubble, getWindowManagerParams(0, 100, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
    }

    private void addLogView() {
        if (logViewAdded)
            return;
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        int sizeX = (int) (outSize.x * (70f / 100));
        int sizeY = (int) (outSize.y * (90f / 100));
        windowManager.addView(layout, getWindowManagerParams(0, 0, sizeX, sizeY));
        LoggerManager.runLog(true, BubbleLoggerService.this);
        logViewAdded = true;
    }

    @NonNull
    private WindowManager.LayoutParams getWindowManagerParams(int x, int y, int width, int height) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = x;
        params.y = y;
        return params;
    }

    private void createLogView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.logger_layout, null, false);
        logTextView = (TextView) layout.findViewById(R.id.main_logTextView);
//        SeekBar alphaBar = (SeekBar) layout.findViewById(R.id.alpha_seekBar);
        Button closeButton = (Button) layout.findViewById(R.id.logger_layout_button);

        /*alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float percentage = (float) i / seekBar.getMax();
                if (percentage < 0.3f) return;
                layout.setAlpha(percentage);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoggerManager.runLog(false, null);
                logTextView.setText("");
                windowManager.removeView(layout);
                logViewAdded = false;
            }
        });
    }

    @Override
    public void updateLog(final String log) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                logTextView.append(log);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoggerManager.runLog(false, null);
        if (logBubble != null)
            windowManager.removeView(logBubble);

    }

    @Override
    public void onLongPressed() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                addLogView();
            }
        });
    }
}
