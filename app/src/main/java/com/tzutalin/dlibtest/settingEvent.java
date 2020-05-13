package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class settingEvent extends AppCompatActivity {

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

        btn_reset = (Button)findViewById(R.id.button9);
        btn_OK = (Button)findViewById(R.id.button10);
        step_1 = (SeekBar)findViewById(R.id.step_1_seek);
        step_2 = (SeekBar)findViewById(R.id.step_2_seek);
        step_3 = (SeekBar)findViewById(R.id.step_3_seek);
        sound_bar = (SeekBar)findViewById(R.id.sound_seek);


        btn_reset.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v)
           {

           }
        });

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        step_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv_1 = (TextView)findViewById(R.id.step_1_sec);
                tv_1.setText(String.valueOf(progress));
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
                TextView tv_2 = (TextView)findViewById(R.id.step_2_sec);
                tv_2.setText(String.valueOf(progress));
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
                TextView tv_3 = (TextView)findViewById(R.id.step_3_sec);
                tv_3.setText(String.valueOf(progress));
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
                TextView tv_4 = (TextView)findViewById(R.id.sound_value);
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
}
