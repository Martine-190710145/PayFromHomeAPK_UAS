package com.example.payfromhome.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityLoginBinding;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.User;
import com.example.payfromhome.model.mAuth;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private User user;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        user = new User("", "", "", 0);
        binding.setActivity(this);
        binding.setUser(user);
    }

    public View.OnClickListener onLoginButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
                Toast.makeText(LoginActivity.this, getString(R.string.username_dan_password_tidak_boleh_kosong), Toast.LENGTH_SHORT).show();
            } else {
                login(user.getUsername(), user.getPassword());
            }
        }
    };

    public View.OnClickListener onRegisterButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    };

    private void login(String uname, String pass) {
        SharedPreferenceHelper preferenceHelper = new SharedPreferenceHelper(this);
        LoadingDialog = ProgressDialog.show(LoginActivity.this, "","Proses. Silahkan Tunggu...", true);
        LoadingDialog.show();
        HashMap<String, String> form = new HashMap<>();
        form.put("email", uname.toString());
        form.put("password", pass.toString());

        (mApiInterface.auth_login(form)).enqueue(new Callback<mAuth>() {
            @Override
            public void onResponse(Call<mAuth> call, Response<mAuth> response) {
                if (response.isSuccessful()) {
                    LoadingDialog.dismiss();
                    if (response.body().getStatus().equals("success")) {
                        preferenceHelper.setSessionEmail(uname.toString());
                        preferenceHelper.setSessionId(response.body().getData().get("id").getAsString());
                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LoadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mAuth> call, Throwable t) {
                LoadingDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new SharedPreferenceHelper(this).isLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}