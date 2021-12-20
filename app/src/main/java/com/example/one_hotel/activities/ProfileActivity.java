package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.PUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.api.UserApi;
import com.example.one_hotel.databinding.ActivityProfileBinding;
import com.example.one_hotel.models.User;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity
{
    private ActivityProfileBinding binding;
    private PorterShapeImageView ivFoto;
    private TextView tvNama;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPassword;
    private Button btnEdit;
    private ProfilePreferences profilePreferences;
    private Intent intent;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String urlFirebase;
    private String urlfoto;
    private SwipeRefreshLayout srProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        binding.setProfileActivity(this);


        ivFoto = binding.ivFoto;
        tvNama = binding.tvNama;
        tvUsername = binding.tvUsername;
        tvEmail = binding.tvEmail;
        tvPassword = binding.tvPassword;
        btnEdit = binding.btnEdit;
        srProfile = binding.srProfile;

        profilePreferences = new ProfilePreferences(this);

        String id = profilePreferences.getProfile().getId();
        String nama = profilePreferences.getProfile().getFullname();
        String username = profilePreferences.getProfile().getUsername();
        String email = profilePreferences.getProfile().getEmail();
        String password = profilePreferences.getProfile().getPassword();

        urlFirebase = "gs://one-hotel.appspot.com/FotoProfile/";
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl(urlFirebase + id);

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if(task.isSuccessful())
                {
                    urlfoto = task.getResult().toString();

                    Glide.with(ProfileActivity.this).load(urlfoto).placeholder(R.drawable.loading).error(R.drawable.noimage).into(ivFoto);
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvNama.setText(nama);
        tvUsername.setText(username);
        tvEmail.setText(email);
        tvPassword.setText(password);

        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(ProfileActivity.this,UpdateActivity.class);
                startActivity(intent);
                finish();
            }
        });

        srProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}