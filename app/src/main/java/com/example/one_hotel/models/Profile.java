package com.example.one_hotel.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile
{
    private String id;
    private String email;
    private String fullname;
    private String username;
    private String password;
    private String urlfoto;
    private String token;

    public Profile(String id, String email, String fullname, String username, String password, String urlfoto, String token)
    {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.urlfoto = urlfoto;
        this.token = token;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUrlfoto()
    {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto)
    {
        this.urlfoto = urlfoto;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
