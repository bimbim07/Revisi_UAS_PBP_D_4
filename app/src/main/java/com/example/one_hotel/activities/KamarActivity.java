package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.one_hotel.R;
import com.example.one_hotel.adapters.KamarAdapter;
import com.example.one_hotel.api.KamarApi;
import com.example.one_hotel.databinding.ActivityKamarBinding;
import com.example.one_hotel.databinding.ActivityMainBinding;
import com.example.one_hotel.databinding.LayoutOrderBinding;
import com.example.one_hotel.fragments.NavigationFragment;
import com.example.one_hotel.models.Kamar;
import com.example.one_hotel.preferences.PaymentPreferences;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.example.one_hotel.response.KamarResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KamarActivity extends AppCompatActivity
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

    private ActivityKamarBinding binding;
    private SearchView svKamar;
    private RecyclerView rvKamar;
    private Intent intent;
    private KamarAdapter adapter;
    private RequestQueue queue;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProfilePreferences profilePreferences;
    private PaymentPreferences paymentPreferences;
    private SwipeRefreshLayout srKamar;
    private ArrayAdapter<String> adapterJumlahKamar;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Date dateCheckIn;
    private Date dateCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_kamar);
        binding.setKamarActivity(this);

        svKamar = binding.svKamar;
        rvKamar = binding.rvKamar;
        srKamar = binding.srKamar;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        profilePreferences = new ProfilePreferences(this);
        paymentPreferences = new PaymentPreferences(this);

        queue = Volley.newRequestQueue(getApplicationContext());

        adapter = new KamarAdapter(new ArrayList<>(), this);
        rvKamar.setLayoutManager(new LinearLayoutManager(this));
        rvKamar.setAdapter(adapter);

        svKamar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        srKamar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getAllKamar();
            }
        });

        getAllKamar();
    }

    private void getAllKamar()
    {
        srKamar.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, KamarApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                KamarResponse kamarResponse = gson.fromJson(response, KamarResponse.class);

                List<Kamar> kamarList = new ArrayList<>();

                for(int i = 0 ; i < kamarResponse.getKamarList().size() ; i++)
                {
                    if(kamarResponse.getKamarList().get(i).getId_hotel() == Long.parseLong(paymentPreferences.getPayment().getIdHotel()))
                    {
                        Long id_hotel = kamarResponse.getKamarList().get(i).getId_hotel();
                        String nama = kamarResponse.getKamarList().get(i).getNama();
                        String jenis = kamarResponse.getKamarList().get(i).getJenis();
                        double harga = kamarResponse.getKamarList().get(i).getHarga();
                        String deskripsi = kamarResponse.getKamarList().get(i).getDeskripsi();
                        String urlfoto = kamarResponse.getKamarList().get(i).getUrlfoto();

                        kamarList.add(new Kamar(id_hotel,nama,jenis,harga,deskripsi,urlfoto));
                    }
                }

                adapter.setKamarList(kamarList);
                adapter.getFilter().filter(svKamar.getQuery());

                srKamar.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srKamar.setRefreshing(false);

                try
                {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(getApplicationContext(), errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization","Bearer " + profilePreferences.getProfile().getToken());

                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public void selectKamar(String id, String nama, String jenis, String harga, String deskripsi, String urlfoto)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(KamarActivity.this);

        LayoutInflater inflater = LayoutInflater.from(builder.getContext());

        LayoutOrderBinding layoutBinding;
        layoutBinding = DataBindingUtil.inflate(inflater,R.layout.layout_order,null,false);
        layoutBinding.setOrderLayout(KamarActivity.this);

        View root = layoutBinding.getRoot();
        AutoCompleteTextView actvCheckIn = layoutBinding.actvCheckIn;
        AutoCompleteTextView actvCheckOut = layoutBinding.actvCheckOut;
        AutoCompleteTextView actvDurasi = layoutBinding.actvDurasi;
        AutoCompleteTextView actvJumlahKamar = layoutBinding.actvJumlahKamar;
        Button btnPesan = layoutBinding.btnPesan;

        adapterJumlahKamar = new ArrayAdapter<>(this, R.layout.item_list, LIST_JUMLAHKAMAR);
        actvJumlahKamar.setAdapter(adapterJumlahKamar);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        actvCheckIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(KamarActivity.this, (datePicker, year, month, day) ->
                {
                    calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    actvCheckIn.setText(simpleDateFormat.format(calendar.getTime()));
                    dateCheckIn = calendar.getTime();

                    if(dateCheckIn != null && dateCheckOut != null)
                    {
                        long diff = dateCheckOut.getTime() - dateCheckIn.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;

                        actvDurasi.setText(String.valueOf(days + 1));
                    }

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(KamarActivity.this, (datePicker, year, month, day) ->
                {
                    calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    actvCheckOut.setText(simpleDateFormat.format(calendar.getTime()));
                    dateCheckOut = calendar.getTime();

                    if(dateCheckIn != null && dateCheckOut != null)
                    {
                        long diff = dateCheckOut.getTime() - dateCheckIn.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;

                        actvDurasi.setText(String.valueOf(days + 0));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String checkIn = actvCheckIn.getText().toString();
                String checkOut = actvCheckOut.getText().toString();
                String durasi = actvDurasi.getText().toString();
                String jumlahKamar = actvJumlahKamar.getText().toString();

                if(checkIn.isEmpty() || checkOut.isEmpty() || durasi.isEmpty() || jumlahKamar.isEmpty())
                {
                    if(checkIn.isEmpty())
                    {
                        actvCheckIn.setError("Tanggal Check In Belum Dipilih !");
                    }
                    else
                    {
                        actvCheckIn.setError(null);
                    }

                    if(checkOut.isEmpty())
                    {
                        actvCheckOut.setError("Tanggal Check Out Belum Dipilih !");
                    }
                    else
                    {
                        actvCheckOut.setError(null);
                    }

                    if(durasi.isEmpty())
                    {
                        actvDurasi.setError("Tanggal Check In dan Check Out Belum Dipilih !");
                    }
                    else
                    {
                        actvDurasi.setError(null);
                    }

                    if(jumlahKamar.isEmpty())
                    {
                        actvJumlahKamar.setError("Jumlah Kamar Belum Dipilih !");
                    }
                    else
                    {
                        actvJumlahKamar.setError(null);
                    }
                }
                else
                {
                    String idHotel = paymentPreferences.getPayment().getIdHotel();
                    String namaHotel = paymentPreferences.getPayment().getNamaHotel();
                    String ratingHotel = paymentPreferences.getPayment().getRatingHotel();
                    String lokasiHotel = paymentPreferences.getPayment().getLokasiHotel();
                    String urlfotoHotel = paymentPreferences.getPayment().getUrlfotoHotel();

                    double tempTotal = Double.parseDouble(harga) * Double.parseDouble(jumlahKamar.replace(" Kamar","")) * Double.parseDouble(durasi);
                    String total = String.valueOf(tempTotal);

                    paymentPreferences.setPayment(idHotel,namaHotel,ratingHotel,lokasiHotel,urlfotoHotel,id,nama,jenis,harga,deskripsi,urlfoto,checkIn,checkOut,durasi,jumlahKamar,total);

                    intent = new Intent(KamarActivity.this,PaymentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        builder.setView(root);
        builder.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        paymentPreferences.deletePayment();
        finish();
    }
}