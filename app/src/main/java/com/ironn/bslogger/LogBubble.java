package com.ironn.bslogger;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Sky_Alligator on 12/23/2015.
 * org SkyWarpTechnology
 */
public class LogBubble extends ImageView {

    private final WindowManager windowManager;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private volatile boolean runThread;
    private LongHoldListener longHoldListener;
    private boolean stopped = true;

    public LogBubble(Context context) {
        super(context);
        setImageResource(R.drawable.bubble_ic);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        WindowManager.LayoutParams params = (WindowManager.LayoutParams) getLayoutParams();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                runThread = true;
                if (runThread && stopped) {
                    stopped = false;
                    new LongTouchTimer().start();
                }
                return true;
            case MotionEvent.ACTION_UP:
                runThread = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                windowManager.updateViewLayout(this, params);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setLongHoldListener(LongHoldListener longHoldListener) {

        this.longHoldListener = longHoldListener;
    }

    public interface LongHoldListener {
        void onLongPressed();
    }

    private class LongTouchTimer extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (runThread) {
                if (longHoldListener != null)
                    longHoldListener.onLongPressed();
            }
            stopped = true;
        }
    }
}
