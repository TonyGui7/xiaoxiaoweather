package com.example_gzh.xiaoxiaoweather.db;

/**
 * 和City_item不同，类cityItem是本地文件中存储的全国所有城市的列表中的一个实体
 * Created by Tony Gui on 2017/6/25.
 */

public class cityItem {

    /**城市编码*/
    private String cityId;

    /**城市英文名字*/
    private String cityEnglishName;

    /**城市中文名字*/
    private String cityChineseName;

    /**所属国家代码*/
    private String countryCode;

    /**国家英文名字*/
    private String countryEnglishName;

    /**国家中文名字*/
    private String countryChineseName;

    /**所属省份英文名字*/
    private String provinceEnglishName;


    /**所属省份中文名字*/
    private String provinceChineseName;

    /**所属上级城市英文名字*/
    private String belongCityEngName;

    /**所属上级城市中文名字*/
    private String belongCityChinaName;

    /**纬度*/
    private String latitude;


    /**经度*/
    private String longitude;









    public String getCityId() {
        return cityId;
    }




    public void setCityId(String cityId) {
        this.cityId = cityId;
    }




    public String getCityEnglishName() {
        return cityEnglishName;
    }




    public void setCityEnglishName(String cityEnglishName) {
        this.cityEnglishName = cityEnglishName;
    }




    public String getCityChineseName() {
        return cityChineseName;
    }




    public void setCityChineseName(String cityChineseName) {
        this.cityChineseName = cityChineseName;
    }




    public String getCountryCode() {
        return countryCode;
    }




    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }




    public String getCountryEnglishName() {
        return countryEnglishName;
    }




    public void setCountryEnglishName(String countryEnglishName) {
        this.countryEnglishName = countryEnglishName;
    }




    public String getCountryChineseName() {
        return countryChineseName;
    }




    public void setCountryChineseName(String countryChineseName) {
        this.countryChineseName = countryChineseName;
    }




    public String getProvinceEnglishName() {
        return provinceEnglishName;
    }




    public void setProvinceEnglishName(String provinceEnglishName) {
        this.provinceEnglishName = provinceEnglishName;
    }




    public String getProvinceChineseName() {
        return provinceChineseName;
    }




    public void setProvinceChineseName(String provinceChineseName) {
        this.provinceChineseName = provinceChineseName;
    }




    public String getBelongCityEngName() {
        return belongCityEngName;
    }




    public void setBelongCityEngName(String belongCityEngName) {
        this.belongCityEngName = belongCityEngName;
    }




    public String getBelongCityChinaName() {
        return belongCityChinaName;
    }




    public void setBelongCityChinaName(String belongCityChinaName) {
        this.belongCityChinaName = belongCityChinaName;
    }




    public String getLatitude() {
        return latitude;
    }




    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }




    public String getLongitude() {
        return longitude;
    }




    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
