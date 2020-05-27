package com.tzutalin.dlibtest;


import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class bmbtest extends AppCompatActivity
{

    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion_basic2);
        /*
        final RippleView rippleView = (RippleView) findViewById(R.id.ripple);
        
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
            }
        });*/

    }
}
