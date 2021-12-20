package com.example.one_hotel.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.activities.LocationActivity;
import com.example.one_hotel.activities.MainActivity;
import com.example.one_hotel.activities.ProfileActivity;
import com.example.one_hotel.activities.UpdateActivity;
import com.example.one_hotel.databinding.FragmentNavigationBinding;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NavigationFragment extends Fragment
{
    private FragmentNavigationBinding binding;
    private ImageButton ibtnFoto;
    private TextView tvUsername;
    private Button btnLokasi;
    private Button btnLogout;
    private Intent intent;
    private ProfilePreferences profilePreferences;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String urlFirebase;
    private String urlfoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_navigation,container,false);
        binding.setNavigationFragment(this);

        ibtnFoto = binding.ibtnFoto;
        tvUsername = binding.tvUsername;
        btnLokasi = binding.btnLokasi;
        btnLogout = binding.btnLogout;

        profilePreferences = new ProfilePreferences(getContext());

        String id = profilePreferences.getProfile().getId();

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

                    if (getActivity() == null)
                    {
                        return;
                    }
                    else
                    {
                        Glide.with(getContext()).load(urlfoto).placeholder(R.drawable.loading).error(R.drawable.noimage).into(ibtnFoto);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvUsername.setText(profilePreferences.getProfile().getUsername());

        ibtnFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnLokasi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(getActivity().getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((MainActivity) getContext()).signOut();
            }
        });

        binding.main.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(NavigationFragment.this).commit();
            }
        });


        return binding.getRoot();
    }
}