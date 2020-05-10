package com.tzutalin.dlibtest.Utility;

import android.os.Handler;
import android.util.Log;

import com.tzutalin.dlibtest.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SleepStepManager {
    Boolean isLooping;
    RetrofitConnection retrofitConnection;
    String TAG = "SleepStepManager";
    Handler sleepStepTimerHandler;
    Runnable runnable;

    public SleepStepManager(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
        retrofitConnection.setRetrofit("http://15.165.116.82:8080/");

        runnable = new Runnable() {

            @Override
            public void run() {
                Call call = retrofitConnection.getServer().dropSleepStep();
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.isSuccessful()){
                            Log.e(TAG,"sleepStep drop");
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        };
    }

    public void resetSleepStep(){
        Call call = retrofitConnection.getServer().resetSleepStep();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Log.e(TAG,"Sleep Step Reset Success");
                }
                else{
                    Log.e(TAG,"Sleep Step Reset Fail");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "Sleep Step Reset Fail // Network Error");
            }
        });
    }


    public void startTimer(){
        if(sleepStepTimerHandler == null){
            sleepStepTimerHandler = new Handler();
            sleepStepTimerHandler.postDelayed(runnable, 20000);
        }
        else{
            Log.e(TAG,"Already playing Thread");
        }
    }


    public void resetHandler(){
        try {
            if (sleepStepTimerHandler != null) {
                sleepStepTimerHandler.removeCallbacks(runnable);
                sleepStepTimerHandler = null; // 자원반환
            }
            if(isLooping){
                startTimer();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
