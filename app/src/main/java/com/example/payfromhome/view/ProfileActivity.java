package com.example.payfromhome.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityProfileBinding;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.User;
import com.example.payfromhome.model.mAuth;
import com.example.payfromhome.model.mResponse;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private User user;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        user = new User();
        binding.setActivity(this);
        binding.setUser(user);

        getUserData();
    }

    public void getUserData(){
        SharedPreferenceHelper preferenceHelper = new SharedPreferenceHelper(this);
        mApiInterface.getUser(preferenceHelper.getSessionId()).enqueue(new Callback<mAuth>() {
            @Override
            public void onResponse(Call<mAuth> call, Response<mAuth> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equals("success")){
                        Log.e("DATA", response.body().getData().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getData().toString());
                            binding.editName.setText(jsonObject.getString("name"));
                            binding.editUsername.setText(jsonObject.getString("email"));
                        }catch (Exception e){
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mAuth> call, Throwable t) {
                Log.e("LOG", t.getMessage());
                Toast.makeText(ProfileActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View.OnClickListener onUpdateButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferenceHelper preferenceHelper = new SharedPreferenceHelper(ProfileActivity.this);
            HashMap<String, String> form = new HashMap<>();
            form.put("id", preferenceHelper.getSessionId());
            form.put("name", binding.editName.getText().toString());
            form.put("email", binding.editUsername.getText().toString());
            form.put("password", binding.editPassword.getText().toString());
            mApiInterface.updateUser(form).enqueue(new Callback<mAuth>() {
                @Override
                public void onResponse(Call<mAuth> call, Response<mAuth> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus().equals("success")){
                            Log.e("DATA", response.body().getData().toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().getData().toString());
                                binding.editName.setText(jsonObject.getString("name"));
                                binding.editUsername.setText(jsonObject.getString("email"));
                                preferenceHelper.setSessionEmail(jsonObject.getString("email"));
                                Toast.makeText(ProfileActivity.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<mAuth> call, Throwable t) {
                    Log.e("LOG", t.getMessage());
                    Toast.makeText(ProfileActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}