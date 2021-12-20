package com.example.one_hotel.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User
{
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("urlfoto")
    @Expose
    private String urlfoto;

    public User(String email, String fullname, String username, String password, String urlfoto)
    {
        this.email = email;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.urlfoto = urlfoto;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public String getPassword() {
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
}
