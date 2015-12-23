package com.ironn.bslogger;

import android.content.Context;
import android.media.Image;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Sky_Alligator on 12/23/2015.
 * org SkyWarpTechnology
 */
public class LogBubble extends ImageView {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private final WindowManager windowManager;

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
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                windowManager.updateViewLayout(this, params);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
