package com.example_gzh.xiaoxiaoweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example_gzh.xiaoxiaoweather.db.City;
import com.example_gzh.xiaoxiaoweather.db.County;
import com.example_gzh.xiaoxiaoweather.db.Province;
import com.example_gzh.xiaoxiaoweather.util.HttpUtil;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
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

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList=new ArrayList<>();



    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;


    private Province selectedProvince;


    private City selectedCity;

    private int currentLevel;

    private  String responseResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.choose_area,parent,false);

        titleText=(TextView) v.findViewById(R.id.title_text);

        backButton=(Button) v.findViewById(R.id.back_Button);

        listView=(ListView) v.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);

        listView.setAdapter(adapter);

        return v;
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        LitePal.getDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                    long id){

                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if (currentLevel==LEVEL_COUNTY){
                    String weatherId=countyList.get(position).getWeatherId();
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
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

    }








    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再到服务器上查询
     * */
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);

        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String address="http://guolin.tech/api/china";
                    queryFromServer(address,"province");
                }
            }).start();

        }




    }






    private void queryCities(){

        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);

        if(cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int provinceCode=selectedProvince.getProvinceCode();
                    String address="http://guolin.tech/api/china/"+provinceCode;
                    queryFromServer(address,"city");
                }
            }).start();


        }

    }




    private void queryCounties(){

        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getCityId())).find(County.class);

        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int provinceCode=selectedProvince.getProvinceCode();
                    int cityCode=selectedCity.getCityCode();
                    String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
                    queryFromServer(address,"county");
                }
            }).start();

        }
    }




    /**
     * 从服务器获取数据
     * */
    private void queryFromServer(String address,final String type) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                  showProgressDialog();
            }
        });

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(address)
                    .build();

            // http://10.0.2.2/getdata.json
            Response response = client.newCall(request).execute();

            String responseData = response.body().string();
            boolean result = false;

            if ("province".equals(type)) {
                result = Utility.handleProvinceResponse(responseData);
            } else if ("city".equals(type)) {
                result = Utility.handleCityResponse(responseData, selectedProvince.getId());

            } else if ("county".equals(type)) {
                result = Utility.handleCountyResponse(responseData, selectedCity.getCityId());
            }

            if (result) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "GET_RESULT", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                        if ("province".equals(type)) {
                            queryProvinces();
                        } else if ("city".equals(type)) {
                            queryCities();
                        } else if ("county".equals(type)) {
                            queryCounties();
                        }
                    }
                });
            }


            //  parseXMLWithPull(responseData);
            // showToast(responseData);
            //   parseJSONWithJSONObject(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //   <用封装的网络请求接口不能正常工作>
        /*   HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().toString();


                responseResult=responseText;

                boolean result=false;//记录解析从服务器返回数据是否成功


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  showProgressDialog();
                        Toast.makeText(getActivity(),"Response:"+responseResult,Toast.LENGTH_LONG).show();

                    }
                });



                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());

                }else if ("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getCityId());
                }

                if (result){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"GET_RESULT",Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

                Toast.makeText(getActivity(),"GET_RESULT_fAILED",Toast.LENGTH_SHORT).show();





            }
        });



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


}
