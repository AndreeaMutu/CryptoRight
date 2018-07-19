package com.andreea.cryptoright.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsArticle {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("guid")
    @Expose
    private String guid;

    @SerializedName("published_on")
    @Expose
    // TODO: 7/19/18 parse with  Instant instant = Instant.ofEpochSecond(1531996260);
    private Long publishedOn;

    @SerializedName("imageurl")
    @Expose
    private String imageurl;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("tags")
    @Expose
    private String tags;

    @SerializedName("categories")
    @Expose
    private String categories;

    @SerializedName("upvotes")
    @Expose
    private String upvotes;

    @SerializedName("downvotes")
    @Expose
    private String downvotes;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("source_info")
    @Expose
    private SourceInfo sourceInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Long getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Long publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(String downvotes) {
        this.downvotes = downvotes;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }
}
