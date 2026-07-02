package com.example.siakad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import com.example.siakad.network.RetrofitClient;
import com.example.siakad.network.response.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout   tilNpm, tilPassword;
    private TextInputEditText etNpm, etPassword;
    private MaterialButton    btnLogin;
    private ProgressBar       progressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupTextWatchers();
        setupLoginButton();
    }

    private void initViews() {
        tilNpm        = findViewById(R.id.tilNpm);
        tilPassword   = findViewById(R.id.tilPassword);
        etNpm         = findViewById(R.id.etNpm);
        etPassword    = findViewById(R.id.etPassword);
        btnLogin      = findViewById(R.id.btnLogin);
        progressLogin = findViewById(R.id.progressLogin);
    }

    private void setupTextWatchers() {
        etNpm.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilNpm.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(v -> {
            if (validateInput()) {
                loginApi();
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        String npm      = etNpm.getText() != null
                ? etNpm.getText().toString().trim() : "";
        String password = etPassword.getText() != null
                ? etPassword.getText().toString().trim() : "";

        if (npm.isEmpty()) {
            tilNpm.setError("NPM tidak boleh kosong");
            isValid = false;
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password tidak boleh kosong");
            isValid = false;
        }

        return isValid;
    }

    private void loginApi() {
        String npm      = etNpm.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Map<String, String> body = new HashMap<>();
        body.put("npm",      npm);
        body.put("password", password);

        showLoading(true);

        RetrofitClient.getInstance()
                .getApiService()
                .login(body)
                .enqueue(new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call,
                                           @NonNull Response<LoginResponse> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse result = response.body();

                            if (result.isStatus()) {
                                LoginResponse.DataMahasiswa mhs = result.getData();
                                navigateToMain(mhs.getNpm(), mhs.getNama());
                            } else {
                                tilNpm.setError(result.getMessage());
                                tilPassword.setError(result.getMessage());
                            }
                        } else {
                            tilNpm.setError("Gagal terhubung ke server");
                            tilPassword.setError("Periksa koneksi Anda");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call,
                                          @NonNull Throwable t) {
                        showLoading(false);
                        tilNpm.setError("Tidak dapat terhubung ke server");
                        tilPassword.setError("Pastikan XAMPP sudah berjalan");
                    }
                });
    }

    private void navigateToMain(String npm, String nama) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("KEY_NPM",  npm);
        intent.putExtra("KEY_NAMA", nama);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnLogin.setEnabled(false);
            btnLogin.setText("Memverifikasi...");
            progressLogin.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setEnabled(true);
            btnLogin.setText("MASUK");
            progressLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
    }
}