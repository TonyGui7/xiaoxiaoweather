package com.example_gzh.xiaoxiaoweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example_gzh.xiaoxiaoweather.MyApplication;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }





    /**
     * 需要在Manifest.xml中申请权限
     * 判断网络是否连接
     * @return 网络连接状态
     * */
    public static boolean isNetworkConnected(Context context){

if (context!=null) {
    ConnectivityManager manager = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo =
            manager.getActiveNetworkInfo();
    if (networkInfo != null) {
        return networkInfo.isConnected();
    }

}

        return false;
    }



    /**
     * 判断设备是否连上WiFi
     * @return 设备WIFI是否可用
     * */
    public static boolean isWiFiAvailable(Context context){

        if (context!=null) {
            ConnectivityManager manager = (ConnectivityManager)
                    MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =
                    manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;

    }



    /**
     * 判断移动网络是否可用
     * @return 移动网络是否可用
     * */
    public static boolean isMobileAvailable(Context context){

        if (context!=null) {
            ConnectivityManager manager = (ConnectivityManager)
                    MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =
                    manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }


}
