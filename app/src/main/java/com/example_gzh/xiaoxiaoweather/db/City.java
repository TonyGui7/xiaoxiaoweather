package com.example_gzh.xiaoxiaoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class City extends DataSupport {



    private int id;

    private String cityName;

    private int cityCode;



    /**
     * 记录当前 城市 所属省份的Id
     * */
    private int provinceId;




    /**
     * 设置当前城市的Id
     * @param id
     * */
    public void setCityId(int id){
        this.id=id;
    }


    /**
     * 返回当前城市的Id
     * @return cityId 当前城市的id
     * */
    public int getCityId(){
        return id;
    }




    /**
     * 设置当前城市的名字
     * @param cityName
     * */
    public void setCityName(String cityName){
        this.cityName=cityName;
    }



    /**
     * 返回当前城市的名字
     * @return cityName
     * */
    public String getCityName(){
        return cityName;
    }




    /**
     * 设置当前城市编码
     * @param cityCode
     * */
    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }


    /**
     * 返回当前城市的编码
     * @return cityCode
     * */
    public int getCityCode(){
        return cityCode;
    }



    /**
     * 设置当前城市 所属 的省份的Id
     * @param provinceId
     * */
    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }



    /**
     * 返回当前城市 所属 省份 的Id
     * @return provinceId
     * */
    public int getProvinceId(){
        return provinceId;
    }




}
