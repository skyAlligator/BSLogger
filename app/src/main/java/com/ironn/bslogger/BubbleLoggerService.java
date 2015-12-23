package com.ironn.bslogger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class BubbleLoggerService extends Service {

    private WindowManager windowManager;
    private LogBubble logBubble;

    public BubbleLoggerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBubble();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initBubble() {
        windowManager = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);

       /* WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        layoutParams.width = 300;
        layoutParams.height = 300;

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags =
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;*/
//        layoutParams.token = getWindow().getDecorView().getRootView().getWindowToken();

        //Feel free to inflate here
        logBubble = new LogBubble(this);
        logBubble.setBackgroundColor(Color.RED);

        //Must wire up back button, otherwise it's not sent to our activity
        /*logBubble.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                }
                return true;
            }
        });*/

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(logBubble, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (logBubble != null)
            windowManager.removeView(logBubble);

    }
}
