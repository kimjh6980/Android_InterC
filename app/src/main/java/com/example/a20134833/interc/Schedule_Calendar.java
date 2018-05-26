package com.example.a20134833.interc;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a20134833.interc.decorators.EventDecorator;
import com.example.a20134833.interc.decorators.OneDayDecorator;
import com.example.a20134833.interc.decorators.SaturdayDecorator;
import com.example.a20134833.interc.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Shows off the most basic usage
 */
public class Schedule_Calendar extends AppCompatActivity{

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    List<String> array = new ArrayList<String>();
    String[] array_result = {};

    ArrayList<String> items;
    ArrayAdapter adapter;
    ListView listview;

    ProgressDialog asyncDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 빈 데이터 리스트 생성.
        items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items) ;

        // listview 생성 및 adapter 지정.
        listview = (ListView) findViewById(R.id.Event_ListView) ;
        listview.setAdapter(adapter) ;

        asyncDialog = new ProgressDialog(Schedule_Calendar.this);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators( // 달력 꾸미기
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        Calendar_receive_Asycn("2018","04");    // 시작하면 초기화면 리스트는 4월자임

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {    // 일 바꿀때 뜨는 알람
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                String S_M = String.format("%02d", Month);
                String S_D = String.format("%02d", Day);
                String shot_Day = Year + "." + S_M + "." + S_D;

                Log.i("shot_Day test", shot_Day + "");

                SearchingEvent(shot_Day);
                materialCalendarView.clearSelection();
            }
        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {   // 월 바꿀떄
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String Year = String.format("%04d", date.getYear());
                String Month = String.format("%02d", date.getMonth() + 1);
                Log.e("ChangeMonth", Year + "." + Month);

                Calendar_receive_Asycn(Year, Month);
            }
        });
    }

    public void SearchingEvent(String day)  {
        for(int i = 0;i < items.size(); i++)
        {
            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
            if (items.get(i).toString().contains(day))
            {
                // 검색된 데이터를 리스트에 추가한다.
                String[] date = items.get(i).split("-");
                String eventname = date[1];

                Toast.makeText(getApplicationContext(), day +"\n"+eventname, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 달력 보여주는 이벤트
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            Log.e("cal", "back");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            //특정날짜 달력에 점표시해주는곳
            //월은 0이 1월 년,일은 그대로
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환

            for(int i = 0 ; i < Time_Result.length ; i ++){
                String[] time = Time_Result[i].split("\\.");    // 받아온 데이터 날짜 변환
                Log.e("TimeResult " + i, " = " + time[0] +"/"+time[1]+"/"+time[2]);
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.set(year,month-1,dayy);
            }

            // 뒤에 하나가 안떠서 억지로 추가해준거(왜안뜨는지는 모르겠다 ㅠ)
            CalendarDay day = CalendarDay.from(calendar);
            dates.add(day);
            calendar.set(2018, 01,01);

            return dates;
        }

        @Override
        protected void onPreExecute() {
            Log.e("cal_", "pre");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            Log.e("cal", "post");

            super.onPostExecute(calendarDays);
/*
            if (isFinishing()) {
                return;
            }
*/
            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,Schedule_Calendar.this));    // 오늘

            asyncDialog.dismiss();  // 로딩중입니다 Dialog 끄기
        }
    }

    //------------OKHttp -> 일정받아오기
    public final String url = "http://api.udp.cc/libs/getCalendar.php";
    OkHttpClient client = new OkHttpClient();
    String responseBody = null;

    public void Calendar_receive_Asycn(final String year,final String month) {
        (new AsyncTask<Schedule_Calendar, Void, String>() {
            @Override
            protected String doInBackground(Schedule_Calendar... mainActivities) {
                Log.e("Do In Back", "Start");
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, year, month);    // 인자값 : year, month
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                Log.e("Receive_", "pre");

                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("로딩중입니다..");
                asyncDialog.show();

                items.clear();
                // listview 선택 초기화.
                listview.clearChoices();
                // listview 갱신.
                adapter.notifyDataSetChanged();

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {

                Log.e("Receive_", "post");
            }
        }).execute();

        return;
    }

    public class ConnectServer {//Client 생성

        public int requestPost(String url,final String year,final String month) {
            array.clear();

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("method", "")
                    .add("target", "CHOSUN")
                    .add("year", "2018").build();


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
                        int EventConut = 1;
                        responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        Log.e("Response == ", String.valueOf(jsonObject));
                        String Data = jsonObject.getString("data");
                        JSONArray jsonArr = new JSONArray(Data);
                        String SetMonth = year + "." + month + ".";
                        Log.e("Year+Month = ", SetMonth);
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            final String time = jsonObj.getString("name");
                            final String lat = jsonObj.getString("stdate");
                            final String lon = jsonObj.getString("eddate");

                            if(lat.contains(SetMonth))    {
                                Log.e("JsonArray_C - " + i, "Data = " + time + "/" + lat + "/" + lon);
                                items.add(EventConut + " / " +lat + " ~ " + lon + " \n " + "-  " + time);
                                EventConut++;
                                array.add(lat);
                            }
                        }
                        array_result = array.toArray(new String[array.size()]);
                        for(int i=0; i<array_result.length; i++)    {
                            Log.e("Array result " + i, "=" + array_result[i]);
                        }

                        if(array_result != null)    {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            Log.e("adapter" ,"RUN");
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
                            new ApiSimulator(array_result).executeOnExecutor(Executors.newSingleThreadExecutor());
                        }   else    {
                            Toast.makeText(Schedule_Calendar.this, "행사가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        //Json 예외처리
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}