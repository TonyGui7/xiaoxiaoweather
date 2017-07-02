package com.example_gzh.xiaoxiaoweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.component_obj.addedCitesListLab;
import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.gson.Weather;
import com.example_gzh.xiaoxiaoweather.util.HttpUtil;
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

    private Button manageCitybackButton;

     public static LinearLayout settingFragmentLayout;

    public static final String ADDED_CITY_INFO_PREF="addedCityInfo";

    public static final String ADDED_CITY_LIST_SIZE="addedCityCount";




    public static final int  REQUEST_SEARCH_RESULT_CITY_ID=0;

    /**被添加的城市天气列表子控件*/
    private TextView addedCity;

    private TextView nowTempTextView;

    private TextView nowWeatherInfoTextView;



    private  List<City_item> cityItemList;

    public static   CityAdapter adapter;

    /**标记是否点击添加城市按钮*/
    public static boolean addCityClick=false;



    private WeatherActivity activity;

    private Weather mweather;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        cityItemList=addedCitesListLab.get(getActivity()).getCityLists();


        activity = (WeatherActivity) getActivity();

    }






    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){


        View v=inflater.inflate(R.layout.setting_fragment,parent,false);

        settingFragmentLayout=(LinearLayout) v.findViewById(R.id.settingFragmentLayout);



        /**
         * 响应添加城市按钮点击事件
         * */
        addCityButton=(Button) v.findViewById(R.id.add_city_Button);
        addCityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              //  addCityClick=true;
              //  WeatherActivity.drawerLayout.closeDrawers();

                Intent i=new Intent(getActivity(),QueryCityActivity.class);
                startActivityForResult(i,REQUEST_SEARCH_RESULT_CITY_ID);

              //WeatherActivity.drawerLayout.openDrawer(GravityCompat.START);
            }
        });








        adapter=new CityAdapter(getActivity(),R.layout.city_item,cityItemList);



        addCityListView=(ListView) v.findViewById(R.id.add_city_listView);

        registerForContextMenu(addCityListView);


        /*
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(addCityListView);
        }else {
            addCityListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            addCityListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater1=mode.getMenuInflater();
                    inflater1.inflate(R.menu.added_city_list_item_context,menu);

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){

                        case R.id.menu_item_delete_city:
                            addedCitesListLab lab=addedCitesListLab.get(getActivity());
                            for (int iloop=adapter.getCount()-1;iloop>=0;iloop--){
                                if (addCityListView.isItemChecked(iloop)){
                                    lab.deleteItem(adapter.getItem(iloop));
                                }
                            }

                            mode.finish();
                            adapter.notifyDataSetChanged();
                            lab.saveCityLists();
                            return true;
                        default:
                            return false;
                    }



                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });


        }
/*/

        if (WeatherActivity.hasRequestWeather){
            updateSettingFragmentBackground();
        }

        adapter.notifyDataSetChanged();

        addCityListView.setAdapter(adapter);
        addCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                            long id){

               City_item item=cityItemList.get(position);

               String weatherString=item.getAddedCityWeather();




               if (weatherString!=null){
                   mweather= Utility.handleWeatherResponse(weatherString);
                   activity.setCurrentDisplayWeather(mweather);
                   WeatherActivity.drawerLayout.closeDrawers();


                   activity.switchCityProgressBar.setVisibility(View.VISIBLE);
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
                                    activity.setWeatherId(mweather.basic.weatherId);
                                   activity.showWeatherInfo(mweather);
                                   activity.switchCityProgressBar.setVisibility(View.GONE);
                                   activity.setCurrentDisplayWeather(mweather);
                                   updateSettingFragmentBackground();
                               }
                           });

                       }
                   }).start();



               }
            }
        });



        manageCitybackButton=(Button) v.findViewById(R.id.manageCity_back_Button);
        manageCitybackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherActivity.drawerLayout.closeDrawers();
            }
        });



        updateSettingFragmentBackground();




        return v;

    }





  /**
   *更新管理城市列表的背景
   * */
    private void updateSettingFragmentBackground(){

        Weather displayWeather=activity.getCurrentDisplayWeather();

        String prefix="weather_background";
        String CurrentinfoCode;
        if (displayWeather==null){
            CurrentinfoCode="";
        }else{
            CurrentinfoCode =displayWeather.now.more.infoCode;
        }

       // String CurrentinfoCode=WeatherActivity.currentDisplayWeatherInfoCode;
       // CurrentinfoCode="100";
        int imageId;

      if (CurrentinfoCode.compareTo("100")==0){//天气晴朗

           String suffix="100";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }else if (CurrentinfoCode.compareTo("101")>=0
              &&CurrentinfoCode.compareTo("103")<=0){//天气多云

          String suffix="101_103";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


      }
      else if (CurrentinfoCode.compareTo("104")==0){//阴天
          String suffix="104";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }
      else if (CurrentinfoCode.compareTo("200")>=0
              &&CurrentinfoCode.compareTo("213")<=0){//起风天气
          String suffix="200_213";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }
      else if (CurrentinfoCode.compareTo("300")>=0
              &&CurrentinfoCode.compareTo("313")<=0){//下雨天气

          if (CurrentinfoCode.compareTo("302")>=0
                  &&CurrentinfoCode.compareTo("304")<=0){//下雨同时打雷

              String suffix="302_304";
              String weatherBackgroundImageName=prefix+suffix;
              imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


          }else {


              //只下雨不打雷
              String suffix = "300_313";
              String weatherBackgroundImageName = prefix + suffix;
              imageId = getResources().getIdentifier(weatherBackgroundImageName, "drawable", "com.example_gzh.xiaoxiaoweather");

          }
      }
      else if (CurrentinfoCode.compareTo("400")>=0
              &&CurrentinfoCode.compareTo("407")<=0){//下雪天气
          String suffix="400_407";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }
      else if (CurrentinfoCode.compareTo("500")>=0
              &&CurrentinfoCode.compareTo("501")<=0){//起雾天气
          String suffix="500_501";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


      }
      else if (CurrentinfoCode.compareTo("502")==0){//霾
          String suffix="502";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");


      }
      else if (CurrentinfoCode.compareTo("503")>=0
              &&CurrentinfoCode.compareTo("508")<=0){//扬尘或沙尘暴天气

          String suffix="503_508";
          String weatherBackgroundImageName=prefix+suffix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }
      else{ //其他返回结果

          String suffix="200_213";
          String weatherBackgroundImageName=prefix;
          imageId=getResources().getIdentifier(weatherBackgroundImageName,"drawable","com.example_gzh.xiaoxiaoweather");

      }


        // String weatherLogoName="test";

        settingFragmentLayout.setBackgroundResource(imageId);

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
            nowTempTextView.setText(city_item.getCurrentTemperature()+"℃");
            nowWeatherInfoTextView.setText(city_item.getWeatherInfo());



            return v;
        }






    }






    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        getActivity().getMenuInflater().inflate(R.menu.added_city_list_item_context,menu);
    }





    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
     int position=info.position;
        City_item cityItem=adapter.getItem(position);


        switch (item.getItemId()){
            case R.id.menu_item_delete_city:
                addedCitesListLab.get(getActivity()).deleteItem(cityItem);
                addedCitesListLab.get(getActivity()).saveCityLists();
                adapter.notifyDataSetChanged();
                return true;
        }


        return super.onContextItemSelected(item);
    }






    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){

        if (resultCode!= Activity.RESULT_OK){
            return;
        }



        if (requestCode==REQUEST_SEARCH_RESULT_CITY_ID){
            if (HttpUtil.isNetworkConnected(getActivity())) {
                String weatherId = (String) data.getSerializableExtra(QueryCityFragment.EXTRA_SEARCH_RESULT_CITY_ID);
                WeatherActivity activity = (WeatherActivity) getActivity();
             //   Toast.makeText(activity, "北京的ID:"+weatherId, Toast.LENGTH_LONG).show();

                WeatherActivity.drawerLayout.closeDrawers();
                activity.swipeRefresh.setRefreshing(true);
                activity.requestWeather(weatherId);

            }
            else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_LONG).show();
                    }
                });
                activity.swipeRefresh.setRefreshing(false);
            }
        }



    }


}
