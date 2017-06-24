package com.example_gzh.xiaoxiaoweather.component_obj;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Tony Gui on 2017/6/21.
 */

public class SharePreference_SaveDatas {



    public static final String SIZE="list_size";

    public static final String CITY_NAME_KEY="city_name";

    public static final String TEMPERATURE_KEY="temp";

    public static final String WEATHER_INFO_KEY="info";

    public static final String WEATHER_RESPONSE_KEY="response";

    private Context mContext;

    private String mFilename;

    public SharePreference_SaveDatas(Context context,String filename){
        mContext=context;
        mFilename=filename;
    }


    public void saveCityLists(ArrayList<City_item> list){

        SharedPreferences pref=mContext.getSharedPreferences(mFilename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();


        editor.putInt(SIZE,list.size());
        if (list.size()>0) {
            int index = 1;
            for (City_item item : list) {
                editor.putString(CITY_NAME_KEY + index, item.getCityName());
                editor.putString(TEMPERATURE_KEY + index, item.getCurrentTemperature());
                editor.putString(WEATHER_INFO_KEY + index, item.getWeatherInfo());
                editor.putString(WEATHER_RESPONSE_KEY + index, item.getAddedCityWeather());

                index++;
            }


            editor.apply();

        }

    }













    public ArrayList<City_item> loadCityLists(){

        ArrayList<City_item> list=new ArrayList<>();

        City_item cityItem;

        SharedPreferences pref=mContext.getSharedPreferences(mFilename, Context.MODE_PRIVATE);

        int listSize=pref.getInt(SIZE,0);
        if (listSize>0){
            for (int iloop=1; iloop<=listSize;iloop++){
                cityItem=new City_item();
                cityItem.setCityName(pref.getString(CITY_NAME_KEY+iloop,""));
                cityItem.setCurrentTemperature(pref.getString(TEMPERATURE_KEY+iloop,""));
                cityItem.setWeatherInfo(pref.getString(WEATHER_INFO_KEY+iloop,""));
                cityItem.setAddedCityWeather(pref.getString(WEATHER_RESPONSE_KEY+iloop,""));
                list.add(cityItem);
            }

            return list;
        }

        return new ArrayList<City_item>();

    }

}
