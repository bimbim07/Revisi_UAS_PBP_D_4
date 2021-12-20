package com.example.one_hotel.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.one_hotel.models.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePreferences 
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String IS_Profile = "isProfile";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_URLFOTO = "urlfoto";
    public static final String KEY_TOKEN = "token";

    public ProfilePreferences(Context context)
    {
        this.context = context;

        preferences = context.getSharedPreferences("ProfilePreferences",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setProfile(String id, String email, String fullname, String username, String password, String urlfoto, String token)
    {
        editor.putBoolean(IS_Profile,true);
        editor.putString(KEY_ID,id);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_FULLNAME,fullname);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_URLFOTO,urlfoto);
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    public Profile getProfile()
    {
        String id;
        String email;
        String fullname;
        String username;
        String password;
        String urlfoto;
        String token;

        id = preferences.getString(KEY_ID,null);
        email = preferences.getString(KEY_EMAIL,null);
        fullname = preferences.getString(KEY_FULLNAME,null);
        username = preferences.getString(KEY_USERNAME,null);
        password = preferences.getString(KEY_PASSWORD,null);
        urlfoto = preferences.getString(KEY_URLFOTO,null);
        token = preferences.getString(KEY_TOKEN,null);

        return new Profile(id,email,fullname,username,password,urlfoto,token);
    }

    public boolean checkProfile()
    {
        return preferences.getBoolean(IS_Profile,false);
    }

    public void deleteProfile()
    {
        editor.clear();
        editor.commit();
    }
}