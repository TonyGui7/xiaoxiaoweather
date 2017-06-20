package com.example_gzh.xiaoxiaoweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by Tony Gui on 2017/6/18.
 */

public class MyApplication extends Application {
    /**
     * 创建全局Context，以供组件调用
     *
     *
     *@author Tony Gui
     * */




    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
    }



    public static Context getContext(){
        return context;
    }

}
