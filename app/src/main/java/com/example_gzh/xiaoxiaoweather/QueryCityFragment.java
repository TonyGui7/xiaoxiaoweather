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



        titleText=(TextView) v.findViewById(R.id.query_title_TextView);
        titleText.setText("潇潇天气");

        listView=(ListView) v.findViewById(R.id.query_list_view);

        adapter=new SearchResultAdapter(getActivity(),R.layout.serach_city_result_list_item,dataList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,
                                    long id) {

                cityId=dataList.get(position).getCityId();
                Intent i=new Intent();
                i.putExtra(EXTRA_SEARCH_RESULT_CITY_ID,cityId);
                getActivity().setResult(Activity.RESULT_OK,i);

            getActivity().finish();
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

                    getActivity().finish();
                }

            }
        });




        try {
            cityItem item = Utility.readFromLocalContent(getActivity(),"武汉");
            cityId=item.getCityId();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        Toast.makeText(getActivity(), "怀柔的Id: "+cityId, Toast.LENGTH_LONG).show();
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



}
