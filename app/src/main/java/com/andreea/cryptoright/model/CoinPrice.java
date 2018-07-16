package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinPrice {
    @SerializedName("FROMSYMBOL")
    @Expose
    private String fromSymbol;
    @SerializedName("TOSYMBOL")
    @Expose
    private String toSymbol;
    @SerializedName("MARKET")
    @Expose
    private String market;
    @SerializedName("PRICE")
    @Expose
    private String price;
    @SerializedName("LASTUPDATE")
    @Expose
    private String lastupdate;
    @SerializedName("LASTVOLUME")
    @Expose
    private String lastvolume;
    @SerializedName("LASTVOLUMETO")
    @Expose
    private String lastvolumeto;
    @SerializedName("LASTTRADEID")
    @Expose
    private String lasttradeid;
    @SerializedName("VOLUMEDAY")
    @Expose
    private String volumeday;
    @SerializedName("VOLUMEDAYTO")
    @Expose
    private String volumedayto;
    @SerializedName("VOLUME24HOUR")
    @Expose
    private String volume24Hour;
    @SerializedName("VOLUME24HOURTO")
    @Expose
    private String volume24Hourto;
    @SerializedName("OPENDAY")
    @Expose
    private String openday;
    @SerializedName("HIGHDAY")
    @Expose
    private String highday;
    @SerializedName("LOWDAY")
    @Expose
    private String lowday;
    @SerializedName("OPEN24HOUR")
    @Expose
    private String open24Hour;
    @SerializedName("HIGH24HOUR")
    @Expose
    private String high24Hour;
    @SerializedName("LOW24HOUR")
    @Expose
    private String low24Hour;
    @SerializedName("LASTMARKET")
    @Expose
    private String lastmarket;
    @SerializedName("CHANGE24HOUR")
    @Expose
    private String change24Hour;
    @SerializedName("CHANGEPCT24HOUR")
    @Expose
    private String changepct24Hour;
    @SerializedName("CHANGEDAY")
    @Expose
    private String changeday;
    @SerializedName("CHANGEPCTDAY")
    @Expose
    private String changepctday;
    @SerializedName("SUPPLY")
    @Expose
    private String supply;
    @SerializedName("MKTCAP")
    @Expose
    private String mktcap;
    @SerializedName("TOTALVOLUME24H")
    @Expose
    private String totalvolume24H;
    @SerializedName("TOTALVOLUME24HTO")
    @Expose
    private String totalvolume24Hto;

    public String getFromSymbol() {
        return fromSymbol;
    }

    public void setFromSymbol(String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public String getToSymbol() {
        return toSymbol;
    }

    public void setToSymbol(String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getLastvolume() {
        return lastvolume;
    }

    public void setLastvolume(String lastvolume) {
        this.lastvolume = lastvolume;
    }

    public String getLastvolumeto() {
        return lastvolumeto;
    }

    public void setLastvolumeto(String lastvolumeto) {
        this.lastvolumeto = lastvolumeto;
    }

    public String getLasttradeid() {
        return lasttradeid;
    }

    public void setLasttradeid(String lasttradeid) {
        this.lasttradeid = lasttradeid;
    }

    public String getVolumeday() {
        return volumeday;
    }

    public void setVolumeday(String volumeday) {
        this.volumeday = volumeday;
    }

    public String getVolumedayto() {
        return volumedayto;
    }

    public void setVolumedayto(String volumedayto) {
        this.volumedayto = volumedayto;
    }

    public String getVolume24Hour() {
        return volume24Hour;
    }

    public void setVolume24Hour(String volume24Hour) {
        this.volume24Hour = volume24Hour;
    }

    public String getVolume24Hourto() {
        return volume24Hourto;
    }

    public void setVolume24Hourto(String volume24Hourto) {
        this.volume24Hourto = volume24Hourto;
    }

    public String getOpenday() {
        return openday;
    }

    public void setOpenday(String openday) {
        this.openday = openday;
    }

    public String getHighday() {
        return highday;
    }

    public void setHighday(String highday) {
        this.highday = highday;
    }

    public String getLowday() {
        return lowday;
    }

    public void setLowday(String lowday) {
        this.lowday = lowday;
    }

    public String getOpen24Hour() {
        return open24Hour;
    }

    public void setOpen24Hour(String open24Hour) {
        this.open24Hour = open24Hour;
    }

    public String getHigh24Hour() {
        return high24Hour;
    }

    public void setHigh24Hour(String high24Hour) {
        this.high24Hour = high24Hour;
    }

    public String getLow24Hour() {
        return low24Hour;
    }

    public void setLow24Hour(String low24Hour) {
        this.low24Hour = low24Hour;
    }

    public String getLastmarket() {
        return lastmarket;
    }

    public void setLastmarket(String lastmarket) {
        this.lastmarket = lastmarket;
    }

    public String getChange24Hour() {
        return change24Hour;
    }

    public void setChange24Hour(String change24Hour) {
        this.change24Hour = change24Hour;
    }

    public String getChangepct24Hour() {
        return changepct24Hour;
    }

    public void setChangepct24Hour(String changepct24Hour) {
        this.changepct24Hour = changepct24Hour;
    }

    public String getChangeday() {
        return changeday;
    }

    public void setChangeday(String changeday) {
        this.changeday = changeday;
    }

    public String getChangepctday() {
        return changepctday;
    }

    public void setChangepctday(String changepctday) {
        this.changepctday = changepctday;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getMktcap() {
        return mktcap;
    }

    public void setMktcap(String mktcap) {
        this.mktcap = mktcap;
    }

    public String getTotalvolume24H() {
        return totalvolume24H;
    }

    public void setTotalvolume24H(String totalvolume24H) {
        this.totalvolume24H = totalvolume24H;
    }

    public String getTotalvolume24Hto() {
        return totalvolume24Hto;
    }

    public void setTotalvolume24Hto(String totalvolume24Hto) {
        this.totalvolume24Hto = totalvolume24Hto;
    }
}