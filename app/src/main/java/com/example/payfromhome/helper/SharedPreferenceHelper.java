package com.example.payfromhome.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static final String PREFERENCE_NAME = "pay_from_home_pref";
    public static final String IS_LOGIN = "is_login";
    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String BALANCE = "balance";

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSessionEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getSessionEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }
    public void setSessionId(String id) {
        editor.putString(ID, id);
        editor.commit();
    }

    public String getSessionId() {
        return sharedPreferences.getString(ID, "");
    }

    public void setSessionBalance(String balance) {
        editor.putString(BALANCE, balance);
        editor.commit();
    }

    public String getSessionBalance() {
        return sharedPreferences.getString(BALANCE, "0");
    }

    public void setIsLogin(boolean isLogin) {
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
