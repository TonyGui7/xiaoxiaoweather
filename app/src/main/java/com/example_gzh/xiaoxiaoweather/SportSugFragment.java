package com.example_gzh.xiaoxiaoweather;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class SportSugFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView briefTextView;

    private TextView cityTextView;

    private TextView tempTextView;

    private TextView detailTextView;

    private WebView webView;

    private LifeSuggestionActivity activity;





    // TODO: Rename and change types of parameters

    public SportSugFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=(LifeSuggestionActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_sport_sug,container,false);


        briefTextView=(TextView) v.findViewById(R.id.LifeSugActi_sportBriefTextView);
        String brief=activity.getBrief();
        if (brief!=null){
            briefTextView.setText(brief);
        }


        cityTextView=(TextView) v.findViewById(R.id.LifeSugActi_sport_cityNameTextView);
        String city=activity.getCity();
        if (city!=null){
            cityTextView.setText(city);
        }


        tempTextView=(TextView) v.findViewById(R.id.LifeSugActi_sport_TempTextView);
        String temp=activity.getTemperature();
        if (temp!=null){
            tempTextView.setText("温度："+temp+"℃");
        }


        detailTextView=(TextView) v.findViewById(R.id.sportDetailSugTextView);
        String detailSug=activity.getDetailSuggestion();
        if (detailSug!=null){
            detailTextView.setText(detailSug);
        }

        webView=(WebView) v.findViewById(R.id.sportWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");



        return v;

        }


}
