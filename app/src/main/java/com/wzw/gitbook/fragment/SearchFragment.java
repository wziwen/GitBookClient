package com.wzw.gitbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wzw.gitbook.R;
import com.wzw.gitbook.adapter.BookViewBinder;
import com.wzw.gitbook.common.AutoSpanLayoutManager;
import com.wzw.gitbook.common.OnLoadMoreListener;
import com.wzw.gitbook.entity.BookInfo;
import com.wzw.gitbook.entity.ExploreResult;
import com.wzw.gitbook.entity.SearchResult;
import com.wzw.gitbook.net.GitBookService;
import com.wzw.gitbook.net.NetProvider;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class SearchFragment extends BaseListFragment {
    GitBookService gitBookService = NetProvider.newInstance();
    private MultiTypeAdapter adapter;
    private Items items;
    private int currentPage;
    private boolean loadingData = false;
    private String keyword = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyword = getArguments()
                .getString("keyword");
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new AutoSpanLayoutManager(getContext(), recyclerView));

        adapter = new MultiTypeAdapter();
        adapter.register(BookInfo.class, new BookViewBinder());
        items = new Items();
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadExploreByPage(currentPage + 1);
            }
        });
    }

    @Override
    protected void loadData() {
        loadExploreByPage(0);
    }

    private void loadExploreByPage(final int page) {
        if (loadingData) {
            return;
        }
        loadingData = true;
        gitBookService.search(keyword, page).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SearchResult searchResult) {
                        currentPage = page;
                        if (page == 0) {
                            items.clear();
                            items.addAll(searchResult.getProps().getResults().getList());
                            adapter.notifyDataSetChanged();
                        } else {
                            int index = items.size();
                            items.addAll(searchResult.getProps().getResults().getList());
                            adapter.notifyItemRangeInserted(index, items.size() - index);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        loadingData = false;
                    }
                });
    }


}
