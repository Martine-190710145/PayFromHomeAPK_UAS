package com.example.payfromhome.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.payfromhome.R;
import com.example.payfromhome.api.ApiClient;
import com.example.payfromhome.api.ApiInterface;
import com.example.payfromhome.databinding.ActivityHistoryBinding;
import com.example.payfromhome.db.TransactionDatabase;
import com.example.payfromhome.helper.SharedPreferenceHelper;
import com.example.payfromhome.model.RiwayatModel;
import com.example.payfromhome.model.Transaction;
import com.example.payfromhome.model.mResponse;
import com.example.payfromhome.model.mTransaction;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private HistoryAdapter adapter;
    private ActivityHistoryBinding binding;
    private ArrayList<RiwayatModel> transactionsdata;
    private SharedPreferenceHelper preferenceHelper;
    public ApiInterface mApiInterface;
    public ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        setSupportActionBar(binding.toolbarHistory);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.riwayat));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferenceHelper = new SharedPreferenceHelper(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        transactionsdata = new ArrayList<RiwayatModel>();
        binding.buttonCetak.setOnClickListener(V -> {
            try {
                cetakPdf();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        });

        getTransactions();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reload() {
        (mApiInterface.getTransaction(preferenceHelper.getSessionId())).enqueue(new Callback<mResponse>() {
            @Override
            public void onResponse(Call<mResponse> call, Response<mResponse> response) {
                if (response.body().getStatus().equals("success")) {
                    Toast.makeText(HistoryActivity.this, "Hapus Data Berhasil", Toast.LENGTH_LONG).show();
                    transactionsdata.clear();
                    transactionsdata.addAll(response.body().getItems());
                    adapter = new HistoryAdapter(HistoryActivity.this, transactionsdata);
                    binding.recyclerTransaction.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<mResponse> call, Throwable t) {

            }
        });
    }

    private void getTransactions() {
        (mApiInterface.getTransaction(preferenceHelper.getSessionId())).enqueue(new Callback<mResponse>() {
            @Override
            public void onResponse(Call<mResponse> call, Response<mResponse> response) {
                if (response.body().getStatus().equals("success")) {
                    transactionsdata.addAll(response.body().getItems());
                    adapter = new HistoryAdapter(HistoryActivity.this, transactionsdata);
                    binding.recyclerTransaction.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<mResponse> call, Throwable t) {

            }
        });
//        class GetTransactions extends AsyncTask<Void, Void, List<Transaction>> {
//
//            @Override
//            protected List<Transaction> doInBackground(Void... voids) {
//                return TransactionDatabase.getInstance(HistoryActivity.this)
//                        .getDatabase()
//                        .transactionDao()
//                        .getAll();
//            }
//
//            @Override
//            protected void onPostExecute(List<Transaction> transactions) {
//                super.onPostExecute(transactions);
//                transactionsdata = transactions;
//                adapter = new HistoryAdapter(HistoryActivity.this, transactionsdata);
//                binding.recyclerTransaction.setAdapter(adapter);
//            }
//        }
//
//        GetTransactions getTransactions = new GetTransactions();
//        getTransactions.execute();
    }

    private void cetakPdf() throws FileNotFoundException, DocumentException {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = currentTime.getTime() + ".pdf";
        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        OutputStream outputStream = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // bagian header
        Paragraph judul = new Paragraph("DATA PEMBAYARAN \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        // Buat tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        // Settingan ukuran tabel
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
//        Paragraph kepada = new Paragraph("Kepada Yth: \n" + "Budi Setiawan" + "\n",
//                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
//        cellSupplier.addElement(kepada);
//        tables.addCell(cellSupplier);
//        Paragraph NomorTanggal = new Paragraph( "No : " + "123456789" + "\n\n" +
//                "Tanggal : " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentTime) + "\n",
//                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));
//        NomorTanggal.setPaddingTop(5);
//        tables.addCell(NomorTanggal);
//        document.add(tables);
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\n Berikut Ini Merupakan List Pembayaran Yang Sudah Anda Lakukan: \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5, 5});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth()); tableHeader.setWidthPercentage(100);

        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Tanggal"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Serial"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Pembayaran"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Nominal"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        PdfPCell h5 = new PdfPCell(new Phrase("Payment"));
        h5.setHorizontalAlignment(Element.ALIGN_CENTER);
        h5.setPaddingBottom(5);

        tableHeader.addCell(h1); tableHeader.addCell(h2);
        tableHeader.addCell(h3); tableHeader.addCell(h4);
        tableHeader.addCell(h5);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.PINK);
        }

        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5, 5});
        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        for (RiwayatModel P : transactionsdata) {
            // masukan data pegawai jadi baris for (Pegawai P : listDataPegawai) {
            tableData.addCell(P.getDate());
            tableData.addCell(String.valueOf(P.getSerial_id()));
            tableData.addCell(P.getDisplayType());
            tableData.addCell(P.getDisplayNominal());
            if (P.getPayment().equals("transfer")) {
                tableData.addCell("Transfer Bank");
            } else {
                tableData.addCell("Saldo");
            }
        }

        document.add(tableData); Font h = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P); document.close();
        previewPdf(pdfFile);
        Toast.makeText(this, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();
    }

    private void previewPdf(File pdfFile) {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf"); List<ResolveInfo> list = packageManager.queryIntentActivities(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY); if (list.size() > 0) {
            Uri uri;
            uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            this.grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        }
    }


}