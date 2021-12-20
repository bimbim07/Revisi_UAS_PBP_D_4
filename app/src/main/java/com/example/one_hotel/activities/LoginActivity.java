package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.one_hotel.R;
import com.example.one_hotel.api.UserApi;
import com.example.one_hotel.databinding.ActivityLoginBinding;
import com.example.one_hotel.models.User;
import com.example.one_hotel.preferences.ProfilePreferences;
import com.example.one_hotel.response.UserResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity
{
    private ActivityLoginBinding binding;
    private EditText etEmail;
    private EditText etPassword;
    private ImageButton ibtnVisibility;
    private Button btnLogin;
    private Button btnRegister;
    private boolean eye = false;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RequestQueue queue;
    private ProfilePreferences profilePreferences;
    private View layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.setLoginActivity(this);

        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        ibtnVisibility = binding.ibtnVisibility;
        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
        layoutLoading = binding.layoutLoading;

        profilePreferences = new ProfilePreferences(this);

        queue = Volley.newRequestQueue(this);

        firebaseAuth = FirebaseAuth.getInstance();

        ibtnVisibility.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(eye)
                {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ibtnVisibility.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24);
                    eye = false;
                }
                else
                {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibtnVisibility.setBackgroundResource(R.drawable.ic_baseline_visibility_24);
                    eye = true;
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty())
                {
                    if(email.isEmpty())
                    {
                        etEmail.setError("Email Kosong !");
                    }

                    if(password.isEmpty())
                    {
                        etPassword.setError("Password Kosong !");
                    }
                }
                else
                {
                    loginUser();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                firebaseAuth.signOut();
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser()
    {
        setLoading(true);

        User user = new User(
                etEmail.getText().toString(),
                null,
                null,
                etPassword.getText().toString(),
                null);

        StringRequest stringRequest = new StringRequest(POST, UserApi.LOGIN, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                UserResponse userResponse = gson.fromJson(response,UserResponse.class);

                String id = String.valueOf(userResponse.getUser().getId());
                String email = userResponse.getUser().getEmail();
                String fullname = userResponse.getUser().getFullname();
                String username = userResponse.getUser().getUsername();
                String password = etPassword.getText().toString();
                String urlfoto = userResponse.getUser().getUrlfoto();
                String token = userResponse.getAccessToken();

                firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            firebaseUser = firebaseAuth.getCurrentUser();

                            if(firebaseUser.isEmailVerified())
                            {
                                profilePreferences.setProfile(id,email,fullname,username,password,urlfoto,token);

                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Email Belum Di Verifikasi !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        setLoading(false);
                    }
                });
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

                    Toast.makeText(getApplicationContext(), errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
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
    protected void onStart()
    {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            if(firebaseUser.isEmailVerified())
            {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}