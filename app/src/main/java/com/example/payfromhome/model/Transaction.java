package com.example.payfromhome.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.payfromhome.BR;

import java.text.NumberFormat;
import java.util.Locale;

@Entity(tableName = "transactions")
public class Transaction extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "serial_id")
    private String serialId;

    @ColumnInfo(name = "nominal")
    private String nominal;

    @ColumnInfo(name = "payment")
    private String payment;

    @ColumnInfo(name = "date")
    private String date;

    public Transaction() {
    }

    public Transaction(String type, String serialId, String nominal, String payment, String date) {
        this.type = type;
        this.serialId = serialId;
        this.nominal = nominal;
        this.date = date;
        this.payment = payment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
        notifyPropertyChanged(BR.serialId);
    }

    @Bindable
    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
        notifyPropertyChanged(BR.nominal);
    }

    public String getStringNominal() {
        return String.valueOf(this.nominal);
    }

    public void setStringNominal(String nominal) {
        if (nominal.isEmpty()) {
            this.nominal = "0";
        } else {
            this.nominal = nominal;
        }
        notifyPropertyChanged(BR.nominal);
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplayType() {
        if (type.equals("LISTRIK")) {
            return "Listrik";
        } else {
            return "Air";
        }
    }

    public String getDisplayNominal() {
        return NumberFormat.getCurrencyInstance(new Locale("in", "id")).format(Integer.valueOf(nominal));
    }
}
