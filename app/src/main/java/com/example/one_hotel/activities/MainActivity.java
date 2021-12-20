package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.one_hotel.R;
import com.example.one_hotel.adapters.HotelAdapter;
import com.example.one_hotel.api.HotelApi;
import com.example.one_hotel.databinding.ActivityMainBinding;
import com.example.one_hotel.fragments.NavigationFragment;
import com.example.one_hotel.preferences.PaymentPreferences;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.example.one_hotel.response.HotelResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private SearchView svHotel;
    private RecyclerView rvHotel;
    private Intent intent;
    private HotelAdapter adapter;
    private Toolbar toolbar;
    private RequestQueue queue;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProfilePreferences profilePreferences;
    private PaymentPreferences paymentPreferences;
    private SwipeRefreshLayout srHotel;
    private NavigationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setMainActivity(this);

        svHotel = binding.svHotel;
        rvHotel = binding.rvHotel;
        toolbar = binding.toolbar;
        srHotel = binding.srHotel;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        profilePreferences = new ProfilePreferences(this);
        paymentPreferences = new PaymentPreferences(this);

        queue = Volley.newRequestQueue(getApplicationContext());

        adapter = new HotelAdapter(new ArrayList<>(), this);
        rvHotel.setLayoutManager(new LinearLayoutManager(this));
        rvHotel.setAdapter(adapter);

        svHotel.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragment = new NavigationFragment();

                getSupportFragmentManager().beginTransaction().replace(binding.fragment.getId(),fragment).commit();
            }
        });

        srHotel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getAllHotel();
            }
        });

        getAllHotel();
    }

    private void getAllHotel()
    {
        srHotel.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, HotelApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                HotelResponse hotelResponse = gson.fromJson(response, HotelResponse.class);

                adapter.setHotelList(hotelResponse.getHotelList());
                adapter.getFilter().filter(svHotel.getQuery());

                srHotel.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srHotel.setRefreshing(false);

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

    public void selectHotel(String id, String nama, String rating, String lokasi, String urlfoto)
    {
        paymentPreferences.setPayment(id,nama,rating,lokasi,urlfoto,null,null,null,null,null,null,null,null,null,null,null);

        intent = new Intent(MainActivity.this,KamarActivity.class);
        startActivity(intent);
    }

    public void signOut()
    {
        firebaseAuth.signOut();
        profilePreferences.deleteProfile();
        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}