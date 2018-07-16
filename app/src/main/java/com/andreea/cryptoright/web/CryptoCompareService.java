package com.andreea.cryptoright.web;


import com.andreea.cryptoright.model.CoinListResponse;
import com.andreea.cryptoright.model.CoinPriceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CryptoCompareService {
    @GET("/data/all/coinlist")
    Call<CoinListResponse> listCoins();

    @GET("/data/pricemultifull")
    Call<CoinPriceResponse> getCoinPrices(@Query("fsyms") String fromSymbols, @Query("tsyms") String toSymbols);
}
