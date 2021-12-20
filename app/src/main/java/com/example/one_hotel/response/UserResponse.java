package com.example.one_hotel.response;

import com.example.one_hotel.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse 
{
    @SerializedName("message") //INI TIPE DATA SENDIRI MAKANYA NAMA SERIALIZED HARUS SAMA SESUAI BACK END DI CONTROLLERNYA
    @Expose
    private String message;

    @SerializedName("userList") //INI TIPE DATA LIST NAMA SERIALIZED BOLEH BEBAS, DISINI SAYA MENGGUNAKAN NAMA YANG SAMA SESUAI VARIABEL
    @Expose
    private List<User> userList;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("token_type") //INI TIPE DATA SENDIRI MAKANYA NAMA SERIALIZED HARUS SAMA SESUAI BACK END DI CONTROLLERNYA
    @Expose
    private String tokenType;

    @SerializedName("access_token") //INI TIPE DATA SENDIRI MAKANYA NAMA SERIALIZED HARUS SAMA SESUAI BACK END DI CONTROLLERNYA
    @Expose
    private String accessToken;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<User> getUserList()
    {
        return userList;
    }

    public void setUserList(List<User> userList)
    {
        this.userList = userList;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getTokenType()
    {
        return tokenType;
    }

    public void setTokenType(String tokenType)
    {
        this.tokenType = tokenType;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
}
