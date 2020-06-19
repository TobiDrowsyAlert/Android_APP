package com.tzutalin.dlibtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;

public class use1Activity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_1);





        ImageButton btn_left =(ImageButton)findViewById(R.id.btn_left);
        ImageButton btn_right =(ImageButton)findViewById(R.id.btn_right);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(use1Activity.this, menuEvent.class);
                startActivity(intent);
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(use1Activity.this, use2Activity.class);
                startActivity(intent);
            }
        });

    }
};
