package com.wzw.gitbook.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.wzw.gitbook.entity.BookInfo;
import com.wzw.gitbook.entity.ExploreResult;
import com.wzw.gitbook.entity.WrapData;
import com.wzw.gitbook.net.NetProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ziwen.wen on 2017/11/14.
 */
public class ExploreViewModel extends ViewModel {

    private MutableLiveData<WrapData<List<BookInfo>>> bookList;
    private int currentPage = 0;
    private boolean loadingData = false;

    public LiveData<WrapData<List<BookInfo>>> getBookList() {
        if (bookList == null) {
            bookList = new MutableLiveData<>();
            bookList.setValue(new WrapData<List<BookInfo>>(new ArrayList<BookInfo>()));
            loadBooks(0);
        }
        return bookList;
    }

    private void loadBooks(final int page) {
        loadingData = true;
        NetProvider.newInstance()
                .explore("zh", page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExploreResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ExploreResult searchResult) {
                        currentPage = page;
                        WrapData<List<BookInfo>> wrapData = bookList.getValue();
                        if (page == 0) {
                            wrapData.data.clear();
                        }
                        wrapData.data.addAll(searchResult.getProps().getBooks().getList());
                        bookList.setValue(wrapData);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        WrapData<List<BookInfo>> wrapData = bookList.getValue();
                        wrapData.errorCode = 0;
                        bookList.setValue(wrapData);
                    }

                    @Override
                    public void onComplete() {
                        loadingData = false;
                    }
                });
    }
}
