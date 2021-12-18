package com.example.payfromhome.model;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.Locale;

public class RiwayatModel {
    @SerializedName("id")
        String id;

    @SerializedName("type")
        String type;

    @SerializedName("serial_id")
    String serial_id;

    @SerializedName("nominal")
        String nominal;

    @SerializedName("payment")
        String payment;

    @SerializedName("date")
        String date;

    public RiwayatModel(String id, String type, String serial_id, String nominal, String payment, String date) {
        this.id = id;
        this.type = type;
        this.serial_id = serial_id;
        this.nominal = nominal;
        this.payment = payment;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public String getNominal() {
        return nominal;
    }

    public String getPayment() {
        return payment;
    }

    public String getDate() {
        return date;
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
