package com.andreea.cryptoright.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.Watchlist;


@Database(entities = {Coin.class, Watchlist.class}, version = 1, exportSchema = false)
public abstract class CoinRoomDatabase extends RoomDatabase {

    private static CoinRoomDatabase INSTANCE;

    public static CoinRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoinRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoinRoomDatabase.class, "coin_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract CoinDao coinDao();

    public abstract WatchlistDao watchlistDao();

    private static Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };
}
