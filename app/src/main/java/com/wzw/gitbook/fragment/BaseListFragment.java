package com.wzw.gitbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzw.gitbook.R;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public abstract class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_view, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        initView(view);

        loadData();
        return view;
    }

    protected void initView(View view) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setDistanceToTriggerSync(150);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    abstract protected void loadData();


    @Override
    public void onRefresh() {
        loadData();
    }
}
