package com.example.a20134833.interc;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Navigation extends AppCompatActivity{

    private double set_lat;
    private double set_lon;
    TextView setLocationTxt;
    TextView location_Select;
    static TMapView tMapView;

    Spinner spinner;
    String[] item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);

        //tMapView.setSKTMapApiKey( "38c7269d-5eb5-4739-b305-9886986b658f" );
        tMapView.setSKTMapApiKey("fadb9bf5-6142-43d6-8b0b-2dc5b86fff4d");
        tMapView.setZoomLevel(20);
        linearLayoutTmap.addView(tMapView);

        setLocationTxt = (TextView) findViewById(R.id.setLocationTxt);
        location_Select = (TextView)findViewById(R.id.location_Select);

        location_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                Navi_ListDialog customDialog = new Navi_ListDialog(Navigation.this);

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction(location_Select);

            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /*
        //------------Spinner 붙이기
        spinner = (Spinner) findViewById(R.id.spinner);

        item = new String[]{"목적지를 선택하세요","인문과학대학", "자연과학대학","법학대학","사회과학대학","경상대학","공과대학","IT융합대학","사범대학","외국어대학","체육대학","의과대학","치과대학","약학대학","미술대학","기초교육대학","보건과학대학","미래사회융합대학"};

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        */


    }

    public void setDate(int position) {
        Log.e("setDate", String.valueOf(position));
    String location = null;
        switch (position)   {
            case 0:
                break;
            case 1: // 인문과학대학
                location ="본관 4층";
                break;
            case 2: // 자연과학대학
                location ="자연과학관 1층";
                set_lat = 35.139398;
                set_lon = 126.928243;
                break;
            case 3: // 법과대학
                location ="법과대학 1층";
                set_lat = 35.139337;
                set_lon = 126.935098;
                break;
            case 4: // 사회과학대학
                location ="사회과학 사범대학 1층";
                set_lat = 35.145934;
                set_lon = 126.934272;
                break;
            case 5: // 경상대학
                location ="경상대학 1층";
                break;
            case 6: // 공과대학
                location ="공과대학 1공학관 2층";
                break;
            case 7: // IT융합대학
                location ="IT융합대학 1층";
                break;
            case 8: // 사범대학
                location ="사회과학 사범대학 3층";
                break;
            case 9: // 외국어대학
                location ="본관 4층";
                break;
            case 10: // 체육대학
                location ="체육대학 3층";
                break;
            case 11: // 의과대학
                location ="의과대학 1호관 2층";
                break;
            case 12: // 치과대학
                location ="치과대학 2층";
                break;
            case 13: // 약학대학
                location ="약학대학 3호관 1층";
                break;
            case 14: // 미술대학
                location ="미술대학 3층";
                break;
            case 15: // 기초교육대학
                location ="본관 2층";
                break;
            case 16: // 보건과학대학
                location ="서석홀 3층";
                break;
            case 17: // 미래사회융합대학
                location ="서석홀 1층";
                break;
            case 18:
                location ="[위치를 선택해 주세요]";
                break;
        }
        //setLocationTxt.setText(location);
        tMapView.setCenterPoint(set_lon, set_lat);

    }
}
