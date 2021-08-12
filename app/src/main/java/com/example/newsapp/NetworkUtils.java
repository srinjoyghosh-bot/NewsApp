package com.example.newsapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    final static String GAURDIAN_BASE_URL="https://content.guardianapis.com/search";
    final static String Query="api-key";
    final static String QUERY_TAG="q";
    final static String KEY="e6f0a2b9-51d4-4ba1-9243-96472b325db7";

    public static URL buildUrl() throws MalformedURLException {
        Uri  buildUri=Uri.parse(GAURDIAN_BASE_URL).buildUpon()
                .appendQueryParameter(Query,KEY)
                .build();
        URL url=new URL(buildUri.toString());
        return url;


    }
    public static URL buildSearchedUrl(String key) throws MalformedURLException {
        Uri  buildUri=Uri.parse(GAURDIAN_BASE_URL).buildUpon()
                .appendQueryParameter(Query,KEY)
                .appendQueryParameter(QUERY_TAG,key)
                .build();
        URL url=new URL(buildUri.toString());
        return url;


    }
    public static String getResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");
            boolean hasInput = sc.hasNext();
            if (hasInput == true) {
                return sc.next();

            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }

    }
    public static List<News> getNewsResponse(String jsonResponse) throws JSONException {
        List<News> newsResults = new ArrayList<News>();
        JSONObject json=new JSONObject(jsonResponse);
        JSONObject response=json.optJSONObject("response");
        JSONArray results=response.getJSONArray("results");
        for (int i=0;i<results.length();i++)
        {
            JSONObject currentNews= results.getJSONObject(i);
            String headline=currentNews.getString("webTitle");
            String urlString= currentNews.getString("webUrl");
            String sectionName=currentNews.getString("sectionName");
            News news=new News(headline,urlString,sectionName);
            newsResults.add(news);

        }
        return newsResults;
    }

}
