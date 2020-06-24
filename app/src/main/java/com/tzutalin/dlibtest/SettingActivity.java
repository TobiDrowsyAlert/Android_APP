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
    private final int DEFAULT_STEP_THREE_TIME = 20;
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

        TextView tv_1 = findViewById(R.id.step_1_sec);
        TextView tv_2 = findViewById(R.id.step_2_sec);
        TextView tv_3 = findViewById(R.id.step_3_sec);
        TextView tv_4 = findViewById(R.id.sound_value);

        int currentStepOneTime = settingPreferences.getInt("stepOne", DEFAULT_STEP_ONE_TIME);
        int currentStepTwoTime = settingPreferences.getInt("stepTwo", DEFAULT_STEP_TWO_TIME);
        int currentStepThreeTime = settingPreferences.getInt("stepThree", DEFAULT_STEP_THREE_TIME);
        int currentAlramVolume = settingPreferences.getInt("soundBar", DEFAULT_ALRAM_VOLUME);

        step_1.setProgress(currentStepOneTime);
        step_2.setProgress(currentStepTwoTime);
        step_3.setProgress(currentStepThreeTime);
        sound_bar.setProgress(currentAlramVolume);

        tv_1.setText(String.format("%s/sec", getTimeText(currentStepOneTime,
                DEFAULT_STEP_ONE_TIME,DEFAULT_STEP_ONE_TIME)));
        tv_2.setText(String.format("%s/sec", getTimeText(currentStepTwoTime,
                DEFAULT_STEP_TWO_TIME,DEFAULT_STEP_TWO_TIME)));
        tv_3.setText(String.format("%s/sec", getTimeText(currentStepThreeTime,
                DEFAULT_STEP_THREE_TIME,DEFAULT_STEP_THREE_TIME)));
        tv_4.setText(String.format("%s", currentAlramVolume));


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

               Intent intent = new Intent(SettingActivity.this , menuEvent.class);
               startActivity(intent);
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
                editor.putFloat("stepOneTime", (float)getTimeText(step_1.getProgress(),
                        DEFAULT_STEP_ONE_TIME,DEFAULT_STEP_ONE_TIME));
                editor.putFloat("stepTwoTime", (float)getTimeText(step_2.getProgress(),
                        DEFAULT_STEP_TWO_TIME,DEFAULT_STEP_TWO_TIME));
                editor.putFloat("stepThreeTime", (float)getTimeText(step_3.getProgress(),
                        DEFAULT_STEP_THREE_TIME,DEFAULT_STEP_THREE_TIME));

                editor.commit();

                Intent intent = new Intent(SettingActivity.this , menuEvent.class);
                startActivity(intent);
            }
        });

        step_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
