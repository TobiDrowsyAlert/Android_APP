package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Printer;
import android.widget.Toast;

import com.tzutalin.dlibtest.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MediaUtility {
    private static MediaUtility mediaUtility = new MediaUtility();
    private static Context mContext;
    int no;
    Vector<Integer> soundVector;
    String soundName[] = {"취약시간입니다따라말해주세요","빠른음악을틀자","오늘하루도고생하셨습니다","오늘하루도고생했어",
            "졸릴땐스트레칭을하세요","졸릴땐환기를시키자","지금졸면내일은없다"};

    MediaPlayer mediaPlayer;
    File file;

    private MediaUtility(){
        mediaPlayer = new MediaPlayer();
        soundVector = new Vector<>();
        soundVector.add(R.raw.sound0);
        soundVector.add(R.raw.sound1);
        soundVector.add(R.raw.sound2);
        soundVector.add(R.raw.sound3);
        soundVector.add(R.raw.sound4);
        soundVector.add(R.raw.sound5);
        soundVector.add(R.raw.sound6);
        soundVector.add(R.raw.sound7);  // 취약시간입니다. 조심해주세요.
        soundVector.add(R.raw.replay);  // 다시시도해주세요.   num 8
        soundVector.add(R.raw.leftsound);  // 왼쪽으로 스트레칭해주세요  num 9
        soundVector.add(R.raw.rightsound);  // 오른쪽으로 스트레칭해주세요  num 10
    }

    public static MediaUtility getInstance(Context context){
        mContext = context.getApplicationContext();
        return mediaUtility;
    }

    public void randomStart(){
        int random = (int) (Math.random() * soundVector.size());
        mediaPlayer = MediaPlayer.create(mContext, soundVector.get(random));
        Toast.makeText(mContext, soundName[random], Toast.LENGTH_SHORT).show();

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            });
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    public void leftRightSound(Boolean direction){
        if(direction)  // true 면 왼쪽으로 스트레칭해주세요
        {
            mediaPlayer = MediaPlayer.create(mContext, soundVector.get(9));
        }
        else {  // false 면 오른쪽으로 스트레칭해주세요
            mediaPlayer = MediaPlayer.create(mContext, soundVector.get(10));
        }

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            });
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void warningSound(){
        mediaPlayer = MediaPlayer.create(mContext, soundVector.get(0));

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            });
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
