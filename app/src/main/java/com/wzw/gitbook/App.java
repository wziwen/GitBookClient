package com.wzw.gitbook;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class App extends Application{

    public static Application CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
        FileDownloader.setupOnApplicationOnCreate(this);
        PreferenceManager.setDefaultValues(this, R.xml.setting_pref, false);
        BlockCanary.install(this, new BlockCanaryContext()).start();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
