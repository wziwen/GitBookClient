package com.wzw.gitbook.common;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

/**
 * Created by ziwen.wen on 2017/11/6.
 */
public class AutoSpanLayoutManager extends GridLayoutManager {
    Context context;
    public AutoSpanLayoutManager(Context context, final RecyclerView recyclerView) {
        super(context, 1);
        this.context = context;
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int viewWidth = recyclerView.getMeasuredWidth();
                        float cardViewWidth = dpToPx(250);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        int spanCount = getSpanCount();
                        if (newSpanCount != 0 && spanCount != newSpanCount) {
                            setSpanCount(newSpanCount);
                            requestLayout();
                        }
                    }
                });
    }

    private float dpToPx(float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
