package com.notesdea.articles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.notesdea.articles.R;
import com.notesdea.articles.model.Article;

import java.util.List;

/**
 * Created by notesdea on 11/18/16.
 */

public class HomeAdapter extends BaseAdapter {
    private List<Article> mArticles;
    private Context mContext;

    public HomeAdapter(List<Article> articles, Context context) {
        mArticles = articles;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDate;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_item_title);
            textDate = (TextView) itemView.findViewById(R.id.text_item_date);
        }
    }

    @Override
    public int getCount() {
        if (mArticles == null) {
            return 0;
        }
        return mArticles.size();
    }

    @Override
    public Object getItem(int i) {
        return mArticles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_main, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }
        Article article = mArticles.get(i);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.textTitle.setText(article.getTitle());
        viewHolder.textDate.setText(mContext.getString(R.string.main_date, article.getDate()));

        return view;
    }

    public boolean add(Article article) {
        return mArticles.add(article);
    }

    public Article remove(int position) {
        return mArticles.remove(position);
    }
}
