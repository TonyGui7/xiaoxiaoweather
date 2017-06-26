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

    private ArrayAdapter<String> adapter;

    private List<String> dataList=new ArrayList<>();



    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;


    private Province selectedProvince;


    private City selectedCity;

    private int currentLevel;

    private  String responseResult;



    private cityItem item;

  //  private EditText userInputEditText;

    private Button searchButton;

    private String cityId;

    private TextView searchResultTextView;

    private RelativeLayout searchLayout;




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.choose_area,parent,false);

        //searchLayout=(RelativeLayout) v.findViewById(R.id.search_layout);


        backButton=(Button) v.findViewById(R.id.back_Button);



        titleText=(TextView) v.findViewById(R.id.title_text);

        listView=(ListView) v.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
      /*/
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

/*/


      //  searchButton=(Button) v.findViewById(R.id.search_button);
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




        /*/
        try {
            cityItem item = Utility.readFromLocalContent(getActivity(),"武汉");
            cityId=item.getCityId();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        Toast.makeText(getActivity(), "怀柔的Id: "+cityId, Toast.LENGTH_LONG).show();


      /*/

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



    }




    /**
     150      * 查询全国所有的省，优先从数据库查询，如果没有查询到再到服务器上查询
     151      * */
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
319             @Override
320             public void onFailure(Call call, IOException e) {
321                 getActivity().runOnUiThread(new Runnable() {
322                     @Override
323                     public void run() {
324                         closeProgressDialog();
325                         Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
326                     }
327                 });
328             }
329
330             @Override
331             public void onResponse(Call call, Response response) throws IOException {
332
333                 String responseText=response.body().toString();
334
335
336                 responseResult=responseText;
337
338                 boolean result=false;//记录解析从服务器返回数据是否成功
339
340
341                 getActivity().runOnUiThread(new Runnable() {
342                     @Override
343                     public void run() {
344                         //  showProgressDialog();
345                         Toast.makeText(getActivity(),"Response:"+responseResult,Toast.LENGTH_LONG).show();
346
347                     }
348                 });
349
350
351
352                 if("province".equals(type)){
353                     result= Utility.handleProvinceResponse(responseText);
354                 }else if("city".equals(type)){
355                     result=Utility.handleCityResponse(responseText,selectedProvince.getId());
356
357                 }else if ("county".equals(type)){
358                     result=Utility.handleCountyResponse(responseText,selectedCity.getCityId());
359                 }
360
361                 if (result){
362
363                     getActivity().runOnUiThread(new Runnable() {
364                         @Override
365                         public void run() {
366                             Toast.makeText(getActivity(),"GET_RESULT",Toast.LENGTH_SHORT).show();
367                             closeProgressDialog();
368                             if ("province".equals(type)){
369                                 queryProvinces();
370                             }else if ("city".equals(type)){
371                                 queryCities();
372                             }else if ("county".equals(type)){
373                                 queryCounties();
374                             }
375                         }
376                     });
377                 }
378
379                 Toast.makeText(getActivity(),"GET_RESULT_fAILED",Toast.LENGTH_SHORT).show();
380
381
382
383
384
385             }
386         });
387
388
389
390     }






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




/*/
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

/*/


}
