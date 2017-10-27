package com.wzw.gitbook.net;

import com.wzw.gitbook.App;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class NetProvider {
    public static GitBookService newInstance() {
        File cacheFile = new File(App.CONTEXT.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 40); // 40Mb

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;

                        newRequest = request.newBuilder()
                                .addHeader("X-PJAX", "true") // GitBook 通过 X-PJAX 控制返回Json数据还是网页, 这里要Json数据
                                .build();
                        return chain.proceed(newRequest);

                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        // 强制缓存 2 天
                        return response.newBuilder()
                                .header("Cache-Control", "public, max-age=172800")
                                .build();
                    }
                })
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.gitbook.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GitBookService service = retrofit.create(GitBookService.class);
        return service;
    }
}
