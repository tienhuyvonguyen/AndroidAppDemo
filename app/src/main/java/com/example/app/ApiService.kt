package com.example.app

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {
    @Multipart
    @POST("uploadAvatar.php")
    fun uploadFile(@Part file: Part?, @Part("file") name: RequestBody?): Call<*>?


}