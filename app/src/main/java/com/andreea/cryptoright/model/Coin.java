package com.andreea.cryptoright.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Coin {
    @PrimaryKey
    @NonNull
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Symbol")
    @Expose
    private String symbol;
    @SerializedName("CoinName")
    @Expose
    private String coinName;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("Algorithm")
    @Expose
    private String algorithm;
    @SerializedName("ProofType")
    @Expose
    private String proofType;
    @SerializedName("FullyPremined")
    @Expose
    private String fullyPremined;
    @SerializedName("TotalCoinSupply")
    @Expose
    private String totalCoinSupply;
    @SerializedName("PreMinedValue")
    @Expose
    private String preMinedValue;
    @SerializedName("TotalCoinsFreeFloat")
    @Expose
    private String totalCoinsFreeFloat;
    @Expose
    @SerializedName("SortOrder")
    private String sortOrder;
    @Expose
    @SerializedName("Sponsored")

    private Boolean sponsored;
    @SerializedName("IsTrading")
    @Expose
    private Boolean isTrading;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

    public String getFullyPremined() {
        return fullyPremined;
    }

    public void setFullyPremined(String fullyPremined) {
        this.fullyPremined = fullyPremined;
    }

    public String getTotalCoinSupply() {
        return totalCoinSupply;
    }

    public void setTotalCoinSupply(String totalCoinSupply) {
        this.totalCoinSupply = totalCoinSupply;
    }

    public String getPreMinedValue() {
        return preMinedValue;
    }

    public void setPreMinedValue(String preMinedValue) {
        this.preMinedValue = preMinedValue;
    }

    public String getTotalCoinsFreeFloat() {
        return totalCoinsFreeFloat;
    }

    public void setTotalCoinsFreeFloat(String totalCoinsFreeFloat) {
        this.totalCoinsFreeFloat = totalCoinsFreeFloat;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Boolean getIsTrading() {
        return isTrading;
    }

    public void setIsTrading(Boolean isTrading) {
        this.isTrading = isTrading;
    }


    @Override
    public String toString() {
        return "Coin{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
