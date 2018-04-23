package com.example.a20134833.interc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.StrictMode;
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

import com.google.android.gms.common.internal.service.Common;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import android.content.res.Resources;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Navigation extends AppCompatActivity{

    public static double set_lat;
    public static double set_lon;
    TextView setLocationTxt;
    TextView location_Select;
    public static TMapView tMapView;

    private Context 	mContext;
    Bitmap bitmap;

    String[] set_location_name = {"본관4층","자연과학관 1층","법과대학 1층","사회과학 사범대학 1층","경상대학 1층","공과대학 1공학관 2층", "IT융합대학 1층", "사회과학 사범대학 3층","본관 4층", "체육대학 3층","의과대학 1호관 2층","치과대학 2층","약학대학 3호관 1층","미술대학 3층","본관 2층","서석홀 3층","서석홀 1층"};
    double[][] set_location =
            {{35.142708, 126.934776}    // 인문과학대학1
            ,{35.139393, 126.928306}    // 자연과학대학2
            ,{35.139356, 126.935120}    // 법과대학3
            ,{35.145877, 126.934367}    // 사회과학대학4
            ,{35.139767, 126.935598}    // 경상대학5
            ,{35.141791, 126.925571}    // 공과대학6
            ,{35.139803, 126.934196}    // IT융합대학7
            ,{35.145877, 126.934367}    // 사범대학8
            ,{35.142708, 126.934776}    // 외국어대학9
            ,{35.140382, 126.927477}    // 체육대학10
            ,{35.140419, 126.929597}    // 의과대학11
            ,{35.144447, 126.926769}    // 치과대학12
            ,{35.141295, 126.926387}    // 약학대학13
            ,{35.144076, 126.930453}    // 미술대학14
            ,{35.142708, 126.934776}    // 기초교육대학15
            ,{35.145059, 126.932534}    // 보건과학대학16
            ,{35.145059, 126.932534}    // 미래사회융합대학17
            };
    public int set_location_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mContext = this;
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(getApplicationContext());

        //tMapView.setSKTMapApiKey( "38c7269d-5eb5-4739-b305-9886986b658f" );
        tMapView.setSKTMapApiKey("fadb9bf5-6142-43d6-8b0b-2dc5b86fff4d");
        tMapView.setZoomLevel(15);

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

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.poi_here);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        tMapView.setCenterPoint( 126.931933, 35.140982);
    }

    public void setDate(final int position) {
        set_location_num = position;
        Log.e("setDate", String.valueOf(set_location_num));
        set_lat = set_location[position][0];
        set_lon = set_location[position][1];
        Log.e("setDate", set_lat + "/" + set_lon);
        final TMapMarkerItem markerItem = new TMapMarkerItem();
        // 마커 아이콘

        markerItem.setIcon(bitmap); // 마커 아이콘 지정
        TMapPoint tMapPoint = new TMapPoint(set_location[position][1], set_location[position][0]);
        markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem.setTMapPoint(tMapPoint); // 마커의 좌표 지정
        markerItem.setName(set_location_name[position]); // 마커의 타이틀 지정

        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(18);
        tMapView.setCenterPoint(set_lon, set_lat);
        tMapView.addMarkerItem("markerItem", markerItem); // 지도에 마커 추가
    }

    public void StartGuidance() {
        tMapView.removeTMapPath();

        setTrackingMode();
        Log.e("Navi", set_lat +"/"+set_lon);

        TMapPoint point1 = new TMapPoint(35.141783, 126.928387);
        //TMapPoint point2 = new TMapPoint(set_location[set_location_num][0], set_location[set_location_num][1]);
        TMapPoint point2 = new TMapPoint(set_lat, set_lon);
        Log.e("Navi", String.valueOf(set_location_num));
        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                tMapView.addTMapPath(polyLine);
            }
        });

        Bitmap start = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.poi_start);
        Bitmap end = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.poi_end);
        tMapView.setTMapPathIcon(start, end);

        tMapView.zoomToTMapPoint(point1, point2);
    }

    public void setTrackingMode() {
        Log.e("Navi", "searchTracking");
        tMapView.setTrackingMode(tMapView.getIsTracking());
    }

    public void StartGuidance(View view) {
        StartGuidance();
    }
}
