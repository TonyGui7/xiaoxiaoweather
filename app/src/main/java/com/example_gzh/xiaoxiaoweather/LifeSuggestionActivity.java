package com.example_gzh.xiaoxiaoweather;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LifeSuggestionActivity extends SingleFragmentActivity {

    private String brief="";

    private String city="";

    private String temperature="";

    private String detailSuggestion="";

    private int FragmentSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







    }


    @Override
    protected Fragment createFragment() {
        // TODO Auto-generated method stub

        Intent i=getIntent();

        FragmentSelected=i.getIntExtra(WeatherActivity.LIFE_SUGGESTION_ITEM,0);


        brief=i.getStringExtra(WeatherActivity.LIFE_SUGGESTION_BRIET);

        city=i.getStringExtra(WeatherActivity.LIFE_SUGGESTION_CITY);

        temperature=i.getStringExtra(WeatherActivity.LIFE_SUGGESTION_TEMP);

        detailSuggestion=i.getStringExtra(WeatherActivity.LIFE_SUGGESTION_DETAIL);




        switch (FragmentSelected){

            case WeatherActivity.DRESS_SUGGESTION:

                return new DressSugFragment();

            case WeatherActivity.SPORT_SUGGESTION:

                return new SportSugFragment();

            case WeatherActivity.CARWASH_SUGGESTION:
                return new CarwashSugFragment();

            case WeatherActivity.TRAVEL_SUGGESTION:
                return new TravelSugFragment();

            case WeatherActivity.ULTRAVIOLET_SUGGESTION:
                return new UltravioletSugFragment();

            case  WeatherActivity.COMFORT_SUGGESTION:

                return new ComfortSugFragment();

            case WeatherActivity.FLU_SUGGESTION:

               return new FluSugFragment();
            default:
                return null;


        }



    }






    public String getBrief(){
        return brief;
    }

    public String getCity(){
        return city;
    }

    public String getTemperature(){
        return temperature;
    }

    public String getDetailSuggestion(){
        return detailSuggestion;
    }


}
