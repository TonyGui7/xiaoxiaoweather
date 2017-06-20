package com.example_gzh.xiaoxiaoweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example_gzh.xiaoxiaoweather.WeatherActivity;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }



    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        updateWeather();
        updateBingPic();

        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        int oneHour=1*60*60*1000;//一小时的毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+oneHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);


    }



    /**
     * 后台定时更新天气信息
     * */
    private void updateWeather(){


        new Thread(new Runnable() {
            @Override
            public void run() {

                /**读取天气数据是否已经请求过*/
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
                String weatherString=prefs.getString("weather",null);

                if(weatherString!=null){ //天气数据已经缓存到本地，直接读取
                    Weather weather= Utility.handleWeatherResponse(weatherString);
                    String weatherId=weather.basic.weatherId;

                    String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";

                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9")
                                .build();

                        // http://10.0.2.2/getdata.json
                        Response response = client.newCall(request).execute();

                        final String responseData = response.body().string();
                        //  parseXMLWithPull(responseData);
                        //   showToast(responseData);

                        weather = Utility.handleWeatherResponse(responseData);

                        if (weather!=null&&"ok".equals(weather.status)){//获取天气数据成功
                            SharedPreferences.Editor editor=PreferenceManager
                                    .getDefaultSharedPreferences(AutoUpdateService.this).edit();

                            editor.putString("weather",responseData);
                            editor.apply();

                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        }).start();




    }


    /**
     * 后台定时更新天气界面背景图片
     * */
    private void updateBingPic(){


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String requestBingPic="http://guolin.tech/api/bing_pic";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(requestBingPic)
                            .build();

                    // http://10.0.2.2/getdata.json
                    Response response = client.newCall(request).execute();

                    final String responseData = response.body().string();
                    //  parseXMLWithPull(responseData);

                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic",responseData);
                    editor.apply();

                    //  parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();



    }


}
