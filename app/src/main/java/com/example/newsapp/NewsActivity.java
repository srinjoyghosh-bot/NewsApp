package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private TextView mEmptyTextView;
    private ListView mListView;
    private List<News> newsResponseList;
    private NewsAdapter mAdapter;
    private EditText mSearchText;
    private ImageButton mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        mEmptyTextView = findViewById(R.id.empty_text);
        mListView=findViewById(R.id.news_list);
        mListView.setEmptyView(mEmptyTextView);
        newsResponseList=new ArrayList<News>();
        mAdapter=new NewsAdapter(this,newsResponseList);
        mListView.setAdapter(mAdapter);


        URL newsUrl=null;
        try {
             newsUrl=NetworkUtils.buildUrl();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (newsUrl==null)
            mEmptyTextView.setText("Some Error");
        else
        {
            new NewsQueryTask().execute(newsUrl);
        }
        mSearchText=(EditText)findViewById(R.id.movie_search);
        mSearchButton=(ImageButton)findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKey=mSearchText.getText().toString();

                if(TextUtils.isEmpty(searchKey))
                {
                    Toast toast;
                    Toast.makeText(NewsActivity.this,R.string.error_search,Toast.LENGTH_SHORT).show();

                }
                else
                {
                    mAdapter.clear();
                    URL newsSearchUrl=null;
                    try {
                        newsSearchUrl=NetworkUtils.buildSearchedUrl(searchKey);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (newsSearchUrl==null)
                        mEmptyTextView.setText(R.string.empty_result);
                    else
                    {
                        new NewsQueryTask().execute(newsSearchUrl);
                    }
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews=mAdapter.getItem(position);
                String urlString= currentNews.getWebUrl();
                Uri newsUri=Uri.parse(urlString);
                Intent newsIntent=new Intent(Intent.ACTION_VIEW,newsUri);
                startActivity(newsIntent);

            }
        });



    }
    public class NewsQueryTask extends AsyncTask<URL,Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            String jsonResponse=null;
            try {
                 jsonResponse=NetworkUtils.getResponse(url);
//                newsList=NetworkUtils.getNewsResponse(jsonResponse);

            } catch (IOException  e) {
                e.printStackTrace();
            }
            return jsonResponse;

        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            if (jsonResponse!=null)
            {
                List<News> newsList=null;
                try {
                   newsList=NetworkUtils.getNewsResponse(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (newsList!=null)
                mAdapter.addAll(newsList);
                else
                    mEmptyTextView.setText("onpostexecute error");

            }
        }
    }
}