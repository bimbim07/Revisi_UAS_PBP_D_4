package com.example.one_hotel.response;

import com.example.one_hotel.models.Hotel;
import com.example.one_hotel.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelResponse 
{
    private String message;

    @SerializedName("hotel")
    private List<Hotel> hotelList;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Hotel> getHotelList()
    {
        return hotelList;
    }

    public void setHotelList(List<Hotel> hotelList)
    {
        this.hotelList = hotelList;
    }
}
