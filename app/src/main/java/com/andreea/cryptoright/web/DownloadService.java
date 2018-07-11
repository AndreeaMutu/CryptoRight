package com.andreea.cryptoright.web;

import android.content.Context;
import android.util.Log;

import com.andreea.cryptoright.db.CoinRoomDatabase;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.CoinListResponse;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadService extends JobService {
    private static final String TAG = DownloadService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoCompareService service = retrofit.create(CryptoCompareService.class);
        Call<CoinListResponse> coins = service.listCoins();
        Context context = getApplicationContext();
        final CoinRoomDatabase db = CoinRoomDatabase.getDatabase(context);
        coins.enqueue(new Callback<CoinListResponse>() {
            @Override
            public void onResponse(Call<CoinListResponse> call, Response<CoinListResponse> response) {
                Log.d(TAG, "Response web service."+ response);
                if (response.isSuccessful()) {
                    CoinListResponse responseBody = response.body();
                    final Map<String, Coin> data = responseBody.getData();
                    Log.d(TAG, "Successfully retrieved coins from web service."+ responseBody);
                    new Thread(() -> {
                        for (Coin coin : data.values()) {
                            String imageUrl = coin.getImageUrl();
                            String url = coin.getUrl();
                            coin.setImageUrl(responseBody.getBaseImageUrl() + imageUrl);
                            coin.setUrl(responseBody.getBaseLinkUrl() + url);
                        }
                        db.coinDao().insertAll(data.values());
                    }).start();

                }
                jobFinished(job, false);
            }

            @Override
            public void onFailure(Call<CoinListResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                jobFinished(job, false);
            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
