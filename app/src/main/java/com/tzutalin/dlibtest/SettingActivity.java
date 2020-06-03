package com.tzutalin.dlibtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    static private SharedPreferences settingPreferences;
    private final int DEFAULT_STEP_ONE_TIME = 5;
    private final int DEFAULT_STEP_TWO_TIME = 10;
    private final int DEFAULT_STEP_THREE_TIME = 15;
    private final int DEFAULT_ALRAM_VOLUME = 50;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Button btn_reset;   //초기화 버튼
        Button btn_OK;      //변경 확인 버튼
        SeekBar step_1;     //스탭1
        SeekBar step_2;     //스탭2
        SeekBar step_3;     //스탭3
        SeekBar sound_bar;  // 사운드바

        settingPreferences = getSharedPreferences("settingPreferences", MODE_PRIVATE);

        btn_reset = findViewById(R.id.button9);
        btn_OK = findViewById(R.id.button10);
        step_1 = findViewById(R.id.step_1_seek);
        step_2 = findViewById(R.id.step_2_seek);
        step_3 = findViewById(R.id.step_3_seek);
        sound_bar = findViewById(R.id.sound_seek);

        int currentStepOneTime = settingPreferences.getInt("stepOne", DEFAULT_STEP_ONE_TIME);
        int currentStepTwoTime = settingPreferences.getInt("stepTwo", DEFAULT_STEP_TWO_TIME);
        int currentStepThreeTime = settingPreferences.getInt("stepThree", DEFAULT_STEP_THREE_TIME);
        int currentAlramVolume = settingPreferences.getInt("soundBar", DEFAULT_ALRAM_VOLUME);

        step_1.setProgress(currentStepOneTime);
        step_2.setProgress(currentStepTwoTime);
        step_3.setProgress(currentStepThreeTime);
        sound_bar.setProgress(currentAlramVolume);


        btn_reset.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v)
           {
               SharedPreferences.Editor editor = settingPreferences.edit();
               editor.putInt("stepOne", DEFAULT_STEP_ONE_TIME);
               editor.putInt("stepTwo", DEFAULT_STEP_TWO_TIME);
               editor.putInt("stepThree", DEFAULT_STEP_THREE_TIME);
               editor.putInt("soundBar", DEFAULT_ALRAM_VOLUME);

               editor.commit();
           }
        });

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = settingPreferences.edit();
                editor.putInt("stepOne", step_1.getProgress());
                editor.putInt("stepTwo", step_2.getProgress());
                editor.putInt("stepThree", step_3.getProgress());
                editor.putInt("soundBar", sound_bar.getProgress());

                editor.commit();
            }
        });

        step_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv_1 = findViewById(R.id.step_1_sec);
                tv_1.setText(String.format("%s/sec", getTimeText(progress,
                        DEFAULT_STEP_ONE_TIME,DEFAULT_STEP_ONE_TIME)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seeBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seeBar) {

            }
        });
        step_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv_2 = findViewById(R.id.step_2_sec);
                tv_2.setText(String.format("%s/sec", getTimeText(progress,
                        DEFAULT_STEP_TWO_TIME,DEFAULT_STEP_TWO_TIME)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seeBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seeBar) {

            }
        });

        step_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv_3 = findViewById(R.id.step_3_sec);
                tv_3.setText(String.format("%s/sec", getTimeText(progress,
                        DEFAULT_STEP_THREE_TIME,DEFAULT_STEP_THREE_TIME)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seeBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seeBar) {

            }
        });

        sound_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv_4 = findViewById(R.id.sound_value);
                tv_4.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seeBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seeBar) {

            }
        });
    }

    private double getTimeText(int progress, int defaultTime, int differentTime){
        double value = defaultTime + (progress / 100.0) * differentTime;
        return value = Math.round((value * 100)/100.0);
    }
}
