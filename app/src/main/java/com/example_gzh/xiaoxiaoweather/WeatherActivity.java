package com.example_gzh.xiaoxiaoweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.component_obj.addedCitesListLab;
import com.example_gzh.xiaoxiaoweather.gson.Forecast;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.service.AutoUpdateService;
import com.example_gzh.xiaoxiaoweather.util.HttpUtil;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends ActionBarActivity {



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

 //   private LinearLayout chooseAreaLayout;


    private static Weather currentDisplayWeather;

    public static ProgressBar switchCityProgressBar;

    public static boolean hasRequestWeather=false;

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
            currentDisplayWeather=weather;
          //  currentDisplayWeatherInfoCode=weather.now.more.infoCode;
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{  //天气数据没有缓存，需要到服务器获取数据
            mWeatherId=getIntent().getStringExtra("weather_id");
           // mWeatherId= "CN101010200";
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

                    swipeRefresh.setRefreshing(false);


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
                 SettingFragment.adapter.notifyDataSetChanged();
            }
        });





        switchCityProgressBar=(ProgressBar) findViewById(R.id.switch_city_ProgressBar);


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
                            .url("https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=9f9e65060d714f22ba47a7b8c9821ef6")
                           .build();


                   // Request request = new Request.Builder()
                    //        .url("http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9")
                    //        .build();   //能够获取未来一周的天气信息

                  //  Request request = new Request.Builder()
                     //       .url("http://guolin.tech/api/weather?cityid="+weatherId+"&key=9f9e65060d714f22ba47a7b8c9821ef6")
                    //        .build();  //用自己申请的Key只能获取未来三天的天气信息

                    // http://10.0.2.2/getdata.json
                    Response response = client.newCall(request).execute();

                final  String responseData = response.body().string();
                    //  parseXMLWithPull(responseData);
                  //  showToast(responseData);

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
                                showWeatherInfo(weather);

                                /**存储当前显示天气的城市的Weather信息******/
                              //  currentDisplayWeatherInfoCode=weather.now.more.infoCode;
                                hasRequestWeather=true;
                                currentDisplayWeather=weather;
                                updateSettingFragmentBackground();
                                String cityName=weather.basic.cityName;


                                if (addedCitesListLab.get(WeatherActivity.this).hasItem(cityName)){//对添加城市列表中的天气数据更新

                                   String weather_upate=responseData;
                                   String weatherInfo_update=weather.now.more.info;
                                   String temp_update=weather.now.temperature;

                                    addedCitesListLab.get(WeatherActivity.this).updateItemInfo(cityName,weather_upate,weatherInfo_update,temp_update);

                                }



                                /**保存一份到添加城市列表中*/
                                if (SettingFragment.addCityClick){


                                    if (addedCitesListLab.get(WeatherActivity.this).hasItem(cityName)){

                                        Toast.makeText(WeatherActivity.this,cityName+"已添加",Toast.LENGTH_SHORT).show();
                                    }else {

                                        City_item item = new City_item();
                                        item.setCityName(weather.basic.cityName);
                                        item.setCurrentTemperature(weather.now.temperature);
                                        item.setWeatherInfo(weather.now.more.info);
                                        item.setAddedCityWeather(responseData);

                                        addedCitesListLab.get(WeatherActivity.this).addCityItem(item);

                                        addedCitesListLab.get(WeatherActivity.this).saveCityLists();
                                        SettingFragment.addCityClick = false;
                                    }

                                }





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

            Button infoLogoButton=(Button) v.findViewById(R.id.weatherLogoButton);

            TextView infoText=(TextView) v.findViewById(R.id.info_text);

            TextView maxText=(TextView) v.findViewById(R.id.max_text);

            TextView minText=(TextView) v.findViewById(R.id.min_text);

            String infoCode=forecast.more.infoCode;

            String weatherLogoName="weather_logo"+infoCode;
           // String weatherLogoName="test";
            int logoId=getResources().getIdentifier(weatherLogoName,"drawable","com.example_gzh.xiaoxiaoweather");

            infoLogoButton.setBackgroundResource(logoId);
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





    public void setWeatherId(String weatherId){
        mWeatherId=weatherId;
    }



    public Weather getCurrentDisplayWeather(){
        return currentDisplayWeather;
    }

    public void setCurrentDisplayWeather(Weather weatherIn){
        currentDisplayWeather=weatherIn;
    }



    /*更新SettingFragment的背景*/
    private void updateSettingFragmentBackground(){


        String prefix="weather_background";
        String CurrentinfoCode;
        if (currentDisplayWeather==null){
            CurrentinfoCode="";
        }else{
            CurrentinfoCode =currentDisplayWeather.now.more.infoCode;
        }

        // String CurrentinfoCode=WeatherActivity.currentDisplayWeatherInfoCode;

        int imageId;

        if (CurrentinfoCode.compareTo("100")==0){//天气晴朗

            String suffix="100";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }else if (CurrentinfoCode.compareTo("101")>=0
                &&CurrentinfoCode.compareTo("103")<=0){//天气多云

            String suffix="101_103";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


        }
        else if (CurrentinfoCode.compareTo("104")==0){//阴天
            String suffix="104";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }
        else if (CurrentinfoCode.compareTo("200")>=0
                &&CurrentinfoCode.compareTo("213")<=0){//起风天气
            String suffix="200_213";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }
        else if (CurrentinfoCode.compareTo("300")>=0
                &&CurrentinfoCode.compareTo("313")<=0){//下雨天气

            if (CurrentinfoCode.compareTo("302")>=0
                    &&CurrentinfoCode.compareTo("304")<=0){//下雨同时打雷

                String suffix="302_304";
                String weatherBackgroundImageName=prefix+suffix;
                imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


            }


            //只下雨不打雷
            String suffix="300_313";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


        }
        else if (CurrentinfoCode.compareTo("400")>=0
                &&CurrentinfoCode.compareTo("407")<=0){//下雪天气
            String suffix="400_407";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }
        else if (CurrentinfoCode.compareTo("500")>=0
                &&CurrentinfoCode.compareTo("501")<=0){//起雾天气
            String suffix="500_501";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


        }
        else if (CurrentinfoCode.compareTo("502")==0){//霾
            String suffix="502";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


        }
        else if (CurrentinfoCode.compareTo("503")>=0
                &&CurrentinfoCode.compareTo("508")<=0){//扬尘或沙尘暴天气

            String suffix="503_508";
            String weatherBackgroundImageName=prefix+suffix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }
        else{ //其他返回结果

            String suffix="200_213";
            String weatherBackgroundImageName=prefix;
            imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

        }


        // String weatherLogoName="test";

        SettingFragment.settingFragmentLayout.setBackgroundResource(imageId);
    }



}
