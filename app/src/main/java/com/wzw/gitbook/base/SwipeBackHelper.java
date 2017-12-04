package com.wzw.gitbook.base;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Created by ziwen.wen on 2017/12/4.
 */
class SwipeBackHelper {

    private final Activity activity;

    private boolean canSwipeBack;
    private boolean swipingBack;
    private static final int EDGE_SIZE = 24; // in dp
    private static final int animationTime = 200; // in milliseconds
    private ViewGroup decorView;
    private float edgeOffset;
    private int touchSlop;
    private float downX;
    private float downY;
    boolean skip;

    public SwipeBackHelper(Activity activity) {
        this.activity = activity;
        touchSlop = ViewConfiguration.get(activity).getScaledTouchSlop();
        decorView = (ViewGroup) activity.getWindow().getDecorView();
        edgeOffset = activity.getResources().getDisplayMetrics().density * EDGE_SIZE;
        canSwipeBack = true;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (canSwipeBack) {
            View contentView = (decorView).getChildAt(0);
            switch (ev.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (swipingBack) {
                        swipingBack = false;
                        // do not hard code or get the screen width, as the App may in Multi Window mode
                        // if x is large then half window width, finish activity; else recover translation
                        float offset = ev.getX() - (contentView.getWidth() / 2);
                        if (offset > 0) {
                            contentView.animate()
                                    .translationX(contentView.getWidth())
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.finish();
                                        }
                                    })
                                    .setDuration((long) (animationTime * (contentView.getWidth() / 2 - offset) / (contentView.getWidth() / 2)))
                                    .start();
                        } else {
                            contentView.animate()
                                    .translationX(0)
                                    .setDuration((long) (animationTime * Math.abs(ev.getX()) / (contentView.getWidth() / 2)))
                                    .start();
                        }
                        return true;
                    }
                    break;
                    case MotionEvent.ACTION_DOWN:
                        if (ev.getX() < edgeOffset) {
                            downX = ev.getX();
                            downY = ev.getY();
                            skip = false;
                        } else {
                            downX = -1;
                        }
                        break;
                case MotionEvent.ACTION_MOVE:
                    if (swipingBack) {
                        contentView.setTranslationX(ev.getX() - downX);
                        return true;
                    } else if (!skip && downX > 0) {
                        float offsetX = Math.abs(ev.getX() - downX);
                        float offsetY = Math.abs(ev.getY() - downY);
                        if (offsetY > 2 * touchSlop) {
                            skip = true;
                        } else if (offsetX > touchSlop) {
                            swipingBack = true;
                            contentView.setTranslationX(ev.getX() - downX);
                            // cancel future event
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                        }
                    }
                    break;
            }
        }
        return false;
    }
}
