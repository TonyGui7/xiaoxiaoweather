package com.example_gzh.xiaoxiaoweather.component_obj;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example_gzh.xiaoxiaoweather.db.City;

import java.util.ArrayList;

/**
 * Created by Tony Gui on 2017/6/21.
 */

public class addedCitesListLab {


    private static addedCitesListLab mList;


    private Context mAppContext;

    private ArrayList<City_item> city_itemArrayList;

    private static final String addedCityFilename="addedCities";

    private SharePreference_SaveDatas prefs;


    private addedCitesListLab(Context context){
        mAppContext=context;

        prefs=new SharePreference_SaveDatas(context,addedCityFilename);

        city_itemArrayList=prefs.loadCityLists();
    }




    public static addedCitesListLab get(Context c){
        if (mList==null){
            mList=new addedCitesListLab(c.getApplicationContext());
        }
        return mList;
    }



    public ArrayList<City_item> getCityLists(){
        return city_itemArrayList;
    };



    public void addCityItem(City_item item){
        city_itemArrayList.add(item);
    }

    public City_item getCityItem(int index){
        return city_itemArrayList.get(index);

    }


    public void saveCityLists(){

        prefs.saveCityLists(city_itemArrayList);
    }



    public boolean hasItem(String city){
        for (City_item item:city_itemArrayList){
            if (city.equals(item.getCityName())){
                return true;
            }
        }

        return false;

    }



    public void deleteItem(City_item item){
        city_itemArrayList.remove(item);
    }



    public void updateItemInfo(String mcityName,String weather,String weatherInfo,String temperarure){//对列表中cityName想进行天气数据更新

        City_item item=getItem(mcityName);

        item.setAddedCityWeather(weather);
        item.setWeatherInfo(weatherInfo);
        item.setCurrentTemperature(temperarure);

        saveCityLists();
    }

    private City_item getItem(String cityName){
        for (City_item item:city_itemArrayList){
            if (cityName.equals(item.getCityName())){
                return item;
            }
        }

        return null;
    }


}
