package com.example_gzh.xiaoxiaoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony Gui on 2017/6/14.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;


    @SerializedName("cond")
    public More more;


    public class More{

        @SerializedName("code")
        public String infoCode;  //天气信息info的代码

        @SerializedName("txt")
        public String info;
    }

}
