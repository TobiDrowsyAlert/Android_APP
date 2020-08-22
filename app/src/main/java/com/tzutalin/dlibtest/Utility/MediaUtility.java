package com.tzutalin.dlibtest.Utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.Printer;
import android.widget.Toast;

import com.tzutalin.dlibtest.CameraActivity;
import com.tzutalin.dlibtest.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MediaUtility {
    public final static int INT_START = 6;
    public final static int INT_WARNING_REPLY = 7;
    public final static int INT_CORRECT_REPLY = 11;
    public final static int INT_INCORRECT_REPLY = 12;
    private static MediaUtility mediaUtility = new MediaUtility();
    private static Context mContext;
    int no;
    Vector<Integer> soundVector;
    String soundName[] = {"빠른음악을틀자","오늘하루도고생하셨습니다","오늘하루도고생했어",
            "졸릴땐스트레칭을하세요","졸릴땐환기를시키자","지금졸면내일은없다"};

    MediaPlayer mediaPlayer;
    File file;

    private MediaUtility(){
        mediaPlayer = new MediaPlayer();
        soundVector = new Vector<>();
        soundVector.add(R.raw.sound1);
        soundVector.add(R.raw.sound2);
        soundVector.add(R.raw.sound3);
        soundVector.add(R.raw.sound4);
        soundVector.add(R.raw.sound5);
        soundVector.add(R.raw.sound6);
        soundVector.add(R.raw.sound0); // 취약시간입니다 따라 말해주세요 6
        soundVector.add(R.raw.sound7);  // 취약시간입니다. 주의해주세요. 7
        soundVector.add(R.raw.replay);  // 다시시도해주세요.   8
        soundVector.add(R.raw.leftsound);  // 왼쪽으로 스트레칭해주세요  9
        soundVector.add(R.raw.rightsound);  // 오른쪽으로 스트레칭해주세요  10
        soundVector.add(R.raw.ok_sound);  // 맞았습니다. 11
        soundVector.add(R.raw.no_sound);  // 틀렸습니다  12
    }

    public static MediaUtility getInstance(Context context){
        mContext = context.getApplicationContext();
        return mediaUtility;
    }

    public int randomStart(){
        int random = (int) (Math.random() * 6);
        mediaPlayer = MediaPlayer.create(mContext, soundVector.get(random));

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    // 여기서 음성인식이 시작되어야 한다.
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    CameraActivity.startSpeech(random);
                }
            });
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        return random;
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

    public void startSound(int i){
        mediaPlayer = MediaPlayer.create(mContext, soundVector.get(i));

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(INT_START == i){
                        randomStart();
                    }
                }
            });
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }



    public Boolean isMeaningCorrect(String str, int select){
        int cnt = 0;
        str = str.replaceAll(" ", "");
        String answer = soundName[select];
        Log.e("isMeaningCorrect", "select : " + select);
        for(int i = 0; i < str.length() - 1; i++){
            if(answer.charAt(i) == str.charAt(i)){
                cnt++;
            }
        }

        return (str.length() / 2) <= cnt;
    }

}
