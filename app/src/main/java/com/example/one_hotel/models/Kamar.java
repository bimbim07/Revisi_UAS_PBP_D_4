package com.example.one_hotel.models;

public class Kamar
{
    private Long id;
    private Long id_hotel;
    private String nama;
    private String jenis;
    private double harga;
    private String deskripsi;
    private String urlfoto;

    public Kamar(Long id_hotel, String nama, String jenis, double harga, String deskripsi, String urlfoto)
    {
        this.id_hotel = id_hotel;
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.deskripsi = deskripsi;
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

    public Long getId_hotel()
    {
        return id_hotel;
    }

    public void setId_hotel(Long id_hotel)
    {
        this.id_hotel = id_hotel;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getJenis()
    {
        return jenis;
    }

    public void setJenis(String jenis)
    {
        this.jenis = jenis;
    }

    public double getHarga()
    {
        return harga;
    }

    public void setHarga(double harga)
    {
        this.harga = harga;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
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