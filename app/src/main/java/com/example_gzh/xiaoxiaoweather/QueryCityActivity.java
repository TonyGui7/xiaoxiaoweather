package com.example_gzh.xiaoxiaoweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QueryCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_city);
    }




    @Override
    public void onDestroy(){
        super.onDestroy();
        SettingFragment.addCityClick=false;
    }
}
