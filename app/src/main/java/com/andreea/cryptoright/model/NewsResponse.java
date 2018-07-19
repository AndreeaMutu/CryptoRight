package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsResponse {
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<NewsArticle> data = new ArrayList<>();

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NewsArticle> getData() {
        return data;
    }

    public void setData(List<NewsArticle> data) {
        this.data = data;
    }

}
