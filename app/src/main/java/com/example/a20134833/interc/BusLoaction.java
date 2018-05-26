package com.example.a20134833.interc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusLoaction extends FragmentActivity implements OnMapReadyCallback {

    public TextView BusTimeText;
    private int Sel_Bus_num = 0;

    private GoogleMap mMap;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_loaction);

        Sel_Bus_num = 0;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // 구글맵
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final TextView Bus_Selected = (TextView)findViewById(R.id.Bus_Selected);
        final Spinner Bus_Spinner = (Spinner) findViewById(R.id.Bus_spinner);
        final Button BusSearch = (Button) findViewById(R.id.Bus_Search);
        BusTimeText = (TextView) findViewById(R.id.BusTime);

        String[] str = getResources().getStringArray(R.array.BusList);
        ArrayAdapter<String > adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str);
        Spinner spi = (Spinner)findViewById(R.id.Bus_spinner);
        spi.setAdapter(adapter);
        spi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Sel_Bus_num = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        // search 버튼
        BusSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BusSearch.getText().equals("START")) {
                    if(Sel_Bus_num == 0)    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Log.e("adapter" ,"RUN");
                                        AlertDialog.Builder alert = new AlertDialog.Builder(BusLoaction.this);
                                        alert.setTitle("Error");
                                        alert.setMessage("버스를 선택해 주세요");
                                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        alert.show();
                                    }
                                });
                            }
                        }).start();

                    }   else    {
                        Bus_Selected.setText(Sel_Bus_num + "번 버스");
                        Bus_Selected.setVisibility(View.VISIBLE);
                        Bus_Spinner.setVisibility(View.GONE);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // To do
                                Bus_Location_receive_Asycn(Sel_Bus_num);
                                }
                        }, 0, 1000);
                        BusSearch.setText("Searching...");
                    }
                }else{
                    Bus_Selected.setVisibility(View.GONE);
                    Bus_Spinner.setVisibility(View.VISIBLE);
                    BusTimeText.setText("BUS TIME");
                    timer.cancel();
                    timer = null;
                    BusSearch.setText("START");
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Gpsinit = new LatLng(35.142023, 126.927910);
        mMap.addMarker(new MarkerOptions().position(Gpsinit).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Gpsinit));
    }

    public final String url = "http://117.16.23.140/bus/B_DB_Select.php";

    OkHttpClient client = new OkHttpClient();

    String responseBody = null;

    public void Bus_Location_receive_Asycn(final int id) {
        (new AsyncTask<BusLoaction, Void, String>() {
            @Override
            protected String doInBackground(BusLoaction... mainActivities) {
                Log.e("Do In Back", "Start");
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, id);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
            }
        }).execute();

        return;
    }

    public class ConnectServer {//Client 생성

        public int requestPost(String url, int id) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("id", String.valueOf(id)).build();


            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        responseBody = response.body().string();
                        String responseResult1 = responseBody.replaceAll("\\[", "");
                        responseBody = responseResult1.replaceAll("\\]", "");
                        JSONObject jsonObject = new JSONObject(responseBody);

                        Log.e("Response == ", String.valueOf(jsonObject));

                        final String time = jsonObject.getString("time");
                        final Double lat = jsonObject.getDouble("latitude");
                        final Double lon = jsonObject.getDouble("longitude");

                        Log.e("Response string", "time = " + time + "/ lat = " + lat + "/ lon = " + lon);

                        (BusLoaction.this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("ShowLocation", "Connect!!!");
                                BusTimeText.setText(time);

                                mMap.clear();
                                LatLng NowLocation = new LatLng(lat, lon);
                                mMap.addMarker(new MarkerOptions().position(NowLocation));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(NowLocation));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                            }
                        });
/*
                        new Thread() {
                            public void run() {
                                Message msg = mhandler.obtainMessage();
                                mhandler.sendMessage(msg);
                            }
                        }.start();
*/
//                        ShownowLocation(lat, lon);

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        // 로그인이 틀렸을 때,
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            timer.cancel();
            timer = null;
        }catch (NullPointerException e) {}

        super.onBackPressed();
    }
}
