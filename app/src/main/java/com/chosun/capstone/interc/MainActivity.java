package com.chosun.capstone.interc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void B_Bus_Location(View v)  {
        Intent intent = new Intent(getApplicationContext(), BusLoaction.class);
        startActivity(intent);
    }

    public void B_School_Food(View v)  {
        Intent intent = new Intent(getApplicationContext(), SchoolFood.class);
        startActivity(intent);
    }

    public void B_Clc(View v)  {
        Intent intent = new Intent(getApplicationContext(), ClcWeb.class);
        startActivity(intent);
    }

    public void B_Calendar(View v)  {
        Intent intent = new Intent(getApplicationContext(), Schedule_Calendar.class);
        startActivity(intent);
    }

    public void B_Navi(View v)  {
        Intent intent = new Intent(getApplicationContext(), Navigation.class);
        startActivity(intent);
    }

    public void B_class(View view) {
        Intent intent = new Intent(getApplicationContext(), Class.class);
        startActivity(intent);
    }
}
