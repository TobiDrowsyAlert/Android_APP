package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.tzutalin.dlibtest.CameraActivity;

public class AlarmManager {

    private final int INT_VIBRATE_TIME = 30000;

    private Context mContext;
    private MediaPlayer mAudio = null;
    private Vibrator vibrator;
    private Uri alramSound;
    private int StreamType = 0;
    private int sleep_step;

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
       vibrator.vibrate(sleep_step * INT_VIBRATE_TIME);
    }

    public void vibrateCancel(){ vibrator.cancel(); }


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
                mAudio.pause();
                vibrator.cancel();
            }
        };
    }

    public Runnable getAlarmRunnable(){
        return alramRunnable;
    }

    public Vibrator getVibrator(){
        return vibrator;
    }

    public MediaPlayer getMAudio(){
        return mAudio;
    }
}
