package com.example_gzh.xiaoxiaoweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;

public class LauncherActivity extends AppCompatActivity {


    private LinearLayout LaunchLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>21){
            View decoreView=getWindow().getDecorView();
            decoreView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_launcher);


        LaunchLayout=(LinearLayout) findViewById(R.id.LaunchLayout);


        /*/
        String launchUIName = "launchback_ground";
        Random random=new Random();
        int max=7;
        int min=1;
        int randomNumber=random.nextInt(max)%(max-min+1)+min;

        if (randomNumber>=1&&randomNumber<=7){
            launchUIName=launchUIName+randomNumber;
        }



        // String weatherLogoName="test";
        int LaunchInterfaceId = getResources().getIdentifier(launchUIName, "drawable", "com.example_gzh.xiaoxiaoweather");

       LaunchLayout.setBackgroundResource(LaunchInterfaceId);
  /*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);


                    SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this);



                    if (prefs.getString("weather", null) != null) {


                        Intent i = new Intent(LauncherActivity.this, WeatherActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(LauncherActivity.this, MainActivity.class);

                        startActivity(i);
                        finish();
                    }
                }catch (Exception e){

                }
            }
        }).start();




    }
}
