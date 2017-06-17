package com.example_gzh.xiaoxiaoweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example_gzh.xiaoxiaoweather.gson.Forecast;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.w3c.dom.Text;

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


        /**读取天气数据是否已经请求过*/
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);

        if(weatherString!=null){ //天气数据已经缓存到本地，直接读取
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{  //天气数据没有缓存，需要到服务器获取数据
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }





        bingPicImg=(ImageView) findViewById(R.id.bing_pic_img);
        String bingPic=prefs.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }


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
                            .url("http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9")
                            .build();

                    // http://10.0.2.2/getdata.json
                    Response response = client.newCall(request).execute();

                    String responseData = response.body().string();
                    //  parseXMLWithPull(responseData);
                 //   showToast(responseData);

                     Weather weather=Utility.handleWeatherResponse(responseData);

                    Weather weather1=new Weather();

                   weather1.status="ok";


                    showToast(weather.status);

                    updateWeatherInfoOnUIThread(weather,responseData);

                    //  parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WeatherActivity.this,"获取天气失败exception",Toast.LENGTH_SHORT).show();

                }

            }

        }).start();








    }




    /**
     * 将天气对象呈现在布局上
     * @param weather
     * */
    private void showWeatherInfo(Weather weather){

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

    }







    private void showToast(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WeatherActivity.this, "name: "+text, Toast.LENGTH_LONG).show();
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
            }
        });



    }





}
