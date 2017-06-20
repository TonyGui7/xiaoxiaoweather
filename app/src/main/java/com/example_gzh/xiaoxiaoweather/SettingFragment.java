package com.example_gzh.xiaoxiaoweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tony Gui on 2017/6/18.
 */

public class SettingFragment extends Fragment {


    private  ListView addCityListView;

    private Button addCityButton;




    public static final String ADDED_CITY_INFO_PREF="addedCityInfo";

    /**被添加的城市天气列表子控件*/
    private TextView addedCity;

    private TextView nowTempTextView;

    private TextView nowWeatherInfoTextView;



    public static List<City_item> cityItemList=new ArrayList<>() ;

    public static   CityAdapter adapter;

    /**标记是否点击添加城市按钮*/
    public static boolean addCityClick=false;


    private WeatherActivity activity;

    private Weather mweather;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        adapter=new CityAdapter(getActivity(),R.layout.city_item,cityItemList);





    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){


        View v=inflater.inflate(R.layout.setting_fragment,parent,false);



        /**
         * 响应添加城市按钮点击事件
         * */
        addCityButton=(Button) v.findViewById(R.id.add_city_Button);
        addCityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              addCityClick=true;
                WeatherActivity.drawerLayout.closeDrawers();
                WeatherActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });





        addCityListView=(ListView) v.findViewById(R.id.add_city_listView);

        adapter.notifyDataSetChanged();

        addCityListView.setAdapter(adapter);
        addCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                            long id){

               City_item item=cityItemList.get(position);
               SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
               String weatherString=prefs.getString(ADDED_CITY_INFO_PREF+item.getCityName()," ");

               if (weatherString!=null){
                   mweather= Utility.handleWeatherResponse(weatherString);
                   WeatherActivity.drawerLayout.closeDrawers();
                   activity = (WeatherActivity) getActivity();

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try{
                               Thread.sleep(1000);
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                           getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   activity.showWeatherInfo(mweather);
                               }
                           });

                       }
                   }).start();



               }
            }
        });


        return v;

    }








    public class CityAdapter extends ArrayAdapter<City_item>{

        private int layoutId;

        public  CityAdapter(Context context, int resourceId, List<City_item> list){
            super(context,resourceId,list);

            layoutId=resourceId;


        }


        @Override
        public View getView(int position,View convertView,ViewGroup parent){

            City_item city_item=getItem(position);
            View v=LayoutInflater.from(getContext()).inflate(layoutId,parent,false);

            addedCity=(TextView) v.findViewById(R.id.added_city_name);
            nowTempTextView=(TextView) v.findViewById(R.id.now_Temp_textView);
            nowWeatherInfoTextView=(TextView) v.findViewById(R.id.now_weather_Info_textView);


            addedCity.setText(city_item.getCityName());
            nowTempTextView.setText(city_item.getCurrentTemperature());
            nowWeatherInfoTextView.setText(city_item.getWeatherInfo());



            return v;
        }






    }








}
