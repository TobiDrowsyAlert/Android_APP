package com.tzutalin.dlibtest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

/*    @GET("set_face/")
    Call<ApiData> sendData();*/

    @POST("/api/value")
    Call<ResponseLandmark> sendData(@Body ApiData landmarks);

    @POST("/api/drop")
    Call<ResponseLandmark> dropSleepStep();

    @POST("/api/reset")
    Call<ResponseLandmark> resetSleepStep();

    @POST("/api/feedback")
    Call<ResponseLandmark> feedback();

    @POST("/api/timer")
    Call<ResponseLandmark> timer();

/*    @FormUrlEncoded
    @POST("set_face")
    Call<ApiData> sendData2(@Field("name") String name, @Field("age") int age);*/


}