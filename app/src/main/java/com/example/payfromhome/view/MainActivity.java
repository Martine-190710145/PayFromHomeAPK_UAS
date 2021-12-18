package com.example.payfromhome.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityMainBinding;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.mAuth;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferenceHelper sharedPreferenceHelper;
    private ActivityMainBinding binding;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        binding.setActivity(this);
        getBalance();
//        binding.setBalance(NumberFormat
//                .getCurrencyInstance(new Locale("in", "id"))
//                .format(sharedPreferenceHelper.getBalance())
//        );
    }

    public void getBalance() {
        HashMap<String, String> form = new HashMap<>();
        form.put("id", sharedPreferenceHelper.getSessionId());
        (mApiInterface.getBalance(form)).enqueue(new Callback<mAuth>() {
            @Override
            public void onResponse(Call<mAuth> call, Response<mAuth> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        binding.setBalance(NumberFormat
                                .getCurrencyInstance(new Locale("in", "id"))
                                .format(response.body().getData().get("balance").getAsInt())
                        );
                        sharedPreferenceHelper.setSessionBalance(response.body().getData().get("balance").getAsString());
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mAuth> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View.OnClickListener cardElectricityClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, ElectricityActivity.class));
        }
    };

    public View.OnClickListener cardWaterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, WaterActivity.class));
        }
    };

    public View.OnClickListener cardHistoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
    };

    public View.OnClickListener cardAboutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
    };

    public View.OnClickListener cardProfileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }
    };

    public View.OnClickListener btnLogoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPreferenceHelper.setIsLogin(false);

            sharedPreferenceHelper.setSessionEmail("");
            sharedPreferenceHelper.setSessionId("");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
//        binding.setBalance(NumberFormat
//                .getCurrencyInstance(new Locale("in", "id"))
//                .format(sharedPreferenceHelper.getBalance())
//        );
    }
}