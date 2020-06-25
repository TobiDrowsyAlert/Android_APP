package com.tzutalin.dlibtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tzutalin.dlibtest.user.model.User;

import retrofit2.Retrofit;

public class menuEvent extends AppCompatActivity {

    private final int DEFAULT_STEP_ONE_TIME = 5;
    private final int DEFAULT_STEP_TWO_TIME = 10;
    private final int DEFAULT_STEP_THREE_TIME = 15;
    private final int DEFAULT_ALRAM_VOLUME = 50;
    private RetrofitConnection retrofitConnection;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        retrofitConnection = RetrofitConnection.getInstance();
        SharedPreferences settingPreferences = getSharedPreferences("settingPreferences", MODE_PRIVATE);

        if(!settingPreferences.contains("stepOne")){
            Log.e("menuEvent", "환경설정 초기화");
            SharedPreferences.Editor editor = settingPreferences.edit();
            editor.putInt("stepOne", DEFAULT_STEP_ONE_TIME);
            editor.putInt("stepTwo", DEFAULT_STEP_TWO_TIME);
            editor.putInt("stepThree", DEFAULT_STEP_THREE_TIME);
            editor.putInt("soundBar", DEFAULT_ALRAM_VOLUME);

            editor.commit();
        }



        Button btn_menu_1 =(Button)findViewById(R.id.menu_1);
        Button btn_menu_2 =(Button)findViewById(R.id.menu_2);
        Button btn_menu_3 =(Button)findViewById(R.id.menu_3);
        Button btn_menu_4 =(Button)findViewById(R.id.menu_4);
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

        btn_menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, WebViewActivity.class);
                startActivity(intent);
            }
        });


        btn_menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btn_menu_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuEvent.this, usedashboradActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btn_menu_5.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                double avgStage = User.getInstance().getAvgStage();
                if(avgStage < 1){
                    intent = new Intent(LoginActivity.getContext(), dashboardBlueActivity.class);
                }else if(avgStage < 2){
                    intent = new Intent(LoginActivity.getContext(), dashboardYellowActivity.class);
                }else{
                    intent = new Intent(LoginActivity.getContext(), dashboardRedActivity.class);
                }

                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            retrofitConnection.requestLogout();
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrofitConnection.requestLogout();
    }


}
