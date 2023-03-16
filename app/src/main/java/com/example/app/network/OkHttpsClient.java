package com.example.app.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpsClient {

    public Response doLogin(String username, String password) {
        try {
            URL url = new URL("http://143.42.66.73:9090/public/api/login.php");
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .build();
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//                val resp = response.body?.string()
//                val token = resp.toString().split(",")[1].split(":")[1].replace("\"", "").replace("}", "")
//                val authenToken = "Bearer $token"
//                println(authenToken)
//                val userResp = client.doGetInfo(authenToken, username).body?.string()

}
