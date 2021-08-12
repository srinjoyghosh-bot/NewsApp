package com.example.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView mEmptyTextView;
    private ListView mListView;
    private List<News> newsResponseList;
    private NewsAdapter mAdapter;
    private EditText mSearchText;
    private ImageButton mSearchButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mEmptyTextView = view.findViewById(R.id.empty_text);
        mListView=view.findViewById(R.id.news_list);
        mListView.setEmptyView(mEmptyTextView);
        newsResponseList=new ArrayList<News>();
        mAdapter=new NewsAdapter(getContext(),newsResponseList);
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
        mSearchText=(EditText)view.findViewById(R.id.movie_search);
        mSearchButton=(ImageButton)view.findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKey=mSearchText.getText().toString();

                if(TextUtils.isEmpty(searchKey))
                {
                    Toast toast;
                    Toast.makeText(getActivity(),R.string.error_search,Toast.LENGTH_SHORT).show();

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
                ((Activity)view.getContext()).startActivity(newsIntent);
                //startActivity(newsIntent);

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
//                newsList=NetworkUtils.getNewsResponse(jsonResponse);

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
                    mEmptyTextView.setText("onpostexecute error");

            }
        }
    }
}