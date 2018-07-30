package com.andreea.cryptoright.model;

public class CoinWithPrice {
    private Coin coin;
    private CoinPrice coinPrice;

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public CoinPrice getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(CoinPrice coinPrice) {
        this.coinPrice = coinPrice;
    }
}
