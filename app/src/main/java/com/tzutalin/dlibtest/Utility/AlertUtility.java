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
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.CameraActivity;
import com.tzutalin.dlibtest.OnGetImageListener;
import com.tzutalin.dlibtest.domain.ResponseLandmark;
import com.tzutalin.dlibtest.RetrofitConnection;
import com.tzutalin.dlibtest.domain.ResponseFeedback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertUtility {

    Vibrator vibrator;
    Ringtone ringtone;
    Context mContext;
    Uri alramSound;

    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;
    ResponseLandmark responseLandmark;

    String TAG = "AlertUtility";
    int StreamType = 0;
    MediaPlayer mAudio = null;
    boolean isPlay = false;
    int sleep_step = 0;
    int time = 3000;

    public AlertUtility(Context mContext){
        this.mContext = mContext;
        mAudio = new MediaPlayer();
        vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        alramSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //ringtone = RingtoneManager.getRingtone(mContext.getApplicationContext(), alramSound);

        try{
            mAudio.setDataSource(mContext, alramSound);
            mAudio.setAudioStreamType(StreamType);
            mAudio.setLooping(true);
            mAudio.prepare();
        }catch(Exception e){

        }

        builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);

    }


    public void vibrate()
    {
        if(sleep_step == 2){
            vibrator.vibrate(60000);
        }
        else if(sleep_step == 3) {
            vibrator.vibrate(90000);
        }
    }

    public void alram(){
/*        try {
            //mAudio.setDataSource(alramSound.toString());
            mAudio.setDataSource(mContext, alramSound);
            mAudio.setAudioStreamType(StreamType);
            mAudio.setLooping(true);
            mAudio.prepareAsync();

        }catch (Exception e){

        }*/
        mAudio.start();
        isPlay = true;
    }

    public void alramStop(){

        if(mAudio.isPlaying()){
            mAudio.pause();
            isPlay = false;
        }
        else{
            // 알람 실행 중 아님
        }
    }

    public void feedbackDialog(String cause){
        Log.e("AlertUtility", "feedbackDialog Activate");
        builder.setTitle("졸음이 인식되었습니다.").setMessage("원인 : "+ cause +", 졸음단계 : " + sleep_step);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                alramStop();
                OnGetImageListener.isBlue = true;
                vibrator.cancel();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                RetrofitConnection retrofitConnection = new RetrofitConnection();
                OnGetImageListener.isBlue = true;
                //AWS 스프링 공인 주소, http://15.165.116.82:8080
                //모듈 직접 접근 http://15.165.116.82:1234
                //http://15.165.116.82:8080/api/value/ REST API 로 데이터 전송
                retrofitConnection.setRetrofit("http://15.165.116.82:8080/");

                ResponseFeedback responseFeedback = new ResponseFeedback(); // Date And isCorrect
                //Call call = retrofitConnection.getServer().sendData(jsonData);
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

                alramStop();
                vibrator.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        time = 3000;
        if(sleep_step == 1){
            time = 30000;
        }
        else if(sleep_step == 2){
            time = 60000;
        }
        else if(sleep_step == 3){
            time = 90000;
        }
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                d.dismiss();
                alramStop();
                vibrator.cancel();
                // 재실행
                CameraActivity.onClickStartCount(null);
                //isBule = true;
                OnGetImageListener.isBlue = true;

            }
        }, time);
    }



}
