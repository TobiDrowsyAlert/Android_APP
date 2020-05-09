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

    @POST("/api/sleepStep/drop")
    Call<ResponseLandmark> dropSleepStep();

    @POST("/api/sleepStep/reset")
    Call<ResponseLandmark> resetSleepStep();

    @POST("/api/sleepStep/feedback")
    Call<ResponseLandmark> feedback();

/*    @FormUrlEncoded
    @POST("set_face")
    Call<ApiData> sendData2(@Field("name") String name, @Field("age") int age);*/


}