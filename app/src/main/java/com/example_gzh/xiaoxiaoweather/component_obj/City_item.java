package com.example_gzh.xiaoxiaoweather.component_obj;

/**
 * Created by Tony Gui on 2017/6/20.
 */

public class City_item {

    /**
     * 针对管理城市模块的城市listView
     * 的arrayAdapter的泛型参数
     *
     * */


    /**添加到listView的城市名字*/
    private String cityName;

    /**当前的温度*/
    private String currentTemperature;

    /**当前的天气状态*/
    private String weatherInfo;


    public String getCityName() {
        return cityName;
    }


    public String getCurrentTemperature() {
        return currentTemperature;
    }


    public String getWeatherInfo() {
        return weatherInfo;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public void setCurrentTemperature(String currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }




}
