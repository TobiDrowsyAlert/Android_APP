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

public class AlertUtility {

    Vibrator vibrator;
    Ringtone ringtone;
    Context mContext;
    Uri alramSound;

    AlertDialog.Builder builder;

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
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO Click", Toast.LENGTH_SHORT).show();
                alramStop();
                vibrator.cancel();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "OK Click", Toast.LENGTH_SHORT).show();
                alramStop();
                vibrator.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
