package com.example.one_hotel.activities;

import static com.example.one_hotel.notification.MyApplication.CHANNEL_1_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.databinding.ActivityPaymentBinding;
import com.example.one_hotel.preferences.PaymentPreferences;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class PaymentActivity extends AppCompatActivity
{
    private ActivityPaymentBinding binding;
    private ImageView ivFotoHotel;
    private TextView tvNamaHotel;
    private TextView tvLokasiHotel;
    private TextView tvRatingHotel;
    private ImageView ivFotoKamar;
    private TextView tvNamaKamar;
    private TextView tvJenisKamar;
    private TextView tvHargaKamar;
    private TextView tvDeskripsiKamar;
    private TextInputEditText tietTanggal;
    private TextInputEditText tietDurasi;
    private TextInputEditText tietJumlahKamar;
    private TextInputEditText tietTotal;
    private Button btnKonfirmasi;

    private PaymentPreferences paymentPreferences;
    private Intent intent;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        binding.setPaymentActivity(this);

        ivFotoHotel = binding.ivFotoHotel;
        tvNamaHotel = binding.tvNamaHotel;
        tvLokasiHotel = binding.tvLokasiHotel;
        tvRatingHotel = binding.tvRatingHotel;

        ivFotoKamar = binding.ivFotoKamar;
        tvNamaKamar = binding.tvNamaKamar;
        tvJenisKamar = binding.tvJenisKamar;
        tvHargaKamar = binding.tvHargaKamar;
        tvDeskripsiKamar = binding.tvDeskripsiKamar;

        tietTanggal = binding.tietTanggal;
        tietDurasi = binding.tietDurasi;
        tietJumlahKamar = binding.tietJumlahKamar;
        tietTotal = binding.tietTotal;
        btnKonfirmasi = binding.btnKonfirmasi;

        paymentPreferences = new PaymentPreferences(this);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        String idHotel = paymentPreferences.getPayment().getIdHotel();
        String namaHotel = paymentPreferences.getPayment().getNamaHotel();
        String ratingHotel = paymentPreferences.getPayment().getRatingHotel();
        String lokasiHotel = paymentPreferences.getPayment().getLokasiHotel();
        String urlfotoHotel = paymentPreferences.getPayment().getUrlfotoHotel();
        String idKamar = paymentPreferences.getPayment().getIdKamar();
        String namaKamar = paymentPreferences.getPayment().getNamaKamar();
        String jenisKamar = paymentPreferences.getPayment().getJenisKamar();
        String hargaKamar = paymentPreferences.getPayment().getHargaKamar();
        String deskripsiKamar = paymentPreferences.getPayment().getDeskripsiKamar();
        String urlfotoKamar = paymentPreferences.getPayment().getUrlfotoKamar();
        String checkIn = paymentPreferences.getPayment().getCheckIn();
        String checkOut = paymentPreferences.getPayment().getCheckOut();
        String durasi = paymentPreferences.getPayment().getDurasi();
        String jumlahKamar = paymentPreferences.getPayment().getJumlahKamar();
        String total = paymentPreferences.getPayment().getTotal();

        Glide.with(this).load(urlfotoHotel).placeholder(R.drawable.loading).error(R.drawable.noimage).into(ivFotoHotel);
        tvNamaHotel.setText(namaHotel);
        tvLokasiHotel.setText(lokasiHotel);
        tvRatingHotel.setText(ratingHotel);

        Glide.with(this).load(urlfotoKamar).placeholder(R.drawable.loading).error(R.drawable.noimage).into(ivFotoKamar);
        tvNamaKamar.setText(namaKamar);
        tvJenisKamar.setText(jenisKamar);
        tvHargaKamar.setText(hargaKamar);
        tvDeskripsiKamar.setText(deskripsiKamar);

        tietTanggal.setText(checkIn + " - " + checkOut);
        tietDurasi.setText(durasi);
        tietJumlahKamar.setText(jumlahKamar);
        tietTotal.setText("Rp. " + new DecimalFormat("############").format(Double.parseDouble(total)));

        btnKonfirmasi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NotificationCompat.Builder notification = new NotificationCompat.Builder(view.getContext(),CHANNEL_1_ID);
                notification.setSmallIcon(R.drawable.ic_baseline_my_location_24)
                        .setContentTitle("Selamat !")
                        .setContentText("Anda Telah Memilih Hotel " + namaHotel + " Untuk Menginap")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setColor(Color.RED);

                notificationManagerCompat.notify(1,notification.build());

                paymentPreferences.deletePayment();
                intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        paymentPreferences.deletePayment();
        intent = new Intent(PaymentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}