package com.example.payfromhome.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.RiwayatModel;
import com.example.payfromhome.model.mAuth;
import com.example.payfromhome.model.mTransaction;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<RiwayatModel> items = new ArrayList<>();
    View vw;
    ApiInterface mApiInterface;
    ProgressDialog LoadingDialog;

    public HistoryAdapter(Context context, ArrayList<RiwayatModel> items) {
        this.items = items;
        this.context = context;
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vw = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new HistoryAdapter.ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (items.get(position).getType().equals("LISTRIK")) {
            holder.textTransactionName.setText("Pembayaran Listrik");
        } else {
            holder.textTransactionName.setText("Pembayaran Air");
            holder.image_transaction_item.setImageResource(R.drawable.ic_water);
        }
        holder.textTransactionStatus.setText(items.get(position).getDisplayNominal());
        holder.textTransactionDate.setText(items.get(position).getDate());
        int posi = position;
        holder.card_bory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("Apakah Kamu Ingin Menghapus Data?")
                        .setMessage("Data Yang Dihapus Tidak Dapat Dikembalikan.")
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoadingDialog = ProgressDialog.show(context, "","Proses. Silahkan Tunggu...", true);
                                LoadingDialog.show();
                                mApiInterface.deleteTransaction(items.get(posi).getId().toString()).enqueue(new Callback<mTransaction>() {
                                    @Override
                                    public void onResponse(Call<mTransaction> call, Response<mTransaction> response) {
                                        if (response.isSuccessful()) {
                                            LoadingDialog.dismiss();
                                            if (response.body().getStatus().equals("success")) {

                                                if (context instanceof HistoryActivity) {
                                                    ((HistoryActivity)context).reload();
                                                }
                                            } else {
                                                Toast.makeText(context, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            LoadingDialog.dismiss();
                                            Toast.makeText(context, "Terjadi Kesalahan Pada Server....", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<mTransaction> call, Throwable t) {
                                        LoadingDialog.dismiss();
                                        Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View  dialogView = inflator.inflate(R.layout.form_data, null);
//                dialog.setView(dialogView);
//                dialog.setCancelable(true);
//                dialog.setIcon(R.mipmap.ic_launcher);
//                dialog.setTitle("Form Edit");
//
//                Spinner spinner_type    = (Spinner) dialogView.findViewById(R.id.spinner_type);
//                Spinner spinner_pay    = (Spinner) dialogView.findViewById(R.id.spinner_pay);
//                EditText txt_nominal    = (EditText) dialogView.findViewById(R.id.txt_nominal);
//                EditText txt_serial    = (EditText) dialogView.findViewById(R.id.txt_serial);
//                String nom_awal = txt_nominal.getText().toString();
//                txt_nominal.setText(items.get(posi).getNominal());
//                txt_serial.setText(items.get(posi).getSerial_id());
//                int p = 0;
//                if (items.get(posi).getType().equals("LISTRIK")) {
//                    p = 0;
//                } else {
//                    p = 1;
//                }
//                int pay = 0;
//                if (items.get(posi).getType().equals("transfer")) {
//                    p = 0;
//                } else {
//                    p = 1;
//                }
//                spinner_type.setSelection(pay);
//                spinner_pay.setSelection(pay);
//                dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String type_edit = "";
//                        if (spinner_type.getSelectedItemPosition() == 0) {
//                            type_edit = "LISTRIK";
//                        } else {
//                            type_edit = "WATER";
//                        }
//                        String payment_edit = "";
//                        if (spinner_pay.getSelectedItemPosition() == 0) {
//                            payment_edit = "transfer";
//                        } else {
//                            payment_edit = "balance";
//                        }
//                        String serial_edit = txt_serial.getText().toString();
//                        String nominal_edit = txt_nominal.getText().toString();
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView textTransactionDate;
        MaterialTextView textTransactionStatus;
        MaterialTextView textTransactionName;
        ShapeableImageView image_transaction_item;
        CardView card_bory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTransactionName = itemView.findViewById(R.id.text_transaction_name);
            textTransactionDate = itemView.findViewById(R.id.text_transaction_status);
            textTransactionStatus = itemView.findViewById(R.id.text_transaction_date);
            image_transaction_item = itemView.findViewById(R.id.image_transaction_item);
            card_bory = itemView.findViewById(R.id.card_bory);
        }
    }
}

