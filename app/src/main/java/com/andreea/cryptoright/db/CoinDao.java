package com.andreea.cryptoright.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andreea.cryptoright.model.Coin;

import java.util.Collection;
import java.util.List;

@Dao
public interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Coin coin);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<Coin> coin);

    @Query("SELECT * from Coin ORDER BY sortOrder ASC")
    LiveData<List<Coin>> getAllCoins();

    @Query("SELECT * from Coin WHERE id = :id")
    LiveData<Coin> getCoinById(String id);
}
