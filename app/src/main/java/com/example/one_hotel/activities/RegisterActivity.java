package com.example.one_hotel.activities;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.one_hotel.R;
import com.example.one_hotel.api.UserApi;
import com.example.one_hotel.databinding.ActivityRegisterBinding;
import com.example.one_hotel.models.User;
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

public class RegisterActivity extends AppCompatActivity
{
    private ActivityRegisterBinding binding;
    private EditText etEmail;
    private EditText etFullname;
    private EditText etUsername;
    private EditText etPassword;
    private ImageButton ibtnVisibility;
    private Button btnRegister;
    private CheckBox cbPrivasi;
    private Button btnLogin;
    private boolean eye = false;
    private boolean privasi = false;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RequestQueue queue;
    private View layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        binding.setRegisterActivity(this);

        etEmail = binding.etEmail;
        etFullname = binding.etFullname;
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        ibtnVisibility = binding.ibtnVisibility;
        btnRegister = binding.btnRegister;
        cbPrivasi = binding.cbPrivasi;
        btnLogin = binding.btnLogin;
        layoutLoading = binding.layoutLoading;

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

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = etEmail.getText().toString();
                String fullname = etFullname.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(email.isEmpty() || fullname.isEmpty() || username.isEmpty() || password.isEmpty())
                {
                    if(email.isEmpty())
                    {
                        etEmail.setError("Email Kosong !");
                    }

                    if(fullname.isEmpty())
                    {
                        etFullname.setError("Fullname Kosong !");
                    }

                    if(username.isEmpty())
                    {
                        etUsername.setError("Username Kosong !");
                    }

                    if(password.isEmpty())
                    {
                        etPassword.setError("Password Kosong !");
                    }
                }
                else
                {
                    if(privasi)
                    {
                        registerUser();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Anda Belum Menyetujui Privasi !", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        cbPrivasi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(compoundButton.isChecked())
                {
                    privasi = true;
                }
                else
                {
                    privasi = false;
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser()
    {
        setLoading(true);

        User user = new User(
                etEmail.getText().toString(),
                etFullname.getText().toString(),
                etUsername.getText().toString(),
                etPassword.getText().toString(),
                null);

        StringRequest stringRequest = new StringRequest(POST, UserApi.REGISTER, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            firebaseUser = firebaseAuth.getCurrentUser();

                            firebaseUser.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Register Berhasil !", Toast.LENGTH_SHORT).show();

                                        intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
}