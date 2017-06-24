package com.example_gzh.xiaoxiaoweather;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.component_obj.addedCitesListLab;
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

    private Button manageCitybackButton;



    public static final String ADDED_CITY_INFO_PREF="addedCityInfo";

    public static final String ADDED_CITY_LIST_SIZE="addedCityCount";

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




    }






    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent,
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








        adapter=new CityAdapter(getActivity(),R.layout.city_item,cityItemList);



        addCityListView=(ListView) v.findViewById(R.id.add_city_listView);

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
                   WeatherActivity.drawerLayout.closeDrawers();
                   activity = (WeatherActivity) getActivity();

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


}
