package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tzutalin.dlibtest.user.model.User;

public class dashboardYellowActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_yellow);

        ImageButton btn_right = (ImageButton)findViewById(R.id.btn_right);
        TextView editText_avgStage = (TextView)findViewById(R.id.avgStage);
        editText_avgStage.setText(String.valueOf(User.getInstance().getAvgStage()));

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(dashboardYellowActivity.this, menuEvent.class);
                startActivity(intent);
            }
        });

    }
}
