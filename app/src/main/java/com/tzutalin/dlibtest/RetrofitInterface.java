package com.tzutalin.dlibtest;

import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
import com.tzutalin.dlibtest.domain.RequestFeedbackDTO;
import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.user.domain.ResponseLoginDTO;
import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;
import com.tzutalin.dlibtest.user.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

/*    @GET("set_face/")
    Call<ApiData> sendData();*/

    @POST("/api/value")
    Call<ResponseLandmarkDTO> sendData(@Body RequestAnalyzeSleepDTO landmarks);

    @POST("/api/drop")
    Call<ResponseLandmarkDTO> dropSleepStep(@Body UserDTO userDTO);

    @POST("/api/reset")
    Call<ResponseLandmarkDTO> resetSleepStep(@Body UserDTO userDTO);

    @POST("/api/feedback")
    Call<ResponseLandmarkDTO> feedback(@Body RequestFeedbackDTO responseFeedback);

    @POST("/api/timer")
    Call<ResponseLandmarkDTO> timer(@Body UserDTO userDTO);

    @POST("/user/login")
    Call<ResponseLoginDTO> login(@Body RequestLoginDTO requestLoginDTO);




}