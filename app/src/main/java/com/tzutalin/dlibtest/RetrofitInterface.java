package com.tzutalin.dlibtest;

import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
import com.tzutalin.dlibtest.domain.ResponseFeedbackDTO;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.domain.ResponseLoginDTO;
import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

/*    @GET("set_face/")
    Call<ApiData> sendData();*/

    @POST("/api/value")
    Call<ResponseLandmarkDTO> sendData(@Body RequestAnalyzeSleepDTO landmarks);

    @POST("/api/drop")
    Call<ResponseLandmarkDTO> dropSleepStep();

    @POST("/api/reset")
    Call<ResponseLandmarkDTO> resetSleepStep();

    @POST("/api/feedback")
    Call<ResponseLandmarkDTO> feedback(@Body ResponseFeedbackDTO responseFeedback);

    @POST("/api/timer")
    Call<ResponseLandmarkDTO> timer();

    @POST("/user/login")
    Call<ResponseLoginDTO> login(@Body RequestLoginDTO requestLoginDTO);




}