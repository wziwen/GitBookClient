package com.wzw.gitbook.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 * Created by ziwen.wen on 2017/11/20.
 */
public class BaseActivity extends AppCompatActivity {

    private String TAG = "BaseActivity";
    SwipeBackHelper swipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeBackHelper = new SwipeBackHelper(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (swipeBackHelper.dispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
