package com.andreea.cryptoright.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andreea.cryptoright.model.NewsArticle;
import com.andreea.cryptoright.model.NewsResponse;
import com.andreea.cryptoright.web.CryptoCompareService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsViewModel extends AndroidViewModel {
    private static final String TAG = NewsViewModel.class.getSimpleName();

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<NewsArticle>> getArticles() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoCompareService service = retrofit.create(CryptoCompareService.class);
        Call<NewsResponse> coinPriceResponseCall = service.getNews("EN");
        MutableLiveData<List<NewsArticle>> newsLiveData = new MutableLiveData<>();
        coinPriceResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse body = response.body();
                    List<NewsArticle> articles = null;
                    if (body != null) {
                        articles = body.getData();
                        newsLiveData.postValue(articles);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Call to news service failed", t);
            }
        });

        return newsLiveData;
    }
}
