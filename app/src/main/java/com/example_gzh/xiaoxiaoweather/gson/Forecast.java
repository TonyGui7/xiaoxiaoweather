package com.example_gzh.xiaoxiaoweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony Gui on 2017/6/14.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

/*/
    @SerializedName("hum")
    public String humidity;//相对湿度(%)


    @SerializedName("pcpn")
    public String rainfall;//降雨量(单位/mm)

    @SerializedName("pres")
    public String pressure;//气压(单位/Pa)


    @SerializedName("vis")
    public String visibility;//能见度(单位/km)



    public Wind wind;// 风


    public AstronomyInfo astronomyInfo;//天文信息

/*/

    public class Temperature{
        public String max;

        public String min;
    }


    public class More{

        @SerializedName("code_d")
        public String infoCode;  //天气信息info的代码

        @SerializedName("txt_d")
        public String info;
    }



/*/

    public class Wind{
        @SerializedName("dir")
        public String windDirection;  //风向


        @SerializedName("sr")
        public String windStrength;  //风的级数

        @SerializedName("spd")
        public String windSpeed;  //风速(单位km/h)


    }




    public class AstronomyInfo{
        @SerializedName("mr")
        public String moonRiseTime;  //月亮升起时间


        @SerializedName("ms")
        public String moonFallTime;  //月亮降下的时间


        @SerializedName("sr")
        public String sunRiseTime;  //太阳升起时间


        @SerializedName("mr")
        public String sunFallTime;  //太阳降下的时间

    }

    /*/

}
