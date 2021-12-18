package com.example.payfromhome.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.payfromhome.databinding.ActivityElectricityBinding;
import com.example.payfromhome.db.TransactionDatabase;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.Transaction;
import com.example.payfromhome.model.mTransaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElectricityActivity extends AppCompatActivity {
    private SharedPreferenceHelper preferenceHelper;

    private ActivityElectricityBinding binding;
    private Transaction transaction;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_electricity);

        preferenceHelper = new SharedPreferenceHelper(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        transaction = new Transaction("", "", "", "", "");
        binding.setActivity(this);
        binding.setTransaction(transaction);

        setSupportActionBar(binding.toolbarElectricity);
        setSupportActionBar(binding.toolbarElectricity);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.listrik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] paymentMethods = {getString(R.string.transfer_bank), getString(R.string.saldo)};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, paymentMethods
        );
        binding.spinnerPayment.setAdapter(spinnerAdapter);
        binding.editNominal.setText("0");
    }

    public View.OnClickListener onPayButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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

            if (transaction.getStringNominal().isEmpty()) {
                Toast.makeText(ElectricityActivity.this, getString(R.string.mohon_lengkapi_form), Toast.LENGTH_SHORT).show();
                return;
            }
            saveTransaction(transaction.getSerialId(), transaction.getNominal(), pay, payment);
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

    private void saveTransaction(String id, String nominal, int payment, String paym) {
        LoadingDialog = ProgressDialog.show(ElectricityActivity.this, "","Proses. Silahkan Tunggu...", true);
        LoadingDialog.show();
        String date = new Date().toString();
        HashMap<String, String> form = new HashMap<>();
        form.put("type", "LISTRIK");
        form.put("serial_id", id);
        form.put("nominal", nominal);
        form.put("payment", paym);
        form.put("date", date);
        form.put("user_id", preferenceHelper.getSessionId());
        (mApiInterface.createdTransaction(form)).enqueue(new Callback<mTransaction>() {
            @Override
            public void onResponse(Call<mTransaction> call, Response<mTransaction> response) {
                if (response.isSuccessful()) {
                    LoadingDialog.dismiss();
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(ElectricityActivity.this, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                        binding.editId.setText("");
                        binding.editNominal.setText("");
                        binding.editId.setText("");
                    } else {
                        Toast.makeText(ElectricityActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LoadingDialog.dismiss();
                    Toast.makeText(ElectricityActivity.this, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mTransaction> call, Throwable t) {
                LoadingDialog.dismiss();
                Toast.makeText(ElectricityActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        class AddTransaction extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                String date = new Date().toString();
//                Transaction transaction = new Transaction("electricity", id, nominal, payment, date);
//
//                TransactionDatabase.getInstance(ElectricityActivity.this)
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
//                startActivity(new Intent(ElectricityActivity.this, LandingActivity.class));
//                finish();
//            }
//        }
//
//        AddTransaction addTransaction = new AddTransaction();
//        addTransaction.execute();
    }
}