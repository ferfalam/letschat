package com.faridcodeur.letschat;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private static AppPreference appPreference;
    SharedPreferences preferences;
    private AppPreference (){

    }

    public static AppPreference getInstance(Context context){
        if(appPreference==null) {
            appPreference = new AppPreference();
            appPreference.preferences = context.getSharedPreferences("MonProjet", Context.MODE_PRIVATE);
        }
        return appPreference;
    }

    public boolean isConnected(){
        return preferences.getBoolean("CONNECTED", false);
    }

    public void setConnected(boolean connect){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("CONNECTED", connect);
        editor.apply();
    }

    public void setUserId(String uid) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ID", uid);
        editor.apply();
    }

    public String getUserId() {
        return preferences.getString("USER_ID", "");
    }

    public void setUserName(String username) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_NAME", username);
        editor.apply();
    }

    public String getUserName() {
        return preferences.getString("USER_NAME", "");
    }
}
