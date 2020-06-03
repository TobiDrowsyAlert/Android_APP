package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    Context mContext;

    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;
    ResponseLandmarkDTO responseDrowsyResponse;

    Runnable dialogRunnable;
    Runnable alarmRunnable;

    public AlarmManager alaramManager;
    Handler handler;


    String TAG = "AlertUtility";

    int time = 3000;

    public AlertUtility(Context mContext){
        this.mContext = mContext;
        alaramManager = new AlarmManager(mContext);
        handler = new Handler();
        builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
    }


    public void feedbackDialog(String cause){
        Log.e("AlertUtility", "feedbackDialog Activate");
        Toast.makeText(mContext.getApplicationContext(), cause, Toast.LENGTH_LONG).show();

        builder.setTitle("졸음이 인식되었습니다.").setMessage("원인 : "+ cause +", 졸음단계 : " + responseDrowsyResponse.getSleep_step());
        alaramManager.alram();
        alaramManager.vibrate(responseDrowsyResponse.getSleep_step());
        // 새로운 졸음 들어온 것, 기존 알람 소리 초기화 필요

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(dialogRunnable);
                alaramManager.alramStop();
                alaramManager.vibrateCancel();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(dialogRunnable);

                RetrofitConnection retrofitConnection = new RetrofitConnection();
                retrofitConnection.setRetrofit("http://15.165.116.82:8080/");
                RequestFeedbackDTO requestFeedbackDTO = new RequestFeedbackDTO();
                requestFeedbackDTO.setCorrect(true);
                requestFeedbackDTO.setDate(responseDrowsyResponse.getCurTime());
                requestFeedbackDTO.setUserId(User.getInstance().getUserId());
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

                alaramManager.alramStop();
                alaramManager.vibrateCancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        time = responseDrowsyResponse.getSleep_step() * INT_ALRAM_TIME;

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

    public void setResponseLandmark(ResponseLandmarkDTO responseLandmarkDTO){
        this.responseDrowsyResponse = responseLandmarkDTO;
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
                    setResponseLandmark(responseDrowsyResponse);
                    Log.e(TAG, "ResponseLandmark : " + response.body());

                    for(SleepCode sleepCode : SleepCode.values()){
                        Log.e(TAG, "sleepCode : " + sleepCode + "StatusCode : " + responseDrowsyResponse.getStatus_code());
                        if(sleepCode.getCode() == responseDrowsyResponse.getStatus_code()){
                            feedbackDialog(sleepCode.getReason());
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
