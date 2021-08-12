package com.example.newsapp;

public class News {

    private  String mTitle;
    private String mWebUrl;
    private String mSection;

    public News(String title,String webUrl,String sectionName) {
        mTitle=title;
        mWebUrl=webUrl;
        mSection=sectionName;
    }

    public  String getTitle() {
        return mTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getSection() {
        return mSection;
    }
}
