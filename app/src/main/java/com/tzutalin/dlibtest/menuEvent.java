package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class menuEvent extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button btn_menu_1 =(Button)findViewById(R.id.menu_1);
//        Button btn_menu_2 =(Button)findViewById(R.id.menu_2);
        Button btn_menu_3 =(Button)findViewById(R.id.menu_3);
//        Button btn_menu_4 =(Button)findViewById(R.id.menu_4);
        Button btn_menu_5 =(Button)findViewById(R.id.menu_5);

        btn_menu_1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(menuEvent.this, CameraActivity.class);
               // 원래 밑에가 기본 오류 발생 시 여기부터 시작.
               /*Intent intent = new Intent(menuEvent.this, MainActivity.class);*/
               startActivity(intent);
           }
        });

        /*btn_menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, MainActivity.class);
                startActivity(intent);
            }
        });*/


        btn_menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        /*btn_menu_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
        /*btn_menu_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, ViewHandler.class);
                //startActivity(intent);
            }
        });*/
    }
}
