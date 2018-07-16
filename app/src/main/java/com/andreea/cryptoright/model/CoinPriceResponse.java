package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CoinPriceResponse {
    @SerializedName("DISPLAY")
    @Expose
    private Map<String, Map<String, CoinPrice>> priceData;

    public Map<String, Map<String, CoinPrice>> getPriceData() {
        return priceData;
    }

    public void setPriceData(Map<String, Map<String, CoinPrice>> priceData) {
        this.priceData = priceData;
    }
}
