package com.tzutalin.dlibtest;

import com.tzutalin.dlibtest.domain.RequestAnalyzeSleepDTO;
        import com.tzutalin.dlibtest.domain.RequestFeedbackDTO;
        import com.tzutalin.dlibtest.domain.RequestRegisterDTO;
        import com.tzutalin.dlibtest.domain.ResponseLandmarkDTO;
import com.tzutalin.dlibtest.domain.StrechDataDTO;
import com.tzutalin.dlibtest.user.domain.ResponseLoginDTO;
        import com.tzutalin.dlibtest.user.domain.RequestLoginDTO;
        import com.tzutalin.dlibtest.user.model.UserDTO;

        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.Headers;
        import retrofit2.http.POST;

public interface RetrofitInterface {

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
    Call<ResponseLandmarkDTO> login(@Body RequestLoginDTO requestLoginDTO);

    @POST("/user/register")
    Call<ResponseLoginDTO> register(@Body RequestRegisterDTO requestRegisterDTO);

    @POST("/user/logout")
    Call<ResponseLoginDTO> logout(@Body UserDTO userDTO);

    @POST("/api/stretch")
    Call<StrechDataDTO> strectch(@Body RequestAnalyzeSleepDTO landmarks);

}
