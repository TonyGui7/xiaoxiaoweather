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


     /*/

    @SerializedName("fl")
    public String feelTemperature;//体感温度


    @SerializedName("hum")
   public String humidity;//相对湿度(%)


    @SerializedName("pcpn")
    public String rainfall;//降雨量(单位/mm)

    @SerializedName("pres")
    public String pressure;//气压(单位/Pa)


    @SerializedName("vis")
    public String visibility;//能见度(单位/km)



    public Wind wind;// 风

/*/



    public class More{

        @SerializedName("code")
        public String infoCode;  //天气信息info的代码

        @SerializedName("txt")
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

    /*/

}
