package com.tzutalin.dlibtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
import com.tzutalin.dlibtest.domain.RequestRegisterDTO;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;
import com.tzutalin.dlibtest.user.domain.ResponseLoginDTO;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {

    final String TAG = "RetrofitConnection";

    static Boolean isSuccess = false;

    Retrofit retrofit;
    RetrofitInterface server;

    public RetrofitConnection(){
        setRetrofit("http://15.165.116.82:8080");
    }

    public RetrofitConnection(String url){
        setRetrofit(url);
    }

    public RetrofitInterface getServer() {
        return server;
    }

    public void setServer(RetrofitInterface server) {
        this.server = server;
    }

    public void setRetrofit(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        server = retrofit.create(RetrofitInterface.class);

    }

    public void requestRegitser(RequestRegisterDTO requestRegisterDTO){
        Call<ResponseLoginDTO> call = this.getServer().register(requestRegisterDTO);
        call.enqueue(new Callback<ResponseLoginDTO>() {
            @Override
            public void onResponse(Call<ResponseLoginDTO> call, Response<ResponseLoginDTO> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.getContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.getContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    Log.e("requestLogin", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.getContext(), "회원가입 실패 // 네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("requestRegitser", t.getMessage());
            }
        });
    }

    public Boolean requestLogin(RequestLoginDTO requestLoginDTO){

        Call<ResponseLandmarkDTO> call = this.getServer().login(requestLoginDTO);
        call.enqueue(new Callback<ResponseLandmarkDTO>() {
            @Override
            public void onResponse(Call<ResponseLandmarkDTO> call, Response<ResponseLandmarkDTO> response) {

                SharedPreferences settingPreferences = LoginActivity.getContext().getSharedPreferences("settingPreferences", LoginActivity.getContext().MODE_PRIVATE);

                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.getContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.getContext(), menuEvent.class);
                    LoginActivity.getContext().startActivity(intent);
                    isSuccess = true;
                }
                else{
                    Toast.makeText(LoginActivity.getContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.e("requestLogin", response.message());
                    isSuccess = false;
                }

            }

            @Override
            public void onFailure(Call<ResponseLandmarkDTO> call, Throwable t) {
                Toast.makeText(LoginActivity.getContext(), "로그인 실패 // 네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("requestLogin", t.getMessage());
                isSuccess = false;
            }
        });

        return isSuccess;
    }



}