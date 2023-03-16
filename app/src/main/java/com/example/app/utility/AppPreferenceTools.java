package com.example.app.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.app.data.model.AuthenticationResponseModel;
import com.example.app.data.model.TokenModel;
import com.example.app.data.model.UserModel;

public class AppPreferenceTools {

    private SharedPreferences mPreference;
    private Context mContext;
    public static final String STRING_PREF_UNAVAILABLE = "string preference unavailable";

    public AppPreferenceTools(Context context) {
        this.mContext = context;
        this.mPreference = this.mContext.getSharedPreferences("app_preference", Context.MODE_PRIVATE);
    }

}
