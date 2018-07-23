package com.andreea.cryptoright.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andreea.cryptoright.model.Watchlist;

@Dao
public interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Watchlist watchlist);

    @Query("SELECT * from Watchlist WHERE userId = :user")
    LiveData<Watchlist> getUserWatchlist (String user);
}
