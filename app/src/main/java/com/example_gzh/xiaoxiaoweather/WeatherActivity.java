package com.example_gzh.xiaoxiaoweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.gson.Forecast;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.service.AutoUpdateService;
import com.example_gzh.xiaoxiaoweather.util.HttpUtil;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.w3c.dom.Text;

import java.security.PublicKey;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {



    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private String responseData;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    public static DrawerLayout drawerLayout;

    private Button navButton;

    private Button settingButton;

    private LinearLayout settingLayout;



    private  static final String WEATHERID_STORE_KEY="weather";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT>21){
            View decoreView=getWindow().getDecorView();
            decoreView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                   View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



        setContentView(R.layout.activity_weather);

        /**初始化各组件*/
        weatherLayout=(ScrollView) findViewById(R.id.weather_layout);

        titleCity=(TextView) findViewById(R.id.title_city);

        titleUpdateTime=(TextView) findViewById(R.id.title_update_time);

        degreeText=(TextView) findViewById(R.id.degree_text);

        weatherInfoText=(TextView) findViewById(R.id.weather_info_text);

        forecastLayout=(LinearLayout) findViewById(R.id.forecast_layout);

        aqiText=(TextView) findViewById(R.id.aqi_text);

        pm25Text=(TextView) findViewById(R.id.pm25_text);

        comfortText=(TextView) findViewById(R.id.comfort_text);

        carWashText=(TextView) findViewById(R.id.car_wash_text);

        sportText=(TextView) findViewById(R.id.sport_text);


        swipeRefresh=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        /**读取天气数据是否已经请求过*/
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);

        if(weatherString!=null){ //天气数据已经缓存到本地，直接读取
            Weather weather= Utility.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{  //天气数据没有缓存，需要到服务器获取数据
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            if(HttpUtil.isNetworkConnected(WeatherActivity.this)){
                requestWeather(mWeatherId);
            }else {
                Toast.makeText(WeatherActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            }
           
        }


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){

                if (HttpUtil.isNetworkConnected(WeatherActivity.this)){//有网络连接
                    requestWeather(mWeatherId);
                    showToast("已获取最新天气信息");




                }
                else {
                    showToast("无网络链接");
                    swipeRefresh.setRefreshing(false);
                }

            }
        });



        bingPicImg=(ImageView) findViewById(R.id.bing_pic_img);
        String bingPic=prefs.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }






        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navButton=(Button) findViewById(R.id.nav_city_Button);
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });





        settingLayout=(LinearLayout) findViewById(R.id.setting_layout);
        settingButton=(Button) findViewById(R.id.setting_Button);
        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(settingLayout);

            }
        });





    }


    /**
     * 加载必应主页图片
     * */
    private void loadBingPic(){




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


                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("bing_pic",responseData);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(responseData).into(bingPicImg);
                        }
                    });

                    //  parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();





    }



    /**从服务器请求天气数据*/
    public void requestWeather(final String weatherId){

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://guolin.tech/api/weather?cityid="+weatherId+"&key=9f9e65060d714f22ba47a7b8c9821ef6")
                            .build();

                    // http://10.0.2.2/getdata.json
                    Response response = client.newCall(request).execute();

                final  String responseData = response.body().string();
                    //  parseXMLWithPull(responseData);
                 //   showToast(responseData);

                 final  Weather weather=Utility.handleWeatherResponse(responseData);


               //     Weather weather1=new Weather();

               //    weather1.status="ok";


                   // showToast(weather.status);

                //    updateWeatherInfoOnUIThread(weather,responseData);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (weather!=null&&"ok".equals(weather.status)){//获取天气数据成功
                                SharedPreferences.Editor editor=PreferenceManager
                                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                                mWeatherId=weather.basic.weatherId;
                                editor.putString("weather",responseData);
                                editor.apply();

                                /**保存一份到添加城市列表中*/
                                if (SettingFragment.addCityClick){
                                    City_item item=new City_item();
                                    item.setCityName(weather.basic.cityName);
                                    item.setCurrentTemperature(weather.now.temperature);
                                    item.setWeatherInfo(weather.now.more.info);

                                    SettingFragment.cityItemList.add(item);
                                    SettingFragment.adapter.notifyDataSetChanged();



                                    SettingFragment.addCityClick=false;
                                    editor.putString(SettingFragment.ADDED_CITY_INFO_PREF+item.getCityName(),responseData);
                                     editor.apply();
                                }



                                showWeatherInfo(weather);
                            }else {
                                Toast.makeText(WeatherActivity.this,"获取天气失败Noexception",Toast.LENGTH_SHORT).show();

                            }

                            swipeRefresh.setRefreshing(false);
                        }
                    });


                    //  parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WeatherActivity.this,"获取天气失败exception",Toast.LENGTH_SHORT).show();
                  swipeRefresh.setRefreshing(false);
                }

            }

        }).start();




         loadBingPic();



    }




    /**
     * 将天气对象呈现在布局上
     * @param weather
     * */
    public void showWeatherInfo(Weather weather){

        String cityName=weather.basic.cityName;

        String updateTime=weather.basic.update.updateTime.split(" ")[1];

        String degree=weather.now.temperature+"℃";

        String weatherInfo=weather.now.more.info;

        titleCity.setText(cityName);

        titleUpdateTime.setText(updateTime+"发布");

        degreeText.setText(degree);

        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();

        for(Forecast forecast:weather.forecastList){

            View v= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText=(TextView) v.findViewById(R.id.date_text);

            TextView infoText=(TextView) v.findViewById(R.id.info_text);

            TextView maxText=(TextView) v.findViewById(R.id.max_text);

            TextView minText=(TextView) v.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(v);

        }



        if (weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);

        }



        String comfort="舒适度:    "+weather.suggestion.comfort.info;

        String carWash="洗车指数:    "+weather.suggestion.carWash.info;

        String sport="运动指数:    "+weather.suggestion.sport.info;

        comfortText.setText(comfort);

        carWashText.setText(carWash);

        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);


        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);


    }







    private void showToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WeatherActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }






    private void updateWeatherInfoOnUIThread(final Weather weather, final String responseData){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (weather!=null&&"ok".equals(weather.status)){//获取天气数据成功
                    SharedPreferences.Editor editor=PreferenceManager
                            .getDefaultSharedPreferences(WeatherActivity.this).edit();
                    editor.putString("weather",responseData);
                    editor.apply();
                    showWeatherInfo(weather);
                }else {
                    Toast.makeText(WeatherActivity.this,"获取天气失败Noexception",Toast.LENGTH_SHORT).show();
                }

                swipeRefresh.setRefreshing(false);
            }
        });



    }





}
