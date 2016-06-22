package com.gky.volleydeme;

import android.app.Application;
import android.content.Context;

/**
 * Created by 凯阳 on 2016/6/20.
 */
public class MainApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
