package com.example.app.network;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.AppContext;
import com.example.app.ui.login.LoginActivity;
import com.example.app.utility.TinyDB;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpsClient {
    public Response doRegister(String username, String password, String email) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("email", email)
                .build();
        Request request = new Request.Builder()
                .url("http://143.42.66.73:9090/public/api/register.php")
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response doGetInfo(String token, String username) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://143.42.66.73:9090/api/user/read.php?view=single&username=tester123")
                    .method("GET", body)
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUSEVfQ0xBSU0iLCJhdWQiOiJUSEVfQVVESUVOQ0UiLCJpYXQiOjE2NzkwMjA5OTYsIm5iZiI6MTY3OTAyMDk5NiwiZXhwIjoxNjc5MDIxODk2LCJkYXRhIjp7InVzZXJuYW1lIjoiVEVTVEVSMTIzIiwicm9sZSI6MCwidXNlckVtYWlsIjoidGVzdEBnbWFpbC5jb20ifX0.sCZMdx4JYARYA0UrEvQ1B7RZoNbd5muBukdpJCQ9q26zVqOF5sMpVxfzFqMThfF8rhDlkwmji4zkObO5MaYumw")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return response;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


//                val resp = response.body?.string()
//                val token = resp.toString().split(",")[1].split(":")[1].replace("\"", "").replace("}", "")
//                val authenToken = "Bearer $token"
//                println(authenToken)
//                val userResp = client.doGetInfo(authenToken, username).body?.string()

}
