package com.example_gzh.xiaoxiaoweather.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class Utility {


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


}
