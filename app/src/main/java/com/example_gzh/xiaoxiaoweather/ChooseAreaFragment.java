package com.example_gzh.xiaoxiaoweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.component_obj.City_item;
import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.db.Province;
import com.example_gzh.xiaoxiaoweather.db.cityItem;
import com.example_gzh.xiaoxiaoweather.util.HttpUtil;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tony Gui on 2017/6/11.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE=0;

    public static final int LEVEL_CITY=1;

    public static final int LEVEL_COUNTY=2;


    private ProgressDialog progressDialog;

    public static TextView titleText;

    private Button backButton;

    private ListView listView;

    private SearchResultAdapter adapter;

    private List<cityItem> dataList=new ArrayList<>();



    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;


    private Province selectedProvince;


    private City selectedCity;

    private int currentLevel;

    private  String responseResult;



    private cityItem item;

    private EditText userInputEditText;

    private Button searchButton;

    private String cityId;

    private TextView searchResultTextView;

    private RelativeLayout searchLayout;


    private LinearLayout chooseAreaLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.choose_area,parent,false);

        //searchLayout=(RelativeLayout) v.findViewById(R.id.search_layout);

        chooseAreaLayout=(LinearLayout) v.findViewById(R.id.choose_area_fragment_layout);


        titleText=(TextView) v.findViewById(R.id.title_TextView);

        listView=(ListView) v.findViewById(R.id.list_view);

        adapter=new SearchResultAdapter(getActivity(),R.layout.serach_city_result_list_item,dataList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                    long id) {

                        cityId=dataList.get(position).getCityId();
                if (getActivity() instanceof MainActivity) { //刚启动程序
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", cityId);
                    startActivity(intent);
                    chooseAreaLayout.setVisibility(View.GONE);
                    getActivity().finish();
                } else if (getActivity() instanceof WeatherActivity) {//以获取天气信息，现在切换城市
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.swipeRefresh.setRefreshing(true);
                    activity.requestWeather(cityId);

                }


                        }
            });

        userInputEditText=(EditText) v.findViewById(R.id.userInputEditText);
        userInputEditText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String CityName=userInputEditText.getText().toString();

                try {
                    item = Utility.readFromLocalContent(getActivity(), CityName);
                }catch (Exception e){
                    e.printStackTrace();
                }


                if (item!=null){
                        dataList.add(item);
                    adapter.notifyDataSetChanged();
                }
            }

        });




        searchButton=(Button) v.findViewById(R.id.search_button);
        /*/
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String cityName=userInputEditText.getText().toString();
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), cityName);
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (cityId!=null){

                    if (getActivity() instanceof MainActivity) { //刚启动程序
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", cityId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {//以获取天气信息，现在切换城市
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(cityId);

                    }

                }

            }
        });

/*/


        try {
            cityItem item = Utility.readFromLocalContent(getActivity(),"武汉");
            cityId=item.getCityId();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        Toast.makeText(getActivity(), "怀柔的Id: "+cityId, Toast.LENGTH_LONG).show();
        return v;
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        LitePal.getDatabase();
/*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                    long id){

                if (HttpUtil.isNetworkConnected(getActivity())) {
                    if (currentLevel == LEVEL_PROVINCE) {
                        selectedProvince = provinceList.get(position);
                        queryCities();
                    } else if (currentLevel == LEVEL_CITY) {
                        selectedCity = cityList.get(position);
                        queryCounties();
                    } else if (currentLevel == LEVEL_COUNTY) {
                        String weatherId = countyList.get(position).getWeatherId();
                        if (getActivity() instanceof MainActivity) { //刚启动程序
                            Intent intent = new Intent(getActivity(), WeatherActivity.class);
                            intent.putExtra("weather_id", weatherId);
                            startActivity(intent);
                            getActivity().finish();
                        } else if (getActivity() instanceof WeatherActivity) {//以获取天气信息，现在切换城市
                            WeatherActivity activity = (WeatherActivity) getActivity();
                            activity.drawerLayout.closeDrawers();
                            activity.swipeRefresh.setRefreshing(true);
                            activity.requestWeather(weatherId);

                        }


                    }
                }
                else {
                    Toast.makeText(getActivity(),"无网络连接",Toast.LENGTH_SHORT).show();
                }

            }

        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });



        queryProvinces();

        /*/

    }






    /**显示进度对话框*/
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }


        progressDialog.show();
    }

    /**关闭进度对话框*/
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }





    public class SearchResultAdapter extends ArrayAdapter<cityItem>{

        private int layoutId;

        public  SearchResultAdapter(Context context, int resourceId, List<cityItem> list){
            super(context,resourceId,list);

            layoutId=resourceId;


        }


        @Override
        public View getView(int position,View convertView,ViewGroup parent){

            cityItem item=getItem(position);
            //View v=LayoutInflater.from(getContext()).inflate(layoutId,parent,false);

            View v= LayoutInflater.from(getActivity()).inflate(layoutId,parent,false);


            TextView resultTextView=(TextView) v.findViewById(R.id.search_result_list_TextView);

            TextView cityTextView=(TextView) v.findViewById(R.id.search_result_city_TextView);

            cityTextView.setText(item.getCityChineseName());

            String result="，"+item.getCountryChineseName()+"，"+item.getProvinceChineseName();
            resultTextView.setText(result);
            return v;
        }






    }




}
