package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.CameraActivity;
import com.tzutalin.dlibtest.OnGetImageListener;
import com.tzutalin.dlibtest.domain.ResponseLandmark;
import com.tzutalin.dlibtest.RetrofitConnection;
import com.tzutalin.dlibtest.domain.ResponseFeedback;

import java.util.ArrayList;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertUtility {

    final int INT_ALRAM_TIME = 10000;

    Context mContext;

    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;
    ResponseLandmark responseLandmark;

    Runnable runnable;

    AlarmManager alaramManager;
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
                ResponseFeedback responseFeedback = new ResponseFeedback();
                responseFeedback.setCorrect(true);
                responseFeedback.setDate(responseLandmark.getCurTime());
                Call call = retrofitConnection.getServer().feedback(responseFeedback);
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

    public void setResponseLandmark(ResponseLandmark responseLandmark){
        this.responseLandmark = responseLandmark;
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




}
