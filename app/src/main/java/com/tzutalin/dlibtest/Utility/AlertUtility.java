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

    // 상태 코드 상수
    private final int INT_BLINK = 100;
    private final int INT_BLIND = 101;
    private final int INT_YAWN = 200;
    private final int INT_DRIVER_AWAY = 300;
    private final int INT_DRIVER_AWARE_FAIL = 301;
    private final int INT_NORMAL = 400;

    final int INT_ALRAM_TIME = 10000;

    Context mContext;

    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;
    ResponseLandmarkDTO responseLandmark;

    Runnable runnable;

    public AlarmManager alaramManager;
    Handler handler;


    String TAG = "AlertUtility";

    int sleep_step = 0;
    int time = 3000;

    public static final int MESSAGE_DELAY_START = 100;
    public static final int MESSAGE_DELAY_STOP = 102;

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

        builder.setTitle("졸음이 인식되었습니다.").setMessage("원인 : "+ cause +", 졸음단계 : " + sleep_step);
        alaramManager.alram();
        alaramManager.vibrate(sleep_step);
        // 새로운 졸음 들어온 것, 기존 알람 소리 초기화 필요

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnable);
                alaramManager.alramStop();
                alaramManager.vibrateCancel();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnable);

                RetrofitConnection retrofitConnection = new RetrofitConnection();
                retrofitConnection.setRetrofit("http://15.165.116.82:8080/");
                RequestFeedbackDTO requestFeedbackDTO = new RequestFeedbackDTO();
                requestFeedbackDTO.setCorrect(true);
                requestFeedbackDTO.setDate(responseLandmark.getCurTime());
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

        time = 3000;
        time = sleep_step * INT_ALRAM_TIME;

        delayTime(time, alertDialog);
    }

    public void delayTime(long time, final Dialog d){
        runnable = new Runnable() {
            @Override
            public void run() {

                d.dismiss();

                // 재실행
                CameraActivity.onClickStartCount(null);

                Toast.makeText(mContext, "DelayTime 실행", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "DelayTime 실행");
            }
        };

        handler.removeCallbacks(alaramManager.getAlarmRunnable());
        handler.postDelayed(alaramManager.getAlarmRunnable(), time);
        handler.postDelayed(runnable, time);
    }

    public RetrofitConnection getRetrofitConnection(){
        return retrofitConnection;
    }

    public void setRetrofitConnection(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
    }

    public void setSleep_step(int sleep_step){
        this.sleep_step = sleep_step;
    }

    public int getSleep_step(){
        return sleep_step;
    }

    public void setResponseLandmark(ResponseLandmarkDTO responseLandmarkDTO){
        this.responseLandmark = responseLandmarkDTO;
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
                    sleep_step = response.body().getSleep_step();
                    ResponseLandmarkDTO responseLandmarkDTO = response.body();
                    setResponseLandmark(responseLandmarkDTO);

                    for(SleepCode sleepCode : SleepCode.values()){
                        if(sleepCode.getCode() == getSleep_step()){
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
