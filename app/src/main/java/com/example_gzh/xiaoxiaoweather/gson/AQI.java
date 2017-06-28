package com.example_gzh.xiaoxiaoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony Gui on 2017/6/14.
 */

public class AQI {

    public AQICity city;


    public class AQICity{
        public String aqi;
        public String pm25;

        /*/
        @SerializedName("qlty ")
        public String airQuality;//空气质量级别
        /*/
    }
}
