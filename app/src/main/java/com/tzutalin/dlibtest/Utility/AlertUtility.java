package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.ApiData;
import com.tzutalin.dlibtest.ResponseLandmark;
import com.tzutalin.dlibtest.RetrofitConnection;

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

    int StreamType = 0;
    MediaPlayer mAudio = null;
    boolean isPlay = false;

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


    public void vibrate(int time){
        vibrator.vibrate(time);
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
        builder.setTitle("졸음이 인식되었습니다.").setMessage(cause + "이 맞습니까?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                alramStop();
                vibrator.cancel();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                RetrofitConnection retrofitConnection = new RetrofitConnection();

                //AWS 스프링 공인 주소, http://15.165.116.82:8080
                //모듈 직접 접근 http://15.165.116.82:1234
                //http://15.165.116.82:8080/api/value/ REST API 로 데이터 전송
                retrofitConnection.setRetrofit("http://15.165.116.82:8080/");

                ApiData jsonData = new ApiData();

                jsonData.setLandmarks(null);
                jsonData.setRect(null);
                jsonData.setDriver(false);
                jsonData.setCorrect(true);

                Call call = retrofitConnection.getServer().sendData(jsonData);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){
                            Log.e("피드백 전송 성공", "jsonData : " + jsonData.getCorrect());
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("피드백 전송 실패", "jsonData : " + jsonData.getCorrect());
                    }
                });

                alramStop();
                vibrator.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public RetrofitConnection getRetrofitConnection(){
        return retrofitConnection;
    }

    public void setRetrofitConnection(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
    }

}
