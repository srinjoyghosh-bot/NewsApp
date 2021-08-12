package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context,  @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View listItemView=convertView;
       if (listItemView==null)
       {
           listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
       }
        TextView titleView=(TextView) listItemView.findViewById(R.id.news_headline);
       News currentNews=getItem(position);
       titleView.setText(currentNews.getTitle());
       TextView sectionView=(TextView) listItemView.findViewById(R.id.section_name);
       sectionView.setText(currentNews.getSection());

       return listItemView;


    }
}
