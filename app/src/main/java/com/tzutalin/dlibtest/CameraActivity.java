/*
 * Copyright 2016-present Tzutalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tzutalin.dlibtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tzutalin.dlibtest.Utility.AlertUtility;
import com.tzutalin.dlibtest.Utility.SleepStepManager;
import com.tzutalin.dlibtest.Utility.TimerHandler;
import com.tzutalin.dlibtest.Utility.TimerMinuteHandler;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.model.User;

import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darrenl on 2016/5/20.
 */
public class CameraActivity extends AppCompatActivity {

    private static int OVERLAY_PERMISSION_REQ_CODE = 1;
    static Context instanceContext;
    static public Boolean isActivateNetwork = true;
    RetrofitConnection retrofitConnection;
    SleepStepManager sleepStepManager;
    static TimerHandler timerHandler;
    static int currentColor;
    static TimerMinuteHandler countHandler;

    ResponseLandmarkDTO landmarkdto;

    public static Handler UiHandler;
    AlertUtility alertUtility;

    static View v;

    Bitmap bitm;
    ImageView i;

    static TextView textViewWeakTime;
    static TextView textViewStage;

    Button btn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        retrofitConnection = new RetrofitConnection();
        retrofitConnection.setRetrofit("http://15.165.116.82:8080/");
        sleepStepManager = new SleepStepManager(retrofitConnection);
        timerHandler = new TimerHandler(retrofitConnection, this);
        countHandler = new TimerMinuteHandler(retrofitConnection, this);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        instanceContext = this;
        UiHandler = new Handler(Looper.getMainLooper());

        textViewWeakTime = (TextView)findViewById(R.id.textViewIsWeakTime);
        textViewStage = (TextView)findViewById(R.id.textViewStage);

        i = (ImageView)findViewById(R.id.ttt);
        bitm = BitmapFactory.decodeResource(getResources(), R.drawable.stopimage);

        alertUtility = new AlertUtility(this);

        v = (View)findViewById(R.id.view1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this.getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }

        if (null == savedInstanceState) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CameraConnectionFragment.newInstance())
                    .commit();
        }

        onClickStartCount(null);
        countHandlerStart();
        sleepStepManager.resetSleepStep();

    }


    @Override
    protected void onPause() {
        super.onPause();
        onClickStopCount(null);
        countHandlerStop();
        AlertUtility alertUtility = OnGetImageListener.getAlertUtility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClickStartCount(null);
        countHandlerStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickStopCount(null);
        countHandlerStop();
        sleepStepManager.resetSleepStep();
        AlertUtility alertUtility = OnGetImageListener.getAlertUtility();
        Log.e("CameraActivity", "객체 : " + alertUtility.toString() );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this.getApplicationContext())) {
                    Toast.makeText(CameraActivity.this, "CameraActivity\", \"SYSTEM_ALERT_WINDOW, permission not granted...", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }
    }



    static public Context getContext(){
        return instanceContext;
    }

    static public Boolean getIsActivateNetwork(){
        return isActivateNetwork;
    }

    public static void runOnUi(Runnable runnable){
        UiHandler.post(runnable);
    }

    static public void setStage(String stage){

        runOnUi(new Runnable() {
            @Override
            public void run() {
                textViewStage.setText("졸음 단계 : "+stage);
            }
        });
    }

    static public void setWeakTime(String isWeakTime){

        runOnUi(new Runnable() {
            @Override
            public void run() {
                textViewWeakTime.setText("취약 시간대 : "+isWeakTime);
            }
        });
    }

    static public void setColor(int changeColor)
    {
        runOnUi(new Runnable() {
            @Override
            public void run() {

                // 1 = blue , 2 = red , 3 = yellow
                if(changeColor == 1)
                {
                    v.setBackgroundColor(Color.BLUE);
                }
                else if(changeColor == 2)
                {
                    v.setBackgroundColor(Color.RED);
                }
                else if(changeColor == 3)
                {
                    v.setBackgroundColor(Color.YELLOW);
                }

            }
        });

        currentColor = changeColor;
    }



    public static int getCurrentColor(){
        return currentColor;
    }



    public void OnclickHandler(View view) {

        String currentPause;

        if(isActivateNetwork == true){
            isActivateNetwork = false;
            currentPause = "ON";

            i.bringToFront();  //이미지 최상단으로 올리기
            i.setImageBitmap(bitm);  // 이미지 추가
            i.setVisibility(View.VISIBLE);  // 이미지 다시 생성하기

            onClickStopCount(null);
            countHandlerStop();
        }else{
            isActivateNetwork = true;
            currentPause = "OFF";

            i.setVisibility(View.GONE); // 이미지 사라지게 하는 것

            onClickStartCount(null);
            countHandlerStart();
        }
        Toast.makeText(CameraActivity.this, "일시정지 " + currentPause, Toast.LENGTH_SHORT).show();

    }

    public void onClickReset(View view){
        sleepStepManager.resetSleepStep();
        Toast.makeText(CameraActivity.this, "졸음단계 초기화 " , Toast.LENGTH_SHORT).show();
    }

    static public void onClickStartCount(View view){
        timerHandler.sendEmptyMessage(timerHandler.MESSAGE_TIMER_START);
        Toast.makeText(CameraActivity.getContext(),"카운트 실행", Toast.LENGTH_SHORT).show();
    }
    static public void onClickStopCount(View view){
        timerHandler.sendEmptyMessage(timerHandler.MESSAGE_TIMER_STOP);
        Toast.makeText(CameraActivity.getContext(),"카운트 중지", Toast.LENGTH_SHORT).show();
    }

    static public void countHandlerStart(){
        countHandler.sendEmptyMessage(timerHandler.MESSAGE_TIMER_START);
    }

    static public void countHandlerStop(){
        countHandler.sendEmptyMessage(timerHandler.MESSAGE_TIMER_STOP);
    }

    static public void countHandlerPause(){
        countHandler.sendEmptyMessage(timerHandler.MESSAGE_TIMER_PAUSE);
    }

    public void onClickDrop(View view){
        Call call = retrofitConnection.getServer().dropSleepStep(User.getInstance().getUserDTO());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void onClickTimerLog(View view){
        Call call = retrofitConnection.getServer().timer(User.getInstance().getUserDTO());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}