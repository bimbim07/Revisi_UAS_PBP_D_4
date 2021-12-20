package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.PUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.one_hotel.R;
import com.example.one_hotel.api.UserApi;
import com.example.one_hotel.databinding.ActivityUpdateBinding;
import com.example.one_hotel.models.User;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;

    private ActivityUpdateBinding binding;
    private ImageView ivFoto;
    private EditText etFullname;
    private EditText etUsername;
    private Button btnUpdate;
    private Bitmap bitmap = null;
    private ProfilePreferences profilePreferences;
    private View layoutLoading;
    private RequestQueue queue;
    private String id;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String urlfoto;
    private String token;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String urlFirebase;
    private Uri path;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update);
        binding.setUpdateActivity(this);

        ivFoto = binding.ivFoto;
        etFullname = binding.etFullname;
        etUsername = binding.etUsername;
        btnUpdate = binding.btnUpdate;
        layoutLoading = binding.layoutLoading;

        queue = Volley.newRequestQueue(getApplicationContext());

        profilePreferences = new ProfilePreferences(this);

        id = profilePreferences.getProfile().getId();
        fullname = profilePreferences.getProfile().getFullname();
        username = profilePreferences.getProfile().getUsername();
        email = profilePreferences.getProfile().getEmail();
        password = profilePreferences.getProfile().getPassword();
        token = profilePreferences.getProfile().getToken();

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

                    Glide.with(UpdateActivity.this).load(urlfoto).placeholder(R.drawable.loading).error(R.drawable.noimage).into(ivFoto);
                }
                else
                {
                    Toast.makeText(UpdateActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        etFullname.setText(fullname);
        etUsername.setText(username);

        ivFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(UpdateActivity.this);
                View selectMediaView = layoutInflater.inflate(R.layout.layout_select_media, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(selectMediaView.getContext()).create();

                Button btnKamera = selectMediaView.findViewById(R.id.btn_kamera);
                Button btnGaleri = selectMediaView.findViewById(R.id.btn_galeri);

                btnKamera.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                        {
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission, PERMISSION_REQUEST_CAMERA);
                        }
                        else
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }

                        alertDialog.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_PICTURE);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(selectMediaView);
                alertDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String fullname = etFullname.getText().toString();
                String username = etUsername.getText().toString();

                if(fullname.isEmpty() || username.isEmpty())
                {
                    if(fullname.isEmpty())
                    {
                        etFullname.setError("Fullname Kosong !");
                    }

                    if(username.isEmpty())
                    {
                        etUsername.setError("Username Kosong !");
                    }
                }
                else
                {
                    setLoading(true);

                    if(path != null)
                    {
                        storageReference.putFile(path)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                            {
                                if(!task.isSuccessful())
                                {
                                    setLoading(false);
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    updateUser(Long.parseLong(id));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
        {
            return;
        }

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE)
        {
            Uri selectedImage = data.getData();

            path = data.getData();

            try
            {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST)
        {
            bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String uri = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, null, null);

            path = Uri.parse(uri);
        }

        bitmap = getResizedBitmap(bitmap, 2048);
        ivFoto.setImageBitmap(bitmap);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1)
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }
        else
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private void updateUser(long id)
    {
        User user = new User(
                email,
                etFullname.getText().toString(),
                etUsername.getText().toString(),
                password,
                urlfoto);

        StringRequest stringRequest = new StringRequest(PUT, UserApi.UPDATE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                setLoading(false);

                profilePreferences.setProfile(String.valueOf(id),email,etFullname.getText().toString(),etUsername.getText().toString(),password,urlfoto,token);

                Toast.makeText(getApplicationContext(), "Update Berhasil !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this,ProfileActivity.class));
                finish();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                setLoading(false);

                try
                {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(UpdateActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                Gson gson = new Gson();

                String requestBody = gson.toJson(user);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void setLoading(boolean isLoading)
    {
        if(isLoading)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(UpdateActivity.this,ProfileActivity.class));
        finish();
    }
}