package com.example.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TechFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TechFragment extends Fragment {
    private ListView mListView;
    private List<News> newsResponseList;
    private NewsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView noDataView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TechFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TechFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TechFragment newInstance(String param1, String param2) {
        TechFragment fragment = new TechFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tech, container, false);
        mListView=view.findViewById(R.id.tech_news_list);
        mProgressBar=view.findViewById(R.id.progress_tech);
        noDataView=view.findViewById(R.id.no_data_text_tech);
        mListView.setEmptyView(mProgressBar);
        newsResponseList=new ArrayList<News>();
        mAdapter=new NewsAdapter(getContext(),newsResponseList);
        mListView.setAdapter(mAdapter);
        URL newsUrl=null;
        try {
            newsUrl=NetworkUtils.buildSearchedUrl("Technology");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (newsUrl==null)
        {
            mListView.setVisibility(View.INVISIBLE);
            noDataView.setVisibility(View.VISIBLE);
        }
        else
        {
            mListView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.INVISIBLE);
            new TechFragment.NewsQueryTask().execute(newsUrl);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews=mAdapter.getItem(position);
                String urlString= currentNews.getWebUrl();
                String newsType=currentNews.getSection();

                Intent intent=new Intent(getContext(),WebViewActivity.class);
                intent.putExtra("url",urlString);
                intent.putExtra("section",newsType);
                startActivity(intent);

            }
        });
        return view;
    }
    public class NewsQueryTask extends AsyncTask<URL,Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            String jsonResponse=null;
            try {
                jsonResponse=NetworkUtils.getResponse(url);


            } catch (IOException e) {
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
                {
                    mListView.setVisibility(View.INVISIBLE);
                    noDataView.setVisibility(View.VISIBLE);
                }


            }
        }
    }
}