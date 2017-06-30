package com.example_gzh.xiaoxiaoweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tony Gui on 2017/5/31.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle onSavedInstanceState) {
       super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_fragment);


        FragmentManager fm=getSupportFragmentManager();


        Fragment fragment=fm.findFragmentById(R.id.fragmentContainer);
        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }

    }

}
