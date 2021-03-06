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


public class TravelSugFragment extends Fragment {

    private TextView briefTextView;

    private TextView cityTextView;

    private TextView tempTextView;

    private TextView detailTextView;

    private WebView webView;

    private LifeSuggestionActivity activity;



    public TravelSugFragment() {
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
        View v=inflater.inflate(R.layout.fragment_travel_sug,container,false);


        briefTextView=(TextView) v.findViewById(R.id.LifeSugActi_travelBriefTextView);
        String brief=activity.getBrief();
        if (brief!=null){
            briefTextView.setText(brief);
        }


        cityTextView=(TextView) v.findViewById(R.id.LifeSugActi_travel_cityNameTextView);
        String city=activity.getCity();
        if (city!=null){
            cityTextView.setText(city);
        }


        tempTextView=(TextView) v.findViewById(R.id.LifeSugActi_travel_TempTextView);
        String temp=activity.getTemperature();
        if (temp!=null){
            tempTextView.setText("温度："+temp+"℃");
        }


        detailTextView=(TextView) v.findViewById(R.id.travelDetailSugTextView);
        String detailSug=activity.getDetailSuggestion();
        if (detailSug!=null){
            detailTextView.setText(detailSug);
        }

        webView=(WebView) v.findViewById(R.id.travelWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.lonelyplanet.com/");




        return v;

    }

}
