package com.example.payfromhome.db;

import android.content.Context;

import androidx.room.Room;

public class TransactionDatabase {
    private Context context;
    private static TransactionDatabase transactionDatabase;

    private AppDatabase database;

    public TransactionDatabase(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context, AppDatabase.class, "pay_from_hom_db")
                .allowMainThreadQueries()
                .build();
    }

    public static synchronized TransactionDatabase getInstance(Context context) {
        if (transactionDatabase == null) {
            transactionDatabase = new TransactionDatabase(context);
        }
        return transactionDatabase;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
