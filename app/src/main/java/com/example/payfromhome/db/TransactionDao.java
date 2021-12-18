package com.example.payfromhome.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.payfromhome.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions")
    List<Transaction> getAll();

    @Insert
    void insert(Transaction transaction);
}
