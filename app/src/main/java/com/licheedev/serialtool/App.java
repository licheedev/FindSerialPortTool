package com.licheedev.serialtool;

import android.app.Application;
import android.os.Handler;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class App extends Application {

    private Handler mUiHandler;
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        mUiHandler = new Handler();
    }

    public static App instance() {
        return sInstance;
    }
}
