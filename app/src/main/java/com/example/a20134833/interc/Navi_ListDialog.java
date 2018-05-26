package com.example.a20134833.interc;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Navi_ListDialog {  // 네비게이션에서 단과대 선택하는 다이얼로그

    Navigation navigation = new Navigation();

    private Context context;

    public Navi_ListDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_navi__list_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        final String[] LIST_MENU = {"인문과학대학","자연과학대학","법과대학","사회과학대학","경상대학","공과대학","IT융합대학","사범대학","외국어대학","체육대학","의과대학","치과대학","약학대학","미술대학","기초교육대학","보건과학대학","미래사회융합대학"} ;
        ArrayAdapter adapter = new ArrayAdapter(dlg.getContext(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        final ListView listview = (ListView) dlg.findViewById(R.id.Dialog_ListView) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;
                main_label.setText(strText);
                Log.e("OnItemClicked", String.valueOf(position));
                navigation.setDate(position);
                dlg.dismiss();
                // TODO : use strText
            }
        }) ;
    }
}