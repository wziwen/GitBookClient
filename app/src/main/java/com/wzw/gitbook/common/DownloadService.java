package com.wzw.gitbook.common;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
import com.wzw.gitbook.entity.Download;
import com.wzw.gitbook.entity.ExploreResult;
import com.wzw.gitbook.net.NetProvider;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by ziwen.wen on 2017/10/26.
 */
public class DownloadService {


    public void initDownload(final String url){
        Observable.create(new ObservableOnSubscribe<Download>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Download> e) throws Exception {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder()
                        .url(url);
                try {
                    Response response = client.newCall(builder.build())
                            .execute();
                    downloadFile(response.body());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        });
        NetProvider.newInstance().downloadFile(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            downloadFile(responseBody);
                        } catch (IOException e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        OkHttpClient client = new OkHttpClient();
                        Request.Builder builder = new Request.Builder()
                                .url(url);
                        try {
                            Response response = client.newCall(builder.build())
                                    .execute();
                            downloadFile(response.body());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.zip");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        int totalFileSize;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                Log.d("DownloadService", "downloaded:" + download.getProgress());
//                showProgress(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
//        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }
}
