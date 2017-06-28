package com.example_gzh.xiaoxiaoweather.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.MyApplication;
import com.example_gzh.xiaoxiaoweather.R;
import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.db.Province;
import com.example_gzh.xiaoxiaoweather.db.cityItem;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class Utility {



    private static cityItem requestItem=null;

    private static   boolean hasCall_hasItemOnLocalFileMethod=false;//是否刚刚调用了hasItemOnLocalFile方法



    /**
     * 解析从服务器返回的省级数据
     * @param response 服务器返回的字符串数据
     * @return boolean 解析数据成功与否
     * */
    public static boolean handleProvinceResponse(String response){


        if (!TextUtils.isEmpty(response)){


            try {
                JSONArray allPovinces=new JSONArray(response);
                for (int iloop=0;iloop<allPovinces.length();iloop++){
                    JSONObject provinceObject=allPovinces.getJSONObject(iloop);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }



        }

        return false;
    }


    /**
     * 解析从服务器返回的市级数据
     * @param response 服务器返回的字符串数据
     * @param provinceId 当前城市所属省份的Id
     * @return boolean 解析数据成功与否
     * */
    public static boolean handleCityResponse(String response,int provinceId){


        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities=new JSONArray(response);
                for (int iloop=0;iloop<allCities.length();iloop++){
                    JSONObject cityObject=allCities.getJSONObject(iloop);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }



        }

        return false;

    }




    /**
     * 解析从服务器返回的县级数据
     * @param response 服务器返回的字符串数据
     * @param cityId 当前县所属城市的Id
     * @return boolean 解析数据成功与否
     * */
    public static boolean handleCountyResponse(String response,int cityId){

        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties=new JSONArray(response);
                for (int iloop=0;iloop<allCounties.length();iloop++){
                    JSONObject countyObject=allCounties.getJSONObject(iloop);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }



        }

        return false;

    }




    /**
     * 解析服务器返回的天气数据
     * @param response
     * @return 天气对象实例
     * */
    public static Weather handleWeatherResponse(String response){

        try{

            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather5");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }








     /**
      * 从本地文件Filename中读取城市名为cityName的条目，返回该城市的Id
      * @param cityName
      * @return cityId
      * */

     public static   cityItem readFromLocalContent(Context context, String cityName)throws FileNotFoundException{


         try {



             if (hasItemOnLocalFile(context,cityName)) {
                hasCall_hasItemOnLocalFileMethod=false;
                 return requestItem;

             }else {


                 return null;
                 /*/
                 String readResult = null;
                 while ((readResult = bufferedReader.readLine()) != null) {
                     cityItem item = construct_cityItem(readResult);
                     if (cityName.equals(item.getCityChineseName())) {
                         return item;
                     }

                 }
                 /*/
             }

         }catch (Exception e){
             e.printStackTrace();
         }



         return null;
     }



public static boolean hasItemOnLocalFile(Context context,String cityName){
    try {

        String encoding="UTF-8";
        // String path=fileName;
        //File file=new File(path);
        InputStream in=context.getResources().openRawResource(R.raw.china_city_list);
        // FileInputStream in=new FileInputStream(file);
        //  InputStream in=MyApplication.getContext().getApplicationContext().getAssets().open(fileName);
        InputStreamReader reader=new InputStreamReader(in,encoding);
        BufferedReader bufferedReader=new BufferedReader(reader);
        String readResult=null;
        while((readResult=bufferedReader.readLine())!=null){
            cityItem item=construct_cityItem(readResult);
            if (cityName.equals(item.getCityChineseName())){
                hasCall_hasItemOnLocalFileMethod=true;
                requestItem=item;
                return true;
            }

        }

    }catch (Exception e){
        e.printStackTrace();
    }



    return false;


}

     /**
      * 返回本地文件中的全部城市列表
      * */
    public static ArrayList<cityItem> readFromLocalContent(Context context)throws FileNotFoundException{


        ArrayList<cityItem> list=new ArrayList<>();

        try {

            String encoding="UTF-8";
            // String path=fileName;
            //File file=new File(path);
            InputStream in=context.getResources().openRawResource(R.raw.china_city_list);
            // FileInputStream in=new FileInputStream(file);
            //  InputStream in=MyApplication.getContext().getApplicationContext().getAssets().open(fileName);
            InputStreamReader reader=new InputStreamReader(in,encoding);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String readResult=null;
            while((readResult=bufferedReader.readLine())!=null){
                cityItem item=construct_cityItem(readResult);

                list.add(item);

            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return list;
    }


    /**
     * 根据用户输入的字符来匹配可能的城市选项，用户输入的可能不是城市全名
     * @param UserInput
     * @param context
     * @return list
     * */
    public static ArrayList<cityItem> matchItem(Context context,CharSequence UserInput){

        ArrayList<cityItem> list=new ArrayList<>();

        try {

            String encoding="UTF-8";
            // String path=fileName;
            //File file=new File(path);
            InputStream in=context.getResources().openRawResource(R.raw.china_city_list);
            // FileInputStream in=new FileInputStream(file);
            //  InputStream in=MyApplication.getContext().getApplicationContext().getAssets().open(fileName);
            InputStreamReader reader=new InputStreamReader(in,encoding);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String readResult=null;
            while((readResult=bufferedReader.readLine())!=null){
                cityItem item=construct_cityItem(readResult);
                if (item.getCityChineseName().contains(UserInput)){
                    list.add(item);

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }




    private static   cityItem construct_cityItem(String readString){
        cityItem item=new cityItem();

        StringBuilder builder=new StringBuilder();
        boolean isLastCharBlank;
        boolean isCurrentCharBlank=true;
        int length=readString.length();

        int count=0;//合成字符串的次数

        for (int iloop=0;iloop<length;iloop++){
            char c=readString.charAt(iloop);
            if (c!='\t'){//当前字符不是table字符
                isLastCharBlank=isCurrentCharBlank;//上一个字符是否为空格
                builder.append(c);
                isCurrentCharBlank=false;

            }
            else {//当前字符是table字符
                isLastCharBlank=isCurrentCharBlank;
                isCurrentCharBlank=true;
                if (!isLastCharBlank&&isCurrentCharBlank) {

                    String result = builder.toString();

                    count++;
                    switch(count){
                        case 1:  //cityId
                            item.setCityId(result);
                            break;
                        case 2:  //cityEnglishName
                            item.setCityEnglishName(result);
                            break;
                        case 3:  //cityChineseName
                            item.setCityChineseName(result);
                            break;
                        case 4:   //countryCode
                            item.setCountryCode(result);
                            break;
                        case 5:    //countryEnglishName
                            item.setCountryEnglishName(result);
                            break;
                        case 6:   //countryChineseName
                            item.setCountryChineseName(result);
                            break;
                        case 7:    //provinceEnglishName
                            item.setProvinceEnglishName(result);
                            break;
                        case 8:     //provinceChineseName
                            item.setProvinceChineseName(result);
                            break;
                        case 9:     //belongCityEngName
                            item.setBelongCityEngName(result);
                            break;

                        case 10:     //belongCityChinaName
                            item.setBelongCityChinaName(result);
                            break;
                        case 11:     //latitude
                            item.setLatitude(result);
                            break;
                        case 12:      //longitude
                            item.setLongitude(result);
                            break;
                        default:
                            break;


                    }

                    builder=new StringBuilder();
                }

            }
        }
        return item;
    }







}
