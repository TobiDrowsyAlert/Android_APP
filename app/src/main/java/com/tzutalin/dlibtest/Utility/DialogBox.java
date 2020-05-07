package com.tzutalin.dlibtest.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.ApiData;
import com.tzutalin.dlibtest.RetrofitConnection;

public class DialogBox {

    Context mContext;
    AlertDialog.Builder builder;
    RetrofitConnection retrofitConnection;


    public DialogBox(Context mContext){
        this.mContext = mContext;
        builder = new AlertDialog.Builder(mContext);
    }


    public RetrofitConnection getRetrofitConnection(){
        return retrofitConnection;
    }

    public void setRetrofitConnection(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
    }

    public void feedbackDialog(String cause){
        builder.setTitle("졸음이 인식되었습니다.").setMessage(cause + "이 맞습니까?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                RetrofitConnection retrofitConnection = new RetrofitConnection();

                //AWS 스프링 공인 주소, http://15.165.116.82:8080
                //모듈 직접 접근 http://15.165.116.82:1234
                //http://15.165.116.82:8080/api/value/ REST API 로 데이터 전송
                retrofitConnection.setRetrofit("http://15.165.116.82:8080/");

                ApiData jsonData = new ApiData();

                jsonData.setLandmarks(null);
                jsonData.setRect(null);
                jsonData.setDriver(false);
                jsonData.setCorrect(true);

                retrofitConnection.getServer().sendData(jsonData);
                Log.e("잘못된 피드백" , "전달된 데이터 : " + jsonData);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}