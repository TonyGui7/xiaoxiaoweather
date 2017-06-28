package com.example_gzh.xiaoxiaoweather.gson;

import android.service.dreams.DreamService;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony Gui on 2017/6/14.
 */

public class Suggestion {


    @SerializedName("comf")
    public Comfort comfort; //舒适指数

    @SerializedName("cw")
    public CarWash carWash; //洗车指数



    public Sport sport;  //运动指数

    /*/

    @SerializedName("drsg")
    public Dress dress;  //穿衣指数


    public Flu flu;


    @SerializedName("trav")   //旅游指数
    public Travel travel;


    @SerializedName("uv")
    public Ultraviolet ultraviolet;//紫外线指数


/*/




    public class Comfort{

        /*/
        @SerializedName("brf")
        public String brief;
        /*/

        @SerializedName("txt")
        public String info;
    }


    public class CarWash{

        /*/

        @SerializedName("brf")
        public String brief;
        /*/

        @SerializedName("txt")
        public String info;
    }

    public class Sport{

        /*/
        @SerializedName("brf")
        public String brief;
        /*/

        @SerializedName("txt")
        public String info;
    }


/*/
    public class Dress{

        @SerializedName("brf")
        public String brief;

        @SerializedName("txt")
        public String info;

    }



    public class Flu{

        @SerializedName("brf")
        public String brief;

        @SerializedName("txt")
        public String info;

    }




    public class Travel{

        @SerializedName("brf")
        public String brief;

        @SerializedName("txt")
        public String info;

    }



    public class Ultraviolet{

        @SerializedName("brf")
        public String brief;

        @SerializedName("txt")
        public String info;

    }

/*/

}
