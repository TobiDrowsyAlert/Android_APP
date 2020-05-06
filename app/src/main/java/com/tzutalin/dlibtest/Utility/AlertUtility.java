package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class AlertUtility {

    Vibrator vibrator;

    Context mContext;

    public AlertUtility(){
        Vibrator vib = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }


    void vibrate(int time){
        vibrator.vibrate(time);
    }

    void alert(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        
    }

}
