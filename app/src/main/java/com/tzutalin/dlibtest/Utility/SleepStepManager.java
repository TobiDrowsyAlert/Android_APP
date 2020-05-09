package com.tzutalin.dlibtest.Utility;

import android.util.Log;

import com.tzutalin.dlibtest.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SleepStepManager {
    Boolean isDroping;
    RetrofitConnection retrofitConnection;
    String TAG = "SleepStepManager";

    public SleepStepManager(RetrofitConnection retrofitConnection){
        this.retrofitConnection = retrofitConnection;
        retrofitConnection.setRetrofit("http://15.165.116.82:8080/");
    }

    public void resetSleepStep(){
        Call call = retrofitConnection.getServer().resetSleepStep();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Log.e(TAG,"Sleep Step Reset Success");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

}
