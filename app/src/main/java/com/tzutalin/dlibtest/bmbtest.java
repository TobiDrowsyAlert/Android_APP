package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;

public class bmbtest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        BoomMenuButton bmb = (BoomMenuButton)findViewById(R.id.bmb);
        //Button btn_menu_1 = (Button) findViewById(R.id.bmb);
//        Button btn_menu_2 =(Button)findViewById(R.id.menu_2);
        //Button btn_menu_3 = (Button) findViewById(R.id.menu_3);
//        Button btn_menu_4 =(Button)findViewById(R.id.menu_4);
//        Button btn_menu_5 =(Button)findViewById(R.id.menu_5);
        bmb.addBuilder(new SimpleCircleButton.Builder().normalImageRes(R.drawable.one));


        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if(index ==1)
                {
                    for(int i=0; i<bmb.getPiecePlaceEnum().pieceNumber(); i++)
                    {
                        SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                                .normalImageRes(R.drawable.one);
                        bmb.addBuilder(builder);
                    }
                }


            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        } );
        /*bmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<bmb.getPiecePlaceEnum().pieceNumber(); i++)
                {
                    SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                            .normalImageRes(R.drawable.one);
                    bmb.addBuilder(builder);
                }
            }
        });*/

        //bmb.setButtonEnum(ButtonEnum.SimpleCircle);



    }
}
