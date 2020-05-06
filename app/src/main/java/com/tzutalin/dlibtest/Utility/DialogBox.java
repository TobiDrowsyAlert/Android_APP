package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class DialogBox {

    Context mContext;
    AlertDialog.Builder builder;

    public DialogBox(Context mContext){
        this.mContext = mContext;
        builder = new AlertDialog.Builder(mContext);
    }

    public void feedbackDialog(String cause){
        builder.setTitle("졸음이 인식되었습니다.").setMessage(cause + "이 맞습니까?");
        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO Click", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "OK Click", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}