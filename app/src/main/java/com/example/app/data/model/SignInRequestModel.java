package com.example.app.data.model;

import com.example.app.utility.ClientConfigs;

public class SignInRequestModel {
    public String username;
    public String password;
    public String client_id;
    public String client_key;

    public SignInRequestModel() {
        this.client_id = ClientConfigs.CLIENT_ID;
        this.client_key = ClientConfigs.CLIENT_KEY;
    }
}
