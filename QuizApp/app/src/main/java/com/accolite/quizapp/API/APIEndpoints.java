package com.accolite.quizapp.API;

import com.accolite.quizapp.model.LoginResponse;
import com.accolite.quizapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Kanika Gupta on 20-10-2016.
 */
public interface APIEndpoints {

    @POST("users/login")
    public Call<LoginResponse> login(@Header("Authorization") String authorization,
                                     @Header("Content-Type") String contentType, @Body User user);

}
