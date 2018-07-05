package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class CoinListResponse {
    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BaseImageUrl")
    @Expose
    private String baseImageUrl;
    @SerializedName("BaseLinkUrl")
    @Expose
    private String baseLinkUrl;
    @SerializedName("Data")
    @Expose
    private Map<String, Coin> data = new HashMap<>();


    public Map<String, Coin> getData() {
        return data;
    }

    public void setData(Map<String, Coin> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CoinListResponse{" +
                "response='" + response + '\'' +
                ", message='" + message + '\'' +
                ", baseImageUrl='" + baseImageUrl + '\'' +
                ", baseLinkUrl='" + baseLinkUrl + '\'' +
                ", data=" + data +
                '}';
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    public String getBaseLinkUrl() {
        return baseLinkUrl;
    }

    public void setBaseLinkUrl(String baseLinkUrl) {
        this.baseLinkUrl = baseLinkUrl;
    }
}
