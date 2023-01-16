package com.example.family_kitchen.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.family_kitchen.constants.Constants;


public class SharedPref {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLoginState(String state){
        editor.putString(Constants.PREF_LOGIN_STATE, state);
        editor.commit();
    }
    public String getLoginState(){
        return pref.getString(Constants.PREF_LOGIN_STATE, "");
    }

    public void setEmail(String email){
        editor.putString(Constants.PREF_EMAIL, email);
        editor.commit();
    }

    public void setUserName(String userName){
        editor.putString(Constants.PREF_USER_NAME, userName);
        editor.commit();
    }

    public void setRememberMe(Boolean value){
        editor.putBoolean(Constants.PREF_REMEMBER_ME, value);
        editor.commit();
    }
    public Boolean getRememberMe(){
        return pref.getBoolean(Constants.PREF_REMEMBER_ME, false);
    }


    public String getEmail(){
        return pref.getString(Constants.PREF_EMAIL, "");
    }

    public String getUserName(){
        return pref.getString(Constants.PREF_USER_NAME, "");
    }





}
