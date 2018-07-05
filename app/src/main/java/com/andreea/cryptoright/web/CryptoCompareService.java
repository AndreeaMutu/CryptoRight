package com.andreea.cryptoright.web;


import com.andreea.cryptoright.model.CoinListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CryptoCompareService {
    @GET("/data/all/coinlist")
    Call<CoinListResponse> listCoins();
}
