package com.example.payfromhome.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class mTransaction {
    @SerializedName("status")
        private String status;

    @SerializedName("message")
        private String message;

    @SerializedName("data")
        private JsonObject data;

    public mTransaction(String status, String message, JsonObject data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject userdata) {
        this.data = data;
    }
}
