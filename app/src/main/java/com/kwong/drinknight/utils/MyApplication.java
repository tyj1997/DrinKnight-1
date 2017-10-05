package com.kwong.drinknight.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by 锐锋 on 2017/10/5.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
