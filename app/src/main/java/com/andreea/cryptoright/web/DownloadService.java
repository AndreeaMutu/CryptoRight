package com.andreea.cryptoright.web;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andreea.cryptoright.db.CoinRoomDatabase;
import com.andreea.cryptoright.helper.Constants;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.CoinListResponse;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Collection;
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
                .baseUrl(Constants.COIN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoCompareService service = retrofit.create(CryptoCompareService.class);
        Call<CoinListResponse> coinListResponseCall = service.listCoins();
        Context context = getApplicationContext();
        final CoinRoomDatabase db = CoinRoomDatabase.getDatabase(context);

        coinListResponseCall.enqueue(new Callback<CoinListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinListResponse> call, @NonNull Response<CoinListResponse> response) {
                Log.d(TAG, "Response coins web service." + response);
                if (response.isSuccessful()) {
                    CoinListResponse responseBody = response.body();
                    new Thread(() -> {
                        if (responseBody != null) {
                            String baseImageUrl = responseBody.getBaseImageUrl();
                            String baseLinkUrl = responseBody.getBaseLinkUrl();
                            final Map<String, Coin> data = responseBody.getData();
                            Collection<Coin> values = data.values();

                            // update urls with base url
                            for (Coin coin : values) {
                                String imageUrl = coin.getImageUrl();
                                String url = coin.getUrl();
                                coin.setImageUrl(baseImageUrl + imageUrl);
                                coin.setUrl(baseLinkUrl + url);
                            }
                            db.coinDao().insertAll(values);
                        } else {
                            Log.e(TAG, "Coin list response is null. Nothing to insert in db.");
                        }
                    }).start();
                }
                jobFinished(job, false);
            }

            @Override
            public void onFailure(@NonNull Call<CoinListResponse> call, @NonNull Throwable t) {
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
