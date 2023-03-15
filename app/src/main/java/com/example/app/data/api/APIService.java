package com.example.app.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public class APIService {

    @POST("/public/api/login.php")
    @
    Call<Response> login(@Field("username") String username, @Field("password") String password) {
        return null;
    }
}
