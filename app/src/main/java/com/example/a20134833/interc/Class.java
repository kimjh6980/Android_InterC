package com.example.a20134833.interc;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Class extends AppCompatActivity {

    EditText id;
    EditText pw;

    TextView[] dtv; // 0~69

    ProgressDialog asyncDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

         id = findViewById(R.id.idT);
         pw = findViewById(R.id.pwT);

         dtv = new TextView[70];

        for(int i=0;  i<70; i++)   {
            int k = getResources().getIdentifier("detailclass"+i, "id", getPackageName());
            dtv[i] = findViewById(k);
        }

        Button classSearch = findViewById(R.id.classSearch);

        classSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class_Asycn(id.getText().toString(), pw.getText().toString());
                Log.e("class = ", id.getText().toString() + "/" + pw.getText().toString());
            }
        });


        asyncDialog = new ProgressDialog(Class.this);


    }

    //--------------------Server Conn
    public final String url = "http://api.udp.cc/libs/query.php";

    OkHttpClient client = new OkHttpClient();

    String responseBody = null;

    public void class_Asycn(final String id, final String pw) {
        (new AsyncTask<Class, Void, String>() {
            @Override
            protected String doInBackground(Class... mainActivities) {
                Log.e("Do In Back", "Start");
                Class.ConnectServer connectServerPost = new Class.ConnectServer();
                connectServerPost.requestPost(url, id, pw);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {

                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("로딩중입니다..");
                asyncDialog.show();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }).execute();

        return;
    }

    public class ConnectServer {//Client 생성

        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        public int requestPost(String url, String id, String pw) {
            //Request Body에 서버에 보낼 데이터 작성
            final String Sbody = "{\"target\":\"TIMETABLE\",\"method\":\"LOAD\",\"perid\":\"" + id + "\",\"password\":\"" + pw + "\",\"custcd\":\"CHOSUN\",\"year\":2018,\"class\":\"10\"}";
            final RequestBody requestBody = RequestBody.create(JSON, Sbody);
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
                        JSONObject jsonObject = new JSONObject(responseBody);
                        Log.e("rsp = ", jsonObject.toString());
                        String msg = jsonObject.getString("message");
                        if(msg.equals("ERROR")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error. Try Again", Toast.LENGTH_LONG).show();
                                }
                            });
                        }   else {

                            final String data = jsonObject.getString("data");
                            //final String Cdata = data.substring(1, data.length()-1);
                            //Log.e("Cdata = ", Cdata);
                            final JSONArray Jdata = new JSONArray(data);

                            for (int i = 0; i < 70; i++) {
                                JSONObject jsonObj = Jdata.getJSONObject(i);
                                final String idx = jsonObj.getString("idx");
                                final String subject = jsonObj.getString("subject");
                                final String curi_num = jsonObj.getString("curi_num");
                                final String emp_nm = jsonObj.getString("emp_nm");
                                final String room_nm = jsonObj.getString("room_nm");

                                if (!subject.isEmpty()) {
                                    final String classdata = subject + "\n" + curi_num;
                                    Log.e("Your class is ", classdata);

                                    final int finalI = i;
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Random random = new Random();
                                            int color = 0;
                                            color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                                            dtv[finalI].setBackgroundColor(color);
                                            dtv[finalI].setText(classdata);
                                            if (finalI >= 7) {
                                                if (dtv[finalI].getText().toString().equals(dtv[finalI - 7].getText().toString())) {
                                                    dtv[finalI].setVisibility(View.GONE);
                                                    dtv[finalI - 7].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2));
                                                }
                                            }
                                        }
                                    });
                                }

                            }
                        }
                        asyncDialog.dismiss();

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
}
