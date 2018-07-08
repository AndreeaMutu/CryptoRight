package com.andreea.cryptoright.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.andreea.cryptoright.db.CoinDao;
import com.andreea.cryptoright.db.CoinRoomDatabase;
import com.andreea.cryptoright.model.Coin;

import java.util.List;

public class CoinsViewModel extends AndroidViewModel {
    private CoinDao coinDao;
    private LiveData<List<Coin>> coinsLiveData;
    public CoinsViewModel(@NonNull Application application) {
        super(application);
        coinDao = CoinRoomDatabase.getDatabase(application).coinDao();
        coinsLiveData = coinDao.getAllCoins();
    }

    public LiveData<List<Coin>> getCoins() {
        return coinsLiveData;
    }

    public LiveData<Coin> getCoinById(String id) {
        return coinDao.getCoinById(id);
    }
}
