package com.andreea.cryptoright.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.andreea.cryptoright.db.Converters;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Watchlist {
    @PrimaryKey
    @NonNull
    private String userId;

    @TypeConverters({Converters.class})
    private List<String> userCoinIds = new ArrayList<>();

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public List<String> getUserCoinIds() {
        return userCoinIds;
    }

    public void setUserCoinIds(List<String> userCoinIds) {
        this.userCoinIds = userCoinIds;
    }

}
