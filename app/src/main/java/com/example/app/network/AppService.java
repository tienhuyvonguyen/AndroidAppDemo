package com.example.app.network;

import com.example.app.data.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppService {
    @POST("public/api/login.php")
    Call<UserModel> doLogin(@Body String username, @Body String password);
}
