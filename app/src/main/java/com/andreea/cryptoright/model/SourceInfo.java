package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SourceInfo {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("img")
    @Expose
    private String img;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
