package com.wzw.epub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by ziwen.wen on 2017/11/2.
 */
public class MotionRecyclerView extends RecyclerView {
    private static final int TAB_PERIOD_TIME = 200;
    long motionDownTime;
    float motionDownY;
    float motionDownX;
    boolean notified = false;

    EventListener eventListener;
    float tapMaxDistance;
    float swipteMinDistance;

    public MotionRecyclerView(Context context) {
        super(context);
    }

    public MotionRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MotionRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tapMaxDistance = dpToPx(4);
        swipteMinDistance = dpToPx(15);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                motionDownTime = System.currentTimeMillis();
                motionDownY = event.getY();
                motionDownX = event.getX();
                notified = false;
                break;
            case MotionEvent.ACTION_UP:
                boolean xInRect = Math.abs(event.getX() - motionDownX) < tapMaxDistance;
                boolean yInRect = Math.abs(event.getY() - motionDownY) < tapMaxDistance;
                if (System.currentTimeMillis() - motionDownTime < TAB_PERIOD_TIME && xInRect && yInRect) {
                    performClick();
                    if (eventListener != null) {
                        eventListener.onClick();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!notified) {
                    float offsetY = event.getY() - motionDownY;
                    if (offsetY > swipteMinDistance) {
                        if (eventListener != null) {
                            eventListener.onPullDown();
                            notified = true;
                        }
                    } else if (offsetY < -swipteMinDistance) {
                        if (eventListener != null) {
                            eventListener.onPullUp();
                            notified = true;
                        }
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    private float dpToPx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    public interface EventListener {
        void onClick();
        void onPullDown();
        void onPullUp();
    }
}
