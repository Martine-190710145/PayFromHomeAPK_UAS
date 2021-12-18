package com.example.payfromhome.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityRegisterBinding;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.User;
import com.example.payfromhome.model.mAuth;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private User user;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        user = new User("", "", "", 0);
        binding.setActivity(this);
        binding.setUser(user);
    }

    public View.OnClickListener onRegisterButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (user.getName().isEmpty() || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
                Toast.makeText(RegisterActivity.this, getString(R.string.mohon_lengkapi_form), Toast.LENGTH_SHORT).show();
            } else {
                register(user.getName(), user.getUsername(), user.getPassword());
            }
        }
    };

    public View.OnClickListener onLoginButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    };

    private void register(String name, String username, String password) {
        LoadingDialog = ProgressDialog.show(RegisterActivity.this, "","Proses. Silahkan Tunggu...", true);
        LoadingDialog.show();
        HashMap<String, String> form = new HashMap<>();
        form.put("name", name.toString());
        form.put("email", username.toString());
        form.put("password", password.toString());

        (mApiInterface.auth_signup(form)).enqueue(new Callback<mAuth>() {
            @Override
            public void onResponse(Call<mAuth> call, Response<mAuth> response) {
                if (response.isSuccessful()) {
                    LoadingDialog.dismiss();
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.registrasi_berhasil), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LoadingDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mAuth> call, Throwable t) {
                LoadingDialog.dismiss();
                Toast.makeText(RegisterActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}