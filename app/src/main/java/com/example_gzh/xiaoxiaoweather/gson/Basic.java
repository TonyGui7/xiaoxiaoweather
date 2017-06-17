package com.example_gzh.xiaoxiaoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony Gui on 2017/6/14.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public  String weatherId;


    public Update update;


    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }


}
