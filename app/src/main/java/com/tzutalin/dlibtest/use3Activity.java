package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class use3Activity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_3);





        ImageButton btn_left =(ImageButton)findViewById(R.id.btn_left);
        //ImageButton btn_right =(ImageButton)findViewById(R.id.btn_right);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(use3Activity.this, use2Activity.class);
                startActivity(intent);
            }
        });

        /*btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(use3Activity.this, menuEvent.class);
                startActivity(intent);
            }
        });*/

    }
};
