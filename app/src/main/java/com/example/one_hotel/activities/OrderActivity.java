package com.example.one_hotel.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.one_hotel.R;
import com.example.one_hotel.databinding.ActivityOrderBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity 
{
    private static final String[] LIST_JUMLAHKAMAR = new String[]
    {
            "1 Kamar",
            "2 Kamar",
            "3 Kamar",
            "4 Kamar",
            "5 Kamar",
            "6 Kamar",
            "7 Kamar",
            "8 Kamar",
            "9 Kamar",
            "10 Kamar"
    };

    private ActivityOrderBinding binding;
    private AutoCompleteTextView actvCheckIn;
    private AutoCompleteTextView actvCheckOut;
    private AutoCompleteTextView actvDurasi;
    private AutoCompleteTextView actvJumlahKamar;
    private Button btnCariOrder;
    private Intent intent;
    private ArrayAdapter<String> adapterKotaTujuan;
    private ArrayAdapter<String> adapterJumlahKamar;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Date dateCheckIn;
    private Date dateCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        binding.setOrderActivity(this);

        actvCheckIn = binding.actvCheckIn;
        actvCheckOut = binding.actvCheckOut;
        actvDurasi = binding.actvDurasi;
        actvJumlahKamar = binding.actvJumlahKamar;
        btnCariOrder = binding.btnCariOrder;

        adapterJumlahKamar = new ArrayAdapter<>(this, R.layout.item_list, LIST_JUMLAHKAMAR);
        actvJumlahKamar.setAdapter(adapterJumlahKamar);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        actvCheckIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderActivity.this, (datePicker, year, month, day) ->
                {
                    calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    actvCheckIn.setText(simpleDateFormat.format(calendar.getTime()));
                    dateCheckIn = calendar.getTime();
                    setDurasi(1);
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        actvCheckOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderActivity.this, (datePicker, year, month, day) ->
                {
                    calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    actvCheckOut.setText(simpleDateFormat.format(calendar.getTime()));
                    dateCheckOut = calendar.getTime();
                    setDurasi(0);
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        btnCariOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                String kotaTujuan = actvKotaTujuan.getText().toString();
//                String checkIn = actvCheckIn.getText().toString();
//                String checkOut = actvCheckOut.getText().toString();
//                String durasi = actvDurasi.getText().toString();
//                String jumlahKamar = actvJumlahKamar.getText().toString();
//
//                if(kotaTujuan.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty() || durasi.isEmpty() || jumlahKamar.isEmpty())
//                {
//                    if(kotaTujuan.isEmpty())
//                    {
//                        actvKotaTujuan.setError("Kota Tujuan Belum Dipilih !");
//                    }
//                    else
//                    {
//                        actvKotaTujuan.setError(null);
//                    }
//
//                    if(checkIn.isEmpty())
//                    {
//                        actvCheckIn.setError("Tanggal Check In Belum Dipilih !");
//                    }
//                    else
//                    {
//                        actvCheckIn.setError(null);
//                    }
//
//                    if(checkOut.isEmpty())
//                    {
//                        actvCheckOut.setError("Tanggal Check Out Belum Dipilih !");
//                    }
//                    else
//                    {
//                        actvCheckOut.setError(null);
//                    }
//
//                    if(durasi.isEmpty())
//                    {
//                        actvDurasi.setError("Tanggal Check In dan Check Out Belum Dipilih !");
//                    }
//                    else
//                    {
//                        actvDurasi.setError(null);
//                    }
//
//                    if(jumlahKamar.isEmpty())
//                    {
//                        actvJumlahKamar.setError("Jumlah Kamar Belum Dipilih !");
//                    }
//                    else
//                    {
//                        actvJumlahKamar.setError(null);
//                    }
//                }
//                else
//                {
////                    paymentPreferences.setPayment();
//
//                    intent = new Intent(OrderActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });
    }

    private void setDurasi(int check)
    {
        if(dateCheckIn != null && dateCheckOut != null)
        {
            long diff = dateCheckOut.getTime() - dateCheckIn.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            actvDurasi.setText(String.valueOf(days + check));
        }
    }
}