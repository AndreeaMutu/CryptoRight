package com.andreea.cryptoright.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.db.CoinDao;
import com.andreea.cryptoright.db.CoinRoomDatabase;
import com.andreea.cryptoright.db.WatchlistDao;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.CoinPrice;
import com.andreea.cryptoright.model.CoinPriceResponse;
import com.andreea.cryptoright.model.Watchlist;
import com.andreea.cryptoright.web.CryptoCompareService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinsViewModel extends AndroidViewModel {
    private static final String TAG = CoinsViewModel.class.getSimpleName();
    private CoinDao coinDao;
    private WatchlistDao watchlistDao;
    private LiveData<List<Coin>> coinsLiveData;
    public CoinsViewModel(@NonNull Application application) {
        super(application);
        coinDao = CoinRoomDatabase.getDatabase(application).coinDao();
        watchlistDao = CoinRoomDatabase.getDatabase(application).watchlistDao();
        coinsLiveData = coinDao.getAllCoins();
    }

    public LiveData<List<Coin>> getCoins() {
        return coinsLiveData;
    }

    public LiveData<Coin> getCoinById(String id) {
        return coinDao.getCoinById(id);
    }

    public LiveData<List<Pair<Integer, String>>> getCoinDetails(String coinId) {
        return Transformations.map(coinDao.getCoinById(coinId), coin -> {
            List<Pair<Integer, String>> details = new ArrayList<>();
            details.add(new Pair<>(R.string.label_coin_name, coin.getCoinName()));
            details.add(new Pair<>(R.string.label_coin_symbol, coin.getSymbol()));
            details.add(new Pair<>(R.string.label_coin_algorithm, coin.getAlgorithm()));
            details.add(new Pair<>(R.string.label_coin_proof_type, coin.getProofType()));
            details.add(new Pair<>(R.string.label_coin_supply, coin.getTotalCoinSupply()));
            return details;
        });
    }

    public LiveData<List<Pair<Integer, String>>> getCoinPriceDetails(String coinSymbol, String referenceCcy) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoCompareService service = retrofit.create(CryptoCompareService.class);
        Call<CoinPriceResponse> coinPriceResponseCall = service.getCoinPrices(coinSymbol, referenceCcy);
        MutableLiveData<CoinPrice> priceDetails = new MutableLiveData<>();
        coinPriceResponseCall.enqueue(new Callback<CoinPriceResponse>() {
            @Override
            public void onResponse(Call<CoinPriceResponse> call, Response<CoinPriceResponse> response) {
                if (response.isSuccessful()){
                    CoinPriceResponse body = response.body();
                    Map<String, Map<String, CoinPrice>> priceData = body.getPriceData();
                    CoinPrice coinPrice = priceData.get(coinSymbol).get(referenceCcy);
                    priceDetails.postValue(coinPrice);
                }
            }

            @Override
            public void onFailure(Call<CoinPriceResponse> call, Throwable t) {
                Log.e(TAG, "Call to price data failed", t);
            }
        });

        return
                Transformations.map(priceDetails, priceData -> {
                    List<Pair<Integer, String>> details = new ArrayList<>();
                    details.add(new Pair<>(R.string.label_coin_price, priceData.getPrice()));
                    details.add(new Pair<>(R.string.label_coin_market, priceData.getMarket()));
                    details.add(new Pair<>(R.string.label_coin_open_day, priceData.getOpenday()));
                    details.add(new Pair<>(R.string.label_coin_high_day, priceData.getHighday()));
                    details.add(new Pair<>(R.string.label_coin_low_day, priceData.getLowday()));
                    details.add(new Pair<>(R.string.label_coin_daily_change, priceData.getChangepctday()+" %"));
                    return details;
                });
    }

    public LiveData<Watchlist> getUserWatchlist(String userId){
       return watchlistDao.getUserWatchlist(userId);
    }

    public void addCoinToWatchlist(String userId, String coinId, Watchlist existing) {
       Watchlist updated;
       if (existing == null) {
           updated = new Watchlist();
       } else {
           updated = existing;
       }
       updated.setUserId(userId);
       updated.addCoin(coinId);
       new Thread(() -> watchlistDao.insert(updated)).start();
    }

    public LiveData<List<Coin>> getWatchlistCoins(String userId) {
        return Transformations.switchMap(watchlistDao.getUserWatchlist(userId),
                input -> {
                    if (input != null) {
                        return coinDao.getCoinsByIds(input.getUserCoinIds());
                    } else return new MutableLiveData<>();
                });
    }
}
