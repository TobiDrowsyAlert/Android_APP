package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.CameraActivity;
import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
import com.tzutalin.dlibtest.RetrofitConnection;
import com.tzutalin.dlibtest.domain.RequestFeedbackDTO;
import com.tzutalin.dlibtest.domain.RequestFeedbackDTO;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.domain.SleepCode;
import com.tzutalin.dlibtest.user.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertUtility {


    final int INT_ALRAM_TIME = 10000;
    private final int DEFAULT_STEP_ONE_TIME = 5;
    private final int DEFAULT_STEP_TWO_TIME = 10;
    private final int DEFAULT_STEP_THREE_TIME = 15;
    private final int DEFAULT_ALRAM_VOLUME = 50;

    Context mContext;

    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;
    ResponseLandmarkDTO responseDrowsyResponse;

    SharedPreferences settingPreferences;

    Runnable dialogRunnable;
    Runnable alarmRunnable;

    public AlarmManager alaramManager;
    Handler handler;

    Float stepOneAlarmTime;
    Float stepTwoAlarmTime;
    Float stepThreeAlarmTime;

    String TAG = "AlertUtility";

    int time = 3000;

    public AlertUtility(Context mContext){
        this.mContext = mContext;
        alaramManager = new AlarmManager(mContext);
        handler = new Handler();
        builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);

        SharedPreferences settingPreferences = mContext.getSharedPreferences("settingPreferences", Context.MODE_PRIVATE);

        stepOneAlarmTime = settingPreferences.getFloat("stepOneTime", DEFAULT_STEP_ONE_TIME) * 1000;
        stepTwoAlarmTime = settingPreferences.getFloat("stepTwoTime", DEFAULT_STEP_TWO_TIME) * 1000;
        stepThreeAlarmTime = settingPreferences.getFloat("stepThreeTime", DEFAULT_STEP_THREE_TIME) * 1000;

        RetrofitConnection retrofitConnection = new RetrofitConnection();
        retrofitConnection.setRetrofit("http://15.165.116.82:8080/");


        Log.e("알람시간 체크", "step1 : " + stepOneAlarmTime.toString() + ", " +
                "step2 : " + stepTwoAlarmTime.toString() + ", " +
                "step3 : " + stepThreeAlarmTime.toString());



    }

    public void feedbackTrans(int logNo, Boolean isCorrect){
        RequestFeedbackDTO requestFeedbackDTO = new RequestFeedbackDTO();
        requestFeedbackDTO.setLogNo(logNo);
        requestFeedbackDTO.setCorrect(isCorrect);
        requestFeedbackDTO.setUserId(User.getInstance().getUserId());
        requestFeedbackDTO.setIsFeedback(true);
        Call call = retrofitConnection.getServer().feedback(requestFeedbackDTO);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Log.e("피드백 전송 성공", "");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("피드백 전송 실패", "");
            }
        });
    }

    public void feedbackDialog(int logNo, String cause){
        Log.e("AlertUtility", "feedbackDialog Activate");
        Toast.makeText(mContext.getApplicationContext(), cause, Toast.LENGTH_LONG).show();

        builder.setTitle("졸음이 인식되었습니다.").setMessage("원인 : "+ cause +", 졸음단계 : " + responseDrowsyResponse.getSleep_step());
        alaramManager.alram();
        alaramManager.vibrate(responseDrowsyResponse.getSleep_step());
        // 새로운 졸음 들어온 것, 기존 알람 소리 초기화 필요

        Log.e(TAG, "LogNo : " + logNo );

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(dialogRunnable);
                alaramManager.alramStop();
                alaramManager.vibrateCancel();
                feedbackTrans(logNo,true);
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(dialogRunnable);
                alaramManager.alramStop();
                alaramManager.vibrateCancel();
                feedbackTrans(logNo,false);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        int sleep_step = responseDrowsyResponse.getSleep_step();

        if(sleep_step == 1){
            time = stepOneAlarmTime.intValue();
        } else if(sleep_step == 2){
            time = stepTwoAlarmTime.intValue();
        }else if(sleep_step == 3){
            time = stepThreeAlarmTime.intValue();
        }

        delayTime(time, alertDialog);
    }

    public void delayTime(long time, final Dialog d){
        dialogRunnable = new Runnable() {
            @Override
            public void run() {

                d.dismiss();

                // 재실행
                CameraActivity.onClickStartCount(null);

                Toast.makeText(mContext, "무응답, 알람 종료", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "DelayTime 실행");
            }
        };

        alarmRunnable = new Runnable() {
            @Override
            public void run() {
                alaramManager.alramStop();
                alaramManager.vibrateCancel();
            }
        };


        handler.removeCallbacks(alarmRunnable);
        handler.postDelayed(alarmRunnable, time);
        handler.postDelayed(dialogRunnable, time);
    }

    public RetrofitConnection getRetrofitConnection(){
        return retrofitConnection;
    }

    public void setRetrofitConnection(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
    }

    public void generateRetrofitConnectionWithURL(String url){
        this.retrofitConnection = new RetrofitConnection(url);
    }

    public void requestSleepAnalyze(RequestAnalyzeSleepDTO requestDTO){
        Call<ResponseLandmarkDTO> call = retrofitConnection.getServer().sendData(requestDTO);
        call.enqueue(new Callback<ResponseLandmarkDTO>() {
            @Override
            public void onResponse(Call<ResponseLandmarkDTO> call, Response<ResponseLandmarkDTO> response) {

                // 성공적으로 서버 통신 성공
                if (response.isSuccessful()) {
                    responseDrowsyResponse = response.body();
                    Log.e(TAG, "ResponseLandmark : " + response.body());

                    for(SleepCode sleepCode : SleepCode.values()){
                        Log.e(TAG, "sleepCode : " + sleepCode + "StatusCode : " + responseDrowsyResponse.getStatus_code());
                        if(sleepCode.getCode() == responseDrowsyResponse.getStatus_code()){
                            feedbackDialog(responseDrowsyResponse.getLogNo(), sleepCode.getReason());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLandmarkDTO> call, Throwable t) {
                Log.e("RetrofitTest", "No one in camera + Network Error");
            }
        });
    }



}
