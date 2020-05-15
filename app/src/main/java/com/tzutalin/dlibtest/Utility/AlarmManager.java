package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.tzutalin.dlibtest.CameraActivity;

public class AlarmManager {

    Context mContext;
    MediaPlayer mAudio = null;
    Vibrator vibrator;
    Uri alramSound;
    int StreamType = 0;
    int sleep_step;

    Boolean isPlay;

    Runnable alramRunnable;

    public AlarmManager(Context mContext){
        this.mContext = mContext;
        mAudio = new MediaPlayer();
        vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        alramSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        isPlay = false;

        try{
            mAudio.setDataSource(mContext, alramSound);
            mAudio.setAudioStreamType(StreamType);
            mAudio.setLooping(true);
            mAudio.prepare();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void vibrate(int sleep_step)
    {
        if(sleep_step == 2)
            vibrator.vibrate(60000);

        if(sleep_step == 3)
            vibrator.vibrate(90000);
    }


    public void alram(){
        mAudio.start();
        CameraActivity.setColor(2);
        isPlay = true;
    }


    public void alramStop(){

        if(mAudio.isPlaying()){
            mAudio.pause();
            isPlay = false;
            CameraActivity.setColor(1);
        }

    }

    private void setAlramRunnable(){
        alramRunnable = new Runnable() {
            @Override
            public void run() {

                vibrator.cancel();
            }
        };
    }

}
