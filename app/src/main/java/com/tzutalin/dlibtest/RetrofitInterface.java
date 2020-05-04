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
    Call<ApiData> sendData(@Body ApiData landmarks);

/*    @FormUrlEncoded
    @POST("set_face")
    Call<ApiData> sendData2(@Field("name") String name, @Field("age") int age);*/


}