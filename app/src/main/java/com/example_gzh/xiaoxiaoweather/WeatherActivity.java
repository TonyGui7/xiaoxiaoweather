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
import android.support.percent.PercentFrameLayout;
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

   // private TextView comfortText;

  //  private TextView carWashText;

 //   private TextView sportText;

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






    /***********suggestion Layout********************/

    /*穿衣指数**/
    private PercentFrameLayout dressLayout;
    private TextView dressBriefTextView;
    private String dressSugDetail;


    /*感冒指数**/
    private PercentFrameLayout fluLayout;
    private TextView fluBriefTextView;
    private String makeupSugDetail;


    /*运动指数**/
    private PercentFrameLayout sportLayout;
    private TextView sportBriefTextView;
    private String sportSugDetail;


    /*洗车指数**/
    private PercentFrameLayout carWashLayout;
    private TextView carWashBriefTextView;
    private String carWashSugDetail;


    /*旅游指数**/
    private PercentFrameLayout travelLayout;
    private TextView travelBriefTextView;
    private String travelSugDetail;



    /*紫外线指数**/
    private PercentFrameLayout ultravioletLayout;
    private TextView ultravioletBriefTextView;
    private String ultravioletSugDetail;


    /*舒适度指数**/
    private PercentFrameLayout comfortLayout;
    private TextView comfortBriefTextView;
    private String comfortSugDetail;


    /*********************************/


    /*用户点击生活指南的选项的键**/
    public  static final String LIFE_SUGGESTION_ITEM="life_sugItem";

    /*如下7个选项**/
    public  static final int DRESS_SUGGESTION=1;

    public  static final int SPORT_SUGGESTION=2;

    public  static final int CARWASH_SUGGESTION=3;

    public  static final int TRAVEL_SUGGESTION=4;

    public  static final int ULTRAVIOLET_SUGGESTION=5;

    public  static final int COMFORT_SUGGESTION=6;

    public  static final int FLU_SUGGESTION=7;



    public  static final String LIFE_SUGGESTION_BRIET="brief";

    public  static final String LIFE_SUGGESTION_CITY="city";

    public  static final String LIFE_SUGGESTION_TEMP="temperature";

    public  static final String LIFE_SUGGESTION_DETAIL="detail";





    /****日落日出降雨量等信息**********************************/

     private TextView sunriseTimeTextView;

     private TextView sunfallTimeTextView;

     private TextView feelTempTextView;

     private TextView humidityTextView;

     private TextView pressureTextView;

     private TextView visibilityTextView;

     private TextView rainfallTextView;


    /*****************************************/





    /****风速等信息*******************/

    private TextView windDirectionTextView;

    private TextView windForceTextView;

    private TextView windSpeedTextView;


    private TextView nowWindForceTextView;
    /***********************/




    /****今天和明天的天气信息************************/

      /*今天的天气信息**/

      private Button today_aqi_Button;

      private Button today_weatherInfo_Button;

      private TextView today_TempRange_TextView;

      private TextView today_weatherInfo_TextView;



    /*明天的天气信息**/

    private Button tomorow_aqi_Button;

    private Button tomorow_weatherInfo_Button;

    private TextView tomorow_TempRange_TextView;

    private TextView tomorow_weatherInfo_TextView;

    /*********************************/





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



        sunriseTimeTextView=(TextView) findViewById(R.id.sunriseTimeTextView);

        sunfallTimeTextView=(TextView) findViewById(R.id.sunfallTimeTextView);

        feelTempTextView=(TextView) findViewById(R.id.feelTempTextView);

        humidityTextView=(TextView) findViewById(R.id.humidityTextView);

        pressureTextView=(TextView) findViewById(R.id.pressureTextView);

        visibilityTextView=(TextView) findViewById(R.id.visbilityTextView);

        rainfallTextView=(TextView) findViewById(R.id.rainfallTextView);



        windDirectionTextView=(TextView) findViewById(R.id.windDirectionTextView);

        windForceTextView=(TextView) findViewById(R.id.windForceTextView);

        windSpeedTextView=(TextView) findViewById(R.id.windSpeedTextView);


        nowWindForceTextView=(TextView) findViewById(R.id.now_windForce_textView);



         /*今天的天气信息**/

        today_aqi_Button=(Button) findViewById(R.id.today_aqi_info_Button);

        today_weatherInfo_Button=(Button) findViewById(R.id.today_weatherInfo_Button);

        today_TempRange_TextView=(TextView)findViewById(R.id.today_tempRange_TextView);

        today_weatherInfo_TextView=(TextView) findViewById(R.id.today_watherInfo_TextView);



    /*明天的天气信息**/

       tomorow_aqi_Button=(Button)findViewById(R.id.tomorow_aqi_info_Button);

        tomorow_weatherInfo_Button=(Button)findViewById(R.id.tomorow_weatherInfo_Button);

        tomorow_TempRange_TextView=(TextView)findViewById(R.id.tomorow_tempRange_TextView);

        tomorow_weatherInfo_TextView=(TextView) findViewById(R.id.tomorow_watherInfo_TextView);





        /*穿衣指数**/
        dressLayout=(PercentFrameLayout) findViewById(R.id.dressSuggestionLayout);
        dressBriefTextView=(TextView) findViewById(R.id.dressBriefTextView);
        //  String dressSugDetail;


    /*化妆指数**/
        fluLayout=(PercentFrameLayout) findViewById(R.id.fluSuggestionLayout);
        fluBriefTextView=(TextView) findViewById(R.id.fluBriefTextView);
        //   private String makeupSugDetail;


    /*运动指数**/
        sportLayout=(PercentFrameLayout) findViewById(R.id.sportSuggestionLayout);
        sportBriefTextView=(TextView) findViewById(R.id.sportBriefTextView);
        //private String sportSugDetail;


    /*洗车指数**/
        carWashLayout=(PercentFrameLayout) findViewById(R.id.carWashSuggestionLayout);
        carWashBriefTextView=(TextView) findViewById(R.id.carWashBriefTextView);
//        private String carWashSugDetail;


    /*旅游指数**/
        travelLayout=(PercentFrameLayout) findViewById(R.id.travelSuggestionLayout);
        travelBriefTextView=(TextView) findViewById(R.id.travelBriefTextView);
        // private String travelSugDetail;



    /*紫外线指数**/
        ultravioletLayout=(PercentFrameLayout) findViewById(R.id.ultravioletSuggestionLayout);
        ultravioletBriefTextView=(TextView) findViewById(R.id.ultravioletBriefTextView);
        //  private String ultravioletSugDetail;


    /*舒适度指数**/
        comfortLayout=(PercentFrameLayout) findViewById(R.id.comfortSuggestionLayout);
        comfortBriefTextView=(TextView) findViewById(R.id.comfortBriefTextView);
        //  private String comfortSugDetail;


        //  comfortText=(TextView) findViewById(R.id.comfort_text);

     //   carWashText=(TextView) findViewById(R.id.car_wash_text);

     //   sportText=(TextView) findViewById(R.id.sport_text);


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
            updateSettingFragmentBackground();
        }else{  //天气数据没有缓存，需要到服务器获取数据
            mWeatherId=getIntent().getStringExtra("weather_id");
            if(mWeatherId==null){
                mWeatherId= "CN101010100";
            }
          //  mWeatherId= "CN101010100";
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





        dressLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,DRESS_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.dress.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.dress.info);

                startActivity(i);

            }
        });





        fluLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,FLU_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.flu.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.flu.info);

                startActivity(i);
            }
        });





        sportLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,SPORT_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.sport.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.sport.info);

                startActivity(i);

            }
        });






        carWashLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,CARWASH_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.carWash.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.carWash.info);

                startActivity(i);

            }
        });





        travelLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,TRAVEL_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.travel.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.travel.info);

                startActivity(i);

            }
        });






        ultravioletLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,ULTRAVIOLET_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.ultraviolet.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.ultraviolet.info);

                startActivity(i);
            }
        });





        comfortLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i=new Intent(WeatherActivity.this,LifeSuggestionActivity.class);
                i.putExtra(LIFE_SUGGESTION_ITEM,COMFORT_SUGGESTION);
                i.putExtra(LIFE_SUGGESTION_BRIET,currentDisplayWeather.suggestion.comfort.brief);
                i.putExtra(LIFE_SUGGESTION_CITY,currentDisplayWeather.basic.cityName);
                i.putExtra(LIFE_SUGGESTION_TEMP,currentDisplayWeather.now.temperature);
                i.putExtra(LIFE_SUGGESTION_DETAIL,currentDisplayWeather.suggestion.comfort.info);

                startActivity(i);

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
                  //  Request request = new Request.Builder()
                  //          .url("https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=9f9e65060d714f22ba47a7b8c9821ef6")
                   //       .build();


                    Request request = new Request.Builder()
                            .url("http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9")
                            .build();   //能够获取未来一周的天气信息

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


                 //   showToast(weather.status);

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
                                    SettingFragment.adapter.notifyDataSetChanged();
                                }



                                /**保存一份到添加城市列表中*/
                                if (SettingFragment.addCityClick){


                                    if (addedCitesListLab.get(WeatherActivity.this).hasItem(cityName)){

                                        //对列表中的数据进行更新
                                        String weather_upate=responseData;
                                        String weatherInfo_update=weather.now.more.info;
                                        String temp_update=weather.now.temperature;

                                        addedCitesListLab.get(WeatherActivity.this).updateItemInfo(cityName,weather_upate,weatherInfo_update,temp_update);

                                        SettingFragment.adapter.notifyDataSetChanged();
                                        Toast.makeText(WeatherActivity.this,cityName+"已添加",Toast.LENGTH_SHORT).show();
                                    }else {

                                        City_item item = new City_item();
                                        item.setCityName(weather.basic.cityName);
                                        item.setCurrentTemperature(weather.now.temperature);
                                        item.setWeatherInfo(weather.now.more.info);
                                        item.setAddedCityWeather(responseData);

                                        addedCitesListLab.get(WeatherActivity.this).addCityItem(item);

                                        addedCitesListLab.get(WeatherActivity.this).saveCityLists();
                                        SettingFragment.adapter.notifyDataSetChanged();
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

        String nowWindForce=weather.now.wind.windStrength;

        titleCity.setText(cityName);

        if (updateTime!=null) {

            titleUpdateTime.setText(updateTime + "发布");
        }

        if (degree!=null) {

            degreeText.setText(degree);
        }


        if (weatherInfo!=null) {
            weatherInfoText.setText(weatherInfo);
        }


        if(nowWindForce!=null) {
            nowWindForceTextView.setText(nowWindForce);
        }


        forecastLayout.removeAllViews();

        for(Forecast forecast:weather.forecastList){

            View v= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText=(TextView) v.findViewById(R.id.date_text);

            Button infoLogoButton=(Button) v.findViewById(R.id.weatherLogoButton);

            TextView infoText=(TextView) v.findViewById(R.id.info_text);

            TextView maxText=(TextView) v.findViewById(R.id.max_text);

            TextView minText=(TextView) v.findViewById(R.id.min_text);

            String infoCode=forecast.more.infoCode;
            String weatherLogoName;
            if (infoCode!=null) {
                weatherLogoName = "weather_logo" + infoCode;
                // String weatherLogoName="test";


                int logoId = getResources().getIdentifier(weatherLogoName, "drawable", "com.example_gzh.xiaoxiaoweather");


                String sunriseTime = forecast.astronomyInfo.sunRiseTime;

                String sunfallTime = forecast.astronomyInfo.sunFallTime;

                String moonriseTime = forecast.astronomyInfo.moonRiseTime;


                String moonfallTime = forecast.astronomyInfo.moonFallTime;


                infoLogoButton.setBackgroundResource(logoId);

                if (forecast.date!=null) {
                    dateText.setText(forecast.date);

                }

                if (forecast.more.info!=null) {
                    infoText.setText(forecast.more.info);
                }

                if (forecast.temperature.max!=null) {
                    maxText.setText(forecast.temperature.max);
                }

                if (forecast.temperature.min!=null) {
                    minText.setText(forecast.temperature.min);
                }

            }
            forecastLayout.addView(v);

        }



        if (weather.aqi!=null){
            String aqi=weather.aqi.city.aqi;
            String pm25=weather.aqi.city.pm25;

                aqiText.setText(aqi);




                String today_aqi_info=weather.aqi.city.aqi;
                int aqi_backgroundId=0;
                if (today_aqi_info!=null) {
                    int aqi_info=Integer.parseInt(today_aqi_info);



                    if (aqi_info < 50) {//空气质量优秀
                        today_aqi_info = today_aqi_info + "优";
                        String aqi_background="aqi_excellent";
                        // String weatherLogoName="test";
                        aqi_backgroundId=getResources().getIdentifier(aqi_background,"drawable","com.example_gzh.xiaoxiaoweather");

                    }

                    else if (aqi_info>= 51 &&
                            aqi_info<= 100) {//空气质量良好

                        today_aqi_info = today_aqi_info + "良";

                        String aqi_background="aqi_good";
                        // String weatherLogoName="test";
                        aqi_backgroundId=getResources().getIdentifier(aqi_background,"drawable","com.example_gzh.xiaoxiaoweather");

                    }


                    else if (aqi_info>= 101 &&
                            aqi_info<= 200) {//轻度污染
                        today_aqi_info = today_aqi_info + "轻度污染";

                        String aqi_background="aqi_light_pollute";
                        // String weatherLogoName="test";
                        aqi_backgroundId=getResources().getIdentifier(aqi_background,"drawable","com.example_gzh.xiaoxiaoweather");

                    }

                    else if (aqi_info>= 201 &&
                            aqi_info<= 300) {//中度污染
                        today_aqi_info = today_aqi_info + "中度污染";

                        String aqi_background="aqi_middle_pollute";
                        // String weatherLogoName="test";
                        aqi_backgroundId=getResources().getIdentifier(aqi_background,"drawable","com.example_gzh.xiaoxiaoweather");

                    }

                    else if (aqi_info>= 301) {//重度污染
                        today_aqi_info = today_aqi_info + "重度污染";

                        String aqi_background="aqi_severe_pollute";
                        // String weatherLogoName="test";
                        aqi_backgroundId=getResources().getIdentifier(aqi_background,"drawable","com.example_gzh.xiaoxiaoweather");

                    }
                    today_aqi_Button.setText(today_aqi_info);
                }
          /*今天的天气信息**/


                if (aqi_backgroundId!=0) {
                    today_aqi_Button.setBackgroundResource(aqi_backgroundId);
                }
                else {
                    today_aqi_Button.setVisibility(View.INVISIBLE);
                }

            if (pm25!=null) {
                pm25Text.setText(pm25);
            }

        }
        else{
            aqiText.setTextSize(10);
            aqiText.setText("Sorry,未获取数据");
            pm25Text.setTextSize(10);
            pm25Text.setText("Sorry,未获取数据");
            today_aqi_Button.setVisibility(View.INVISIBLE);
        }




     //   today_aqi_Button.setBackground(Color.);



        if (weather.forecastList.get(0)!=null) {
            String todayinfoCode = weather.forecastList.get(0).more.infoCode;

            if (todayinfoCode!=null) {
                String today_weatherLogoName = "weather_logo" + todayinfoCode;
                // String weatherLogoName="test";
                int today_logoId = getResources().getIdentifier(today_weatherLogoName, "drawable", "com.example_gzh.xiaoxiaoweather");


                today_weatherInfo_Button.setBackgroundResource(today_logoId);

                String today_tempRange = weather.forecastList.get(0).temperature.min +
                        "~" + weather.forecastList.get(0).temperature.max + "℃";

                today_TempRange_TextView.setText(today_tempRange);

                String todayWeatherInfo = weather.forecastList.get(0).more.info;

                today_weatherInfo_TextView.setText(todayWeatherInfo);

            }
        }


    /*明天的天气信息**/

        tomorow_aqi_Button.setVisibility(View.INVISIBLE);

        String tomorowinfoCode=weather.forecastList.get(1).more.infoCode;

        if (tomorowinfoCode!=null) {
            String tomorow_weatherLogoName = "weather_logo" + tomorowinfoCode;
            // String weatherLogoName="test";
            int tomorow_logoId = getResources().getIdentifier(tomorow_weatherLogoName, "drawable", "com.example_gzh.xiaoxiaoweather");


            tomorow_weatherInfo_Button.setBackgroundResource(tomorow_logoId);

            String tomorow_tempRange = weather.forecastList.get(1).temperature.min +
                    "~" + weather.forecastList.get(1).temperature.max + "℃";


            tomorow_TempRange_TextView.setText(tomorow_tempRange);

            String tomorow_WeatherInfo = weather.forecastList.get(1).more.info;
            tomorow_weatherInfo_TextView.setText(tomorow_WeatherInfo);

        }

        /*简要生活建议**/
        String dressBrief=weather.suggestion.dress.brief;

        String sportBrief=weather.suggestion.sport.brief;

        String carWashBrief=weather.suggestion.carWash.brief;

        String travelBrief=weather.suggestion.travel.brief;

        String ultravioletBrief=weather.suggestion.ultraviolet.brief;

        String comfortBrief=weather.suggestion.comfort.brief;

        String fluBrief=weather.suggestion.flu.brief;


        /*详细生活建议**/
        String dressDetail=weather.suggestion.dress.info;

        String sportDetail=weather.suggestion.sport.info;

        String carWashDetail=weather.suggestion.carWash.info;

        String travelDetail=weather.suggestion.travel.info;

        String ultravioletDetail=weather.suggestion.ultraviolet.info;

        String comfortDetail=weather.suggestion.comfort.info;

        String fluDetail=weather.suggestion.flu.info;

     //   CharSequence briefTest="le";

        /*穿衣指数**/

        dressBriefTextView.setText(dressBrief);



    /*感冒指数**/

    if (fluBrief!=null) {
        fluBriefTextView.setText(fluBrief);
    }


    /*运动指数**/
         if (sportBrief!=null) {
             sportBriefTextView.setText(sportBrief);
         }


    /*洗车指数**/

    if (carWashBrief!=null) {
        carWashBriefTextView.setText(carWashBrief);
    }


    /*旅游指数**/
if (travelBrief!=null) {
    travelBriefTextView.setText(travelBrief);
}



    /*紫外线指数**/
if (ultravioletBrief!=null) {
    ultravioletBriefTextView.setText(ultravioletBrief);
}


    /*舒适度指数**/
if (comfortBrief!=null) {
    comfortBriefTextView.setText(comfortBrief);
}


if (weather.forecastList.get(0).astronomyInfo!=null) {

    String sunriseTime = weather.forecastList.get(0).astronomyInfo.sunRiseTime;

    String sunfallTime = weather.forecastList.get(0).astronomyInfo.sunFallTime;

    String moonriseTime = weather.forecastList.get(0).astronomyInfo.moonRiseTime;

    String moonfallTime = weather.forecastList.get(0).astronomyInfo.moonFallTime;


    String feelTemp = weather.now.feelTemperature + "℃";

    String humidity = weather.now.humidity + "%";

    String pressure = weather.now.pressure + "Pa";

    String visibility = weather.now.visibility + "km";

    String rainfall = weather.now.rainfall + "mm";


    if (sunriseTime!=null) {
        sunriseTimeTextView.setText(sunriseTime);
    }

    if (sunfallTime!=null) {
        sunfallTimeTextView.setText(sunfallTime);
    }

if (feelTemp!=null) {
    feelTempTextView.setText(feelTemp);
}

if (humidity!=null) {
    humidityTextView.setText(humidity);
}

     if (pressure!=null) {
         pressureTextView.setText(pressure);
     }


     if (visibility!=null) {
         visibilityTextView.setText(visibility);
     }

     if (rainfall!=null) {
         rainfallTextView.setText(rainfall);
     }

}


if (weather.now.wind!=null) {
    String windDirection = weather.now.wind.windDirection;

    String windForce = weather.now.wind.windStrength + "级";

    String windSpeed = weather.now.wind.windSpeed + "km/h";

if (windDirection!=null&&windForce!=null&&windSpeed!=null) {
    windDirectionTextView.setText(windDirection);

    windForceTextView.setText(windForce);

    windSpeedTextView.setText(windSpeed);
}

}


        /*/
        String comfort="舒适度:    "+weather.suggestion.comfort.info;

        String carWash="洗车指数:    "+weather.suggestion.carWash.info;

        String sport="运动指数:    "+weather.suggestion.sport.info;

        comfortText.setText(comfort);

        carWashText.setText(carWash);

        sportText.setText(sport);
        /*/

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
