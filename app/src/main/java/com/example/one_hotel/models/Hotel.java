package com.example.one_hotel.models;

public class Hotel
{
    private Long id;
    private String nama;
    private double rating;
    private String lokasi;
    private String urlfoto;

    public Hotel(String nama, double rating, String lokasi, String urlfoto)
    {
        this.nama = nama;
        this.rating = rating;
        this.lokasi = lokasi;
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

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    public String getLokasi()
    {
        return lokasi;
    }

    public void setLokasi(String lokasi)
    {
        this.lokasi = lokasi;
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