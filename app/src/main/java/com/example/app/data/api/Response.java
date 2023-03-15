package com.example.app.data.api;

import com.google.gson.annotations.SerializedName;

public final class Response {
    @SerializedName("success")
    private String status;
    @SerializedName("token")
    private String token;
}
