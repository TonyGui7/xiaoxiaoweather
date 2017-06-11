package com.example_gzh.xiaoxiaoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class County extends DataSupport {

    private int countyId;

    private String countyName;

    private String weatherId;


    /**当前县 所属  城市的Id*/
    private int cityId;


    /**
     * 设置当前县的Id
     * @param countyId
     * */
    public void setCountyId(int countyId){
        this.countyId=countyId;
    }


    /**
     * 返回当前县的Id
     * @return countyId
     * */
    public int getCountyId(){
        return countyId;
    }


    /**
     * 设置当前县的名字
     * @param countyName
     * */
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }


    /**
     * 返回当前县的名字
     * @return countyName
     * */
    public String getCountyName(){
        return countyName;
    }



    /**
     * 设置当前县的天气Id
     * @param  weatherId (String)
     * */
    public void setWeatherId(String weatherId){
        this.weatherId=weatherId;
    }


    /**
     * 返回当前县的天气id
     * @return weatherId
     * */
    public String getWeatherId(){
        return weatherId;
    }


    /**
     * 设置当前县所属城市的Id
     * @param cityId
     * */
    public void setCityId(int cityId){
        this.cityId=cityId;
    }


    /**
     * 返回当前县所属城市的Id
     * @return cityId
     * */
    public int getCityId(){
        return cityId;
    }

}
