package com.example.payfromhome.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityWaterBinding;
import com.example.payfromhome.db.TransactionDatabase;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.Transaction;
import com.example.payfromhome.model.mTransaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterActivity extends AppCompatActivity {
    private SharedPreferenceHelper preferenceHelper;
    private ActivityWaterBinding binding;
    private Transaction transaction;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_water);

        preferenceHelper = new SharedPreferenceHelper(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        transaction = new Transaction();

        binding.setActivity(this);
        binding.setTransaction(transaction);

        setSupportActionBar(binding.toolbarWater);
        setSupportActionBar(binding.toolbarWater);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.air));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] paymentMethods = {getString(R.string.transfer_bank), getString(R.string.saldo)};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, paymentMethods
        );
        binding.spinnerPayment.setAdapter(spinnerAdapter);
    }

    public View.OnClickListener onPayButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int nominal = new Random().nextInt(5000 - 1000) + 1000;
            int paymentPos = binding.spinnerPayment.getSelectedItemPosition();
            String payment = "";
            int pay = 0;
            if (paymentPos == 0) {
                payment = "transfer";
                pay = 0;
            } else {
                payment = "balance";
                pay = 1;
            }

            if (transaction.getSerialId().isEmpty()) {
                Toast.makeText(WaterActivity.this, getString(R.string.mohon_lengkapi_form), Toast.LENGTH_SHORT).show();
                return;
            }

            saveTransaction(transaction.getSerialId(), nominal, pay, payment);
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTransaction(String id, int nominal, int payment, String paym) {
        LoadingDialog = ProgressDialog.show(WaterActivity.this, "","Proses. Silahkan Tunggu...", true);
        LoadingDialog.show();
        String date = new Date().toString();
        HashMap<String, String> form = new HashMap<>();
        form.put("type", "WATER");
        form.put("serial_id", id);
        form.put("nominal", String.valueOf(nominal));
        form.put("payment", paym);
        form.put("date", date);
        form.put("user_id", preferenceHelper.getSessionId());
        (mApiInterface.createdTransaction(form)).enqueue(new Callback<mTransaction>() {
            @Override
            public void onResponse(Call<mTransaction> call, Response<mTransaction> response) {
                if (response.isSuccessful()) {
                    LoadingDialog.dismiss();
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(WaterActivity.this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                        binding.editId.setText("");
                        binding.editId.setText("");
                    } else {
                        Toast.makeText(WaterActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LoadingDialog.dismiss();
                    Toast.makeText(WaterActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mTransaction> call, Throwable t) {
                LoadingDialog.dismiss();
                Toast.makeText(WaterActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        class AddTransaction extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                String date = new Date().toString();
//                Transaction transaction = new Transaction("water", id, nominal, payment, date);
//
//                TransactionDatabase.getInstance(WaterActivity.this)
//                        .getDatabase()
//                        .transactionDao()
//                        .insert(transaction);
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void unused) {
//                super.onPostExecute(unused);
//                startActivity(new Intent(WaterActivity.this, LandingActivity.class));
//                finish();
//            }
//        }
//
//        AddTransaction addTransaction = new AddTransaction();
//        addTransaction.execute();
    }
}