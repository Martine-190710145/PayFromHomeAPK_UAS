package com.example.payfromhome.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class mResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<RiwayatModel> items;

    public mResponse(String status, String message, ArrayList<RiwayatModel> items) {
        this.status = status;
        this.message = message;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = message;
    }

    public ArrayList<RiwayatModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<RiwayatModel> items) {
        this.items = items;
    }
}
