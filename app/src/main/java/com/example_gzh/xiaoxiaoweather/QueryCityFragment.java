package com.example_gzh.xiaoxiaoweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example_gzh.xiaoxiaoweather.db.cityItem;
import com.example_gzh.xiaoxiaoweather.util.Utility;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;



public class QueryCityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final String EXTRA_SEARCH_RESULT_CITY_ID = "com.example_gzh.xiaoxiaoweather.search_result_cityId";




    private ListView listView;

    private SearchResultAdapter adapter;

    private List<cityItem> dataList=new ArrayList<>();

    private cityItem item;

    private EditText userInputEditText;

    private Button searchButton;

    private String cityId;

    private TextView searchResultTextView;

    private RelativeLayout searchLayout;


    private LinearLayout chooseAreaLayout;

    public static TextView titleText;

    private Button backSettingFragmentButton;

    private TextView userInputHintTextView;


    private CharSequence currentInputText;//用户当前输入的字符串




    /*******推荐城市列表*****************/

    private LinearLayout recommendCityLayout;

    private TextView chongqinTextView;

    private TextView beijingTextView;

    private TextView wuhanTextView;

    private TextView shanghaiTextView;

    private TextView guangzhouTextView;

    private TextView shenzhenTextView;

    private TextView hangzhouTextView;

    private TextView nanjingTextView;

    private TextView xianggangTextView;

    private TextView chengduTextView;

    private TextView changshaTextView;

    private TextView guilinTextView;


    private TextView recommendCityTextView;

    /**********************/




    public QueryCityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_query_city,parent,false);

        //searchLayout=(RelativeLayout) v.findViewById(R.id.search_layout);



        /*初始化推荐城市组件*****/
        recommendCityLayout=(LinearLayout) v.findViewById(R.id.recommend_city_layout);

        recommendCityTextView=(TextView)v.findViewById(R.id.recommendCity_TextView);

        recommendCityLayout.setVisibility(View.VISIBLE);
        recommendCityTextView.setVisibility(View.VISIBLE);

        chongqinTextView=(TextView)v.findViewById(R.id.chongqin_textView);

        beijingTextView=(TextView)v.findViewById(R.id.beijing_textView);

        wuhanTextView=(TextView)v.findViewById(R.id.wuhan_textView);

        shanghaiTextView=(TextView)v.findViewById(R.id.shanghai_textView);

        guangzhouTextView=(TextView)v.findViewById(R.id.guangzhou_textView);

        shenzhenTextView=(TextView)v.findViewById(R.id.shenzhen_textView);

        hangzhouTextView=(TextView)v.findViewById(R.id.hangzhou_textView);

        nanjingTextView=(TextView)v.findViewById(R.id.nanjing_textView);

        xianggangTextView=(TextView)v.findViewById(R.id.xianggang_textView);

        chengduTextView=(TextView)v.findViewById(R.id.chengdu_textView);

        changshaTextView=(TextView)v.findViewById(R.id.changsha_textView);

        guilinTextView=(TextView)v.findViewById(R.id.guilin_textView);

        /****************/

        backSettingFragmentButton=(Button) v.findViewById(R.id.backSettingFragmentButton);
        backSettingFragmentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().finish();
            }
        });

        titleText=(TextView) v.findViewById(R.id.query_title_TextView);
        titleText.setText("潇潇天气");

        userInputHintTextView=(TextView) v.findViewById(R.id.userInputHintTextView);


        listView=(ListView) v.findViewById(R.id.query_list_view);

        adapter=new SearchResultAdapter(getActivity(),R.layout.serach_city_result_list_item,dataList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                    long id) {




                  cityId = dataList.get(position).getCityId();
                  Intent i = new Intent();
                  i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                  getActivity().setResult(Activity.RESULT_OK, i);
                  SettingFragment.addCityClick = true;
                  getActivity().finish();

            }
        });

        userInputEditText=(EditText) v.findViewById(R.id.userInputEditText);
        userInputEditText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                //userInputEditText.setText(" ");

                recommendCityLayout.setVisibility(View.VISIBLE);
                recommendCityTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                currentInputText=s;
              //  userInputHintTextView.setText("当前输入的字符的个数: "+currentInputText.length());


             dataList.clear();
                userInputHintTextView.setText("");
                if (currentInputText.length()>0) {
                    ArrayList<cityItem> tempList=new ArrayList<cityItem>();

                    tempList = Utility.matchItem(getActivity(), currentInputText);

                    if (tempList.size()>0) {
                       recommendCityLayout.setVisibility(View.INVISIBLE);
                       recommendCityTextView.setVisibility(View.INVISIBLE);
                        userInputHintTextView.setText("找到" + tempList.size() + "个结果");
                    }
                    else {
                        recommendCityLayout.setVisibility(View.VISIBLE);
                        recommendCityTextView.setVisibility(View.VISIBLE);
                        userInputHintTextView.setText("未找到市县，请输入正确的市县名称");
                    }

                   for (int iloop=0;iloop<tempList.size();iloop++){
                       dataList.add(tempList.get(iloop));
                   }
                    adapter.notifyDataSetChanged();
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub





/*/
                if (currentInputText.length() > 0) {

                    String CityName = userInputEditText.getText().toString();

                    if (CityName != "") {//输入非空字符

                        try {
                            item = Utility.readFromLocalContent(getActivity(), CityName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (item != null) {
                            dataList.add(item);
                            adapter.notifyDataSetChanged();
                        }

                    } else {//输入的是空字符或者还未输入就点击按钮
                        Toast.makeText(getActivity(), "请输入城市名字", Toast.LENGTH_SHORT).show();
                    }

                }
                /*/
            }


        });




        searchButton=(Button) v.findViewById(R.id.search_button);

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

                if (cityId!=null) {



                        Intent i = new Intent();
                        i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                        getActivity().setResult(Activity.RESULT_OK, i);
                        SettingFragment.addCityClick = true;
                        getActivity().finish();

                }

            }
        });




        chongqinTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "重庆");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });


        beijingTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "北京");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });


        wuhanTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "武汉");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });


        shanghaiTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "上海");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });



        guangzhouTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "广州");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


        shenzhenTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "深圳");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });



        hangzhouTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "杭州");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


        nanjingTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "南京");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


        xianggangTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "香港");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


        chengduTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "成都");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }
            }

        });



        changshaTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "长沙");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


        guilinTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    cityItem item=Utility.readFromLocalContent(getActivity(), "桂林");
                    cityId =item.getCityId();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (cityId!=null) {



                    Intent i = new Intent();
                    i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID, cityId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    SettingFragment.addCityClick = true;
                    getActivity().finish();

                }

            }

        });


     //   Toast.makeText(getActivity(),"北京的ID:"+Utility.matchItem(getActivity(),"北京").get(0).getCityId(),Toast.LENGTH_LONG).show();


        return v;
    }










    public class SearchResultAdapter extends ArrayAdapter<cityItem> {

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








    @Override
    public void onDestroy(){
        super.onDestroy();
        SettingFragment.addCityClick=false;
    }


}
