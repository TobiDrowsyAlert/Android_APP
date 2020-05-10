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
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tzutalin.dlibtest.Utility.SleepStepManager;
import com.tzutalin.dlibtest.Utility.TimerHandler;

/**
 * Created by darrenl on 2016/5/20.
 */
public class CameraActivity extends Activity {

    private static int OVERLAY_PERMISSION_REQ_CODE = 1;
    static Context instanceContext;
    static public Boolean isActivateNetwork = true;
    RetrofitConnection retrofitConnection;
    SleepStepManager sleepStepManager;
    static TimerHandler timerHandler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        retrofitConnection = new RetrofitConnection();
        retrofitConnection.setRetrofit("http://15.165.116.82:8080/");
        sleepStepManager = new SleepStepManager(retrofitConnection);
        timerHandler = new TimerHandler(retrofitConnection);

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        instanceContext = this;

        setContentView(R.layout.activity_camera);

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

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickStopCount(null);

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

    public void OnclickHandler(View view) {

        String currentPause;

        if(isActivateNetwork == true){
            isActivateNetwork = false;
            currentPause = "ON";
            onClickStopCount(null);
        }else{
            isActivateNetwork = true;
            currentPause = "OFF";
            onClickStartCount(null);
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

}