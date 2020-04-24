package com.tzutalin.dlibtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class statisticsPage extends AppCompatActivity {

    Button btnL;
    Button btnR;

    @SuppressLint("WrongViewCast")
    @Override
    protected  void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        /*setContentView(R.layout.statistics_01);

        btnL = (Button)findViewById(R.id.btn_left);
        btnR = (Button)findViewById(R.id.btn_right);

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment1 fragment1 = new Fragment1();

                transaction.replace(R.id.frame, fragment1);
                transaction.commit();
            }
        });

        /*btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment2 fragment2 = new Fragment2();

                transaction.replace(R.id.frame, fragment2);
                transaction.commit();
            }
        });*/

    }



}
