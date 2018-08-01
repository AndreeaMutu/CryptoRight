package com.andreea.cryptoright.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andreea.cryptoright.db.CoinDao;
import com.andreea.cryptoright.db.CoinRoomDatabase;
import com.andreea.cryptoright.db.WatchlistDao;
import com.andreea.cryptoright.helper.Constants;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.CoinPrice;
import com.andreea.cryptoright.model.CoinPriceResponse;
import com.andreea.cryptoright.model.CoinWithPrice;
import com.andreea.cryptoright.model.Watchlist;
import com.andreea.cryptoright.web.CryptoCompareService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinsViewModel extends AndroidViewModel {
    private static final String TAG = CoinsViewModel.class.getSimpleName();
    private CoinDao coinDao;
    private WatchlistDao watchlistDao;
    private final CryptoCompareService service;
    private String selectedCoin;
    private String refCcy;

    private LiveData<CoinWithPrice> coinDetails;

    public CoinsViewModel(@NonNull Application application) {
        super(application);
        // TODO add coin repository
        coinDao = CoinRoomDatabase.getDatabase(application).coinDao();
        watchlistDao = CoinRoomDatabase.getDatabase(application).watchlistDao();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.COIN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CryptoCompareService.class);
    }

    public LiveData<List<Coin>> getCoins() {
        return coinDao.getAllCoins();
    }

    private LiveData<Coin> getCoinById(String id) {
        return coinDao.getCoinById(id);
    }

    public LiveData<CoinWithPrice> getCoinPriceDetails(String coinId, String referenceCcy) {
        if (coinDetails == null || !coinId.equals(selectedCoin) || !referenceCcy.equals(refCcy)) {
            selectedCoin = coinId;
            refCcy = referenceCcy;
            coinDetails = Transformations.switchMap(getCoinById(coinId), coin -> {
                MutableLiveData<CoinWithPrice> coinWithPriceMutableLiveData = new MutableLiveData<>();
                if (coin != null) {
                    final CoinWithPrice coinWithPrice = new CoinWithPrice();
                    coinWithPrice.setCoin(coin);

                    String symbol = coin.getSymbol();
                    Call<CoinPriceResponse> coinPriceResponseCall = service.getCoinPrices(symbol, refCcy);
                    coinPriceResponseCall.enqueue(new Callback<CoinPriceResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CoinPriceResponse> call, @NonNull Response<CoinPriceResponse> response) {
                            if (response.isSuccessful()) {
                                CoinPriceResponse body = response.body();
                                Log.d(TAG, "Call to price data success: " + body);

                                if (body != null) {
                                    Map<String, Map<String, CoinPrice>> priceData = body.getPriceData();
                                    CoinPrice coinPrice = priceData.get(symbol).get(refCcy);
                                    coinWithPrice.setCoinPrice(coinPrice);
                                    coinWithPriceMutableLiveData.postValue(coinWithPrice);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CoinPriceResponse> call, @NonNull Throwable t) {
                            Log.e(TAG, "Call to price data failed", t);
                        }
                    });
                }

                return coinWithPriceMutableLiveData;
            });
        }
        return coinDetails;
    }

    public LiveData<Watchlist> getUserWatchlist(String userId) {
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
        Set<String> coinIds = new HashSet<>(updated.getUserCoinIds());
        coinIds.add(coinId);
        updated.setUserCoinIds(new ArrayList<>(coinIds));
        new InsertTask(watchlistDao).execute(updated);
    }

    public LiveData<List<Coin>> getWatchlistCoins(String userId) {
        return Transformations.switchMap(watchlistDao.getUserWatchlist(userId),
                input -> {
                    if (input != null) {
                        Log.d(TAG, "getWatchlistCoins: ids " + input.getUserCoinIds());
                        return coinDao.getCoinsByIds(input.getUserCoinIds());
                    } else return new MutableLiveData<>();
                });
    }

    private static class InsertTask extends AsyncTask<Watchlist, Void, Void> {
        private final WatchlistDao dao;

        private InsertTask(WatchlistDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Watchlist... input) {
            Watchlist watchlist = input[0];
            if (watchlist != null) {
                dao.insert(watchlist);
            } else {
                Log.e(TAG, "Can not insert null watchlist.");
            }
            return null;
        }
    }
}
