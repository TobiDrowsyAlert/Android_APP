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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.naver.speech.clientapi.SpeechRecognitionResult;
import com.tzutalin.dlibtest.Utility.AlertUtility;
import com.tzutalin.dlibtest.Utility.MediaUtility;
import com.tzutalin.dlibtest.Utility.SleepStepManager;
import com.tzutalin.dlibtest.Utility.TimerHandler;
import com.tzutalin.dlibtest.Utility.TimerMinuteHandler;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.model.User;
import com.tzutalin.dlibtest.utils.AudioWriterPCM;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by darrenl on 2016/5/20.
 */
public class CameraActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int OVERLAY_PERMISSION_REQ_CODE = 1;
    static Context instanceContext;
    static public Boolean isActivateNetwork = true;
    RetrofitConnection retrofitConnection;
    SleepStepManager sleepStepManager;
    static TimerHandler timerHandler;
    static int currentColor;
    static TimerMinuteHandler countHandler;
    static AlertDialog alertDialog;

    Button btnSpeech;

    // 음성인식 변수들
    private static final String CLIENT_ID = "lpsdk0tjp1"; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private CameraActivity.RecognitionHandler handler;
    private static NaverRecognizer naverRecognizer;
    private String mResult;
    private AudioWriterPCM writer;

    public static Handler UiHandler;
    AlertUtility alertUtility;

    static View v;

    Bitmap bitm;

    Bitmap bitm_mic;
    Bitmap bitm_stretch;
    Bitmap bitm_undo;
    Bitmap bitm_redo;

    ImageView i;

    ImageView mic;  // 마이크 이미지
    ImageView stretch;  //스트레칭 이미지
    ImageView undo; // 왼쪽아래 이미지
    ImageView redo; //



    static TextView textViewWeakTime;
    static TextView textViewStage;

    int select;

    Button btn;



    int flag = 1;  // 0이면 왼쪽, 1이면 오른쪽 , 2면 스트레칭 x 상태

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {

        switch (msg.what) {
            case R.id.clientReady: // 음성인식 준비 가능
                //동작 확인용 인터페이스 추가 필요
                writer = new AudioWriterPCM(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;
            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;
            case R.id.partialResult:

                mResult = (String) (msg.obj);
                // 실패 시 원인 설명 필요
                break;
            case R.id.finalResult: // 최종 인식 결과
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                for(String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                    if(result.contains("예") && alertDialog != null){
                        Toast.makeText(CameraActivity.this, "응답 `예` 확인" , Toast.LENGTH_SHORT).show();
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                    }
                    else if(result.contains("아니요") && alertDialog != null){
                        Toast.makeText(CameraActivity.this, "응답 `아니요` 확인" , Toast.LENGTH_SHORT).show();
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).callOnClick();
                    }
                    else if(alertDialog == null){
                        if(!MediaUtility.getInstance(this).isMeaningCorrect(result, select)){
                            // 졸음 단계 상승 + 틀림을 음성으로 알려줌
                            MediaUtility.getInstance(this).startSound(MediaUtility.INT_CORRECT_REPLY);
                        }
                        else{
                            // 졸음 단계 상승 X + 맞았음을 알려줌
                            MediaUtility.getInstance(this).startSound(MediaUtility.INT_INCORRECT_REPLY);
                        }
                    }
                }
                mResult = strBuf.toString();
                // 결과값 텍스트로 보여주기
                //txtResult.setText(mResult);
                break;
            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }
                mResult = "Error code : " + msg.obj.toString();
                // 실패 시 결과 값 테스트로 보여주기
                //txtResult.setText(mResult);
                break;
            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
/*                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);*/
                break;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        retrofitConnection = RetrofitConnection.getInstance();
        sleepStepManager = new SleepStepManager(retrofitConnection);
        timerHandler = new TimerHandler(retrofitConnection, this);
        countHandler = new TimerMinuteHandler(retrofitConnection, this);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        instanceContext = this;
        UiHandler = new Handler(Looper.getMainLooper());

        textViewWeakTime = (TextView)findViewById(R.id.textViewIsWeakTime);
        textViewStage = (TextView)findViewById(R.id.textViewStage);

        i = (ImageView)findViewById(R.id.ttt);

        mic = (ImageView)findViewById(R.id.mic_image);
        stretch = (ImageView)findViewById(R.id.stretchImage);
        undo = (ImageView)findViewById(R.id.undo_L);
        redo = (ImageView)findViewById(R.id.redo_R);

        btnSpeech = (Button)findViewById(R.id.btnSpeech);
        bitm = BitmapFactory.decodeResource(getResources(), R.drawable.stopimage);

        bitm_mic = BitmapFactory.decodeResource(getResources(), R.drawable.mic_48);
        bitm_stretch = BitmapFactory.decodeResource(getResources(), R.drawable.roll2);
        bitm_undo = BitmapFactory.decodeResource(getResources(), R.drawable.lll);
        bitm_redo = BitmapFactory.decodeResource(getResources(), R.drawable.rrr);

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

        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeech();
            }
        });

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
    protected void onStart() {
        super.onStart(); // 음성인식 서버 초기화는 여기서
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClickStartCount(null);
        countHandlerStart();
    }

    @Override
    protected void onStop() {
        super.onStop(); // 음성인식 서버 종료
        naverRecognizer.getSpeechRecognizer().release();
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

    public void OnclickTest(View view)
    {

        if(isActivateNetwork == true)
        {
            isActivateNetwork = false;

            stretch.bringToFront();
            stretch.setImageBitmap(bitm_stretch);
            stretch.setVisibility(view.VISIBLE);

            undo.bringToFront();
            undo.setImageBitmap(bitm_undo);
            undo.setVisibility(View.VISIBLE);

            redo.bringToFront();
            redo.setImageBitmap(bitm_redo);
            redo.setVisibility(View.VISIBLE);

        }
        else
        {
            isActivateNetwork = true;

            stretch.setVisibility(view.GONE);
            undo.setVisibility(View.GONE);
            redo.setVisibility(View.GONE);
        }

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

    static public void startSpeech(){
        if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
            naverRecognizer.recognize();
            // 실행 중 동작 아이콘 추가 부분
        } else {
            Log.d(TAG, "stop and wait Final Result");
            naverRecognizer.getSpeechRecognizer().stop();
        }
    }

    static public void startSpeech(AlertDialog alertDialog){
        if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
            naverRecognizer.recognize();
            CameraActivity.alertDialog = alertDialog;
            // 실행 중 동작 아이콘 추가 부분
        } else {
            Log.d(TAG, "stop and wait Final Result");
            naverRecognizer.getSpeechRecognizer().stop();
        }
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<CameraActivity> mActivity;
        RecognitionHandler(CameraActivity activity) {
            mActivity = new WeakReference<CameraActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            CameraActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}

