package com.example.payfromhome.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.payfromhome.model.Transaction;

@Database(entities = {Transaction.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();
}
