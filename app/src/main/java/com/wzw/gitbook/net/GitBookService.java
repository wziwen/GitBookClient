package com.wzw.gitbook.net;

import com.wzw.gitbook.entity.ExploreResult;
import com.wzw.gitbook.entity.SearchResult;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public interface GitBookService {

    @GET("search")
    Observable<SearchResult> search(@Query("q") String keyword, @Query("page") int page);

    /**
     *
     * @param lang 语言
     *             all: 全部
     *             en: 英文
     *             zh: 中文
     *             fr: french
     *             ar: arabic
     *             es: spanish
     * @return
     */
    @GET("explore")
    Observable<ExploreResult> explore(@Query("lang") String lang, @Query("page") int page);

    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url);
}
