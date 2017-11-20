package com.wzw.gitbook.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ziwen.wen on 2017/11/20.
 */
public class BaseActivity extends AppCompatActivity {

    View decorView;
    private String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();
    }

    boolean swipeBack = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent  " + ev.getX() );
        // decorView contains Navigation bar, ues real content view
        View contentView = ((ViewGroup)decorView).getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (swipeBack) {
                // do not hard code or get the screen width, as the App may in Multi Window mode
                if (ev.getX() > contentView.getWidth() / 2) {
                    contentView.animate()
                            .translationX(contentView.getWidth())
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            })
                            .start();
                } else {
                    contentView.animate()
                            .translationX(0)
                            .start();
                }
            }
            swipeBack = false;
        } else  if (swipeBack || (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < 50)) {
            swipeBack = true;
            Log.d(TAG, "onTouch");
            contentView.setTranslationX(ev.getX());
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
