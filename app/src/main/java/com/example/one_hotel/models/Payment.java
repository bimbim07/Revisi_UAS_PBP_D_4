package com.example.one_hotel.models;

public class Payment
{
    //HOTEL
    private String idHotel;
    private String namaHotel;
    private String ratingHotel;
    private String lokasiHotel;
    private String urlfotoHotel;

    //KAMAR
    private String idKamar;
    private String namaKamar;
    private String jenisKamar;
    private String hargaKamar;
    private String deskripsiKamar;
    private String urlfotoKamar;

    //ORDER
    private String checkIn;
    private String checkOut;
    private String durasi;
    private String jumlahKamar;
    private String total;

    public Payment(String idHotel, String namaHotel, String ratingHotel, String lokasiHotel, String urlfotoHotel, String idKamar, String namaKamar, String jenisKamar, String hargaKamar, String deskripsiKamar, String urlfotoKamar, String checkIn, String checkOut, String durasi, String jumlahKamar, String total)
    {
        this.idHotel = idHotel;
        this.namaHotel = namaHotel;
        this.ratingHotel = ratingHotel;
        this.lokasiHotel = lokasiHotel;
        this.urlfotoHotel = urlfotoHotel;
        this.idKamar = idKamar;
        this.namaKamar = namaKamar;
        this.jenisKamar = jenisKamar;
        this.hargaKamar = hargaKamar;
        this.deskripsiKamar = deskripsiKamar;
        this.urlfotoKamar = urlfotoKamar;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.durasi = durasi;
        this.jumlahKamar = jumlahKamar;
        this.total = total;
    }

    public String getIdHotel()
    {
        return idHotel;
    }

    public void setIdHotel(String idHotel)
    {
        this.idHotel = idHotel;
    }

    public String getNamaHotel()
    {
        return namaHotel;
    }

    public void setNamaHotel(String namaHotel)
    {
        this.namaHotel = namaHotel;
    }

    public String getRatingHotel()
    {
        return ratingHotel;
    }

    public void setRatingHotel(String ratingHotel)
    {
        this.ratingHotel = ratingHotel;
    }

    public String getLokasiHotel()
    {
        return lokasiHotel;
    }

    public void setLokasiHotel(String lokasiHotel)
    {
        this.lokasiHotel = lokasiHotel;
    }

    public String getUrlfotoHotel()
    {
        return urlfotoHotel;
    }

    public void setUrlfotoHotel(String urlfotoHotel)
    {
        this.urlfotoHotel = urlfotoHotel;
    }

    public String getIdKamar()
    {
        return idKamar;
    }

    public void setIdKamar(String idKamar)
    {
        this.idKamar = idKamar;
    }

    public String getNamaKamar()
    {
        return namaKamar;
    }

    public void setNamaKamar(String namaKamar)
    {
        this.namaKamar = namaKamar;
    }

    public String getJenisKamar()
    {
        return jenisKamar;
    }

    public void setJenisKamar(String jenisKamar)
    {
        this.jenisKamar = jenisKamar;
    }

    public String getHargaKamar()
    {
        return hargaKamar;
    }

    public void setHargaKamar(String hargaKamar)
    {
        this.hargaKamar = hargaKamar;
    }

    public String getDeskripsiKamar()
    {
        return deskripsiKamar;
    }

    public void setDeskripsiKamar(String deskripsiKamar)
    {
        this.deskripsiKamar = deskripsiKamar;
    }

    public String getUrlfotoKamar()
    {
        return urlfotoKamar;
    }

    public void setUrlfotoKamar(String urlfotoKamar)
    {
        this.urlfotoKamar = urlfotoKamar;
    }

    public String getCheckIn()
    {
        return checkIn;
    }

    public void setCheckIn(String checkIn)
    {
        this.checkIn = checkIn;
    }

    public String getCheckOut()
    {
        return checkOut;
    }

    public void setCheckOut(String checkOut)
    {
        this.checkOut = checkOut;
    }

    public String getDurasi()
    {
        return durasi;
    }

    public void setDurasi(String durasi)
    {
        this.durasi = durasi;
    }

    public String getJumlahKamar()
    {
        return jumlahKamar;
    }

    public void setJumlahKamar(String jumlahKamar)
    {
        this.jumlahKamar = jumlahKamar;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }
}
