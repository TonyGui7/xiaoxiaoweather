package com.example_gzh.xiaoxiaoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class Province extends DataSupport {


    /**存放省的Id*/
    private int id;


    /**存放省名*/
    private String provinceName;


    /**存放省编码*/
    private int provinceCode;



    /**
     * 设置当前对象的id
     * @param id
     * */
    public void setId(int id){
        this.id=id;
    }

    /**
     * 返回当前省对象的Id
     * @return id
     * */
    public int getId(){
        return id;
    }



    /**
     * 设置当前省份的名字
     * @param provinceName 省名
     * */
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }



    /**
     * @return 当前省份的名字
     * */
    public String getProvinceName(){
        return provinceName;
    }




    /**
     *设置当前省份的编码
     * @param provinceCode 省份编码
     * */
    public void setProvinceCode(int provinceCode){
        this.provinceCode=provinceCode;
    }





    /**
     * 返回当前省份的编码
     * @return provinceCode 省份编码
     * */
    public int getProvinceCode(){
        return provinceCode;
    }


}
