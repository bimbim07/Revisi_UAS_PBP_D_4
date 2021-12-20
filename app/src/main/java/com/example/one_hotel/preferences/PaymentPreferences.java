package com.example.one_hotel.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.one_hotel.models.Payment;

public class PaymentPreferences
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String IS_PAYMENT = "isPayment";

    //HOTEL
    public static final String KEY_ID_HOTEL = "idHotel";
    public static final String KEY_NAMA_HOTEL = "namaHotel";
    public static final String KEY_RATING_HOTEL = "ratingHotel";
    public static final String KEY_LOKASI_HOTEL = "lokasiHotel";
    public static final String KEY_URLFOTO_HOTEL = "urlfotoHotel";

    //KAMAR
    public static final String KEY_ID_KAMAR = "idKamar";
    public static final String KEY_NAMA_KAMAR = "namaKamar";
    public static final String KEY_JENIS_KAMAR = "jenisKamar";
    public static final String KEY_HARGA_KAMAR = "hargaKamar";
    public static final String KEY_DESKRIPSI_KAMAR = "deskripsiKamar";
    public static final String KEY_URLFOTO_KAMAR = "urlfotoKamar";

    //ORDER
    public static final String KEY_CHECKIN = "checkIn";
    public static final String KEY_CHECKOUT = "checkOut";
    public static final String KEY_DURASI = "durasi";
    public static final String KEY_JUMLAHKAMAR = "jumlahKamar";
    public static final String KEY_TOTAL = "total";

    public PaymentPreferences(Context context)
    {
        this.context = context;

        preferences = context.getSharedPreferences("PaymentPreferences",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setPayment(String idHotel, String namaHotel, String ratingHotel, String lokasiHotel, String urlfotoHotel, String idKamar, String namaKamar, String jenisKamar, String hargaKamar, String deskripsiKamar, String urlfotoKamar, String checkIn, String checkOut, String durasi, String jumlahKamar, String total)
    {
        editor.putBoolean(IS_PAYMENT,true);

        editor.putString(KEY_ID_HOTEL,idHotel);
        editor.putString(KEY_NAMA_HOTEL,namaHotel);
        editor.putString(KEY_RATING_HOTEL,ratingHotel);
        editor.putString(KEY_LOKASI_HOTEL,lokasiHotel);
        editor.putString(KEY_URLFOTO_HOTEL,urlfotoHotel);

        editor.putString(KEY_ID_KAMAR,idKamar);
        editor.putString(KEY_NAMA_KAMAR,namaKamar);
        editor.putString(KEY_JENIS_KAMAR,jenisKamar);
        editor.putString(KEY_HARGA_KAMAR,hargaKamar);
        editor.putString(KEY_DESKRIPSI_KAMAR,deskripsiKamar);
        editor.putString(KEY_URLFOTO_KAMAR,urlfotoKamar);

        editor.putString(KEY_CHECKIN,checkIn);
        editor.putString(KEY_CHECKOUT,checkOut);
        editor.putString(KEY_DURASI,durasi);
        editor.putString(KEY_JUMLAHKAMAR,jumlahKamar);
        editor.putString(KEY_TOTAL,total);

        editor.commit();
    }

    public Payment getPayment()
    {
        //HOTEL
        String idHotel = preferences.getString(KEY_ID_HOTEL,null);
        String namaHotel = preferences.getString(KEY_NAMA_HOTEL,null);
        String ratingHotel = preferences.getString(KEY_RATING_HOTEL,null);
        String lokasiHotel = preferences.getString(KEY_LOKASI_HOTEL,null);
        String urlfotoHotel = preferences.getString(KEY_URLFOTO_HOTEL,null);

        //KAMAR
        String idKamar = preferences.getString(KEY_ID_KAMAR,null);
        String namaKamar = preferences.getString(KEY_NAMA_KAMAR,null);
        String jenisKamar = preferences.getString(KEY_JENIS_KAMAR,null);
        String hargaKamar = preferences.getString(KEY_HARGA_KAMAR,null);
        String deskripsiKamar = preferences.getString(KEY_DESKRIPSI_KAMAR,null);
        String urlfotoKamar = preferences.getString(KEY_URLFOTO_KAMAR,null);

        //ORDER
        String checkIn = preferences.getString(KEY_CHECKIN,null);
        String checkOut = preferences.getString(KEY_CHECKOUT,null);
        String durasi = preferences.getString(KEY_DURASI,null);
        String jumlahKamar = preferences.getString(KEY_JUMLAHKAMAR,null);
        String total = preferences.getString(KEY_TOTAL,null);

        return new Payment(idHotel,namaHotel,ratingHotel,lokasiHotel,urlfotoHotel,idKamar,namaKamar,jenisKamar,hargaKamar,deskripsiKamar,urlfotoKamar,checkIn,checkOut,durasi,jumlahKamar,total);
    }

    public boolean checkPayment()
    {
        return preferences.getBoolean(IS_PAYMENT,false);
    }

    public void deletePayment()
    {
        editor.clear();
        editor.commit();
    }
}
