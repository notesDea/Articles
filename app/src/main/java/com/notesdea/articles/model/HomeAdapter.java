package com.notesdea.articles.model;

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

public class HomeAdapter extends RecyclerView.Adapter{


    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_CONTENT = 2;

    private List<Article> mArticles;
    private Context mContext;

    public HomeAdapter(List<Article> articles, Context context) {
        mContext = context;
        mArticles = articles;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
//                new HeaderViewHolder(); todo 是否需要？
                break;
            case VIEW_TYPE_FOOTER:
//                new FooterViewHolder(); todo 是否需要？
                break;
            case VIEW_TYPE_CONTENT:
                View layout = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
                return new ContentViewHolder(layout);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            Article article = mArticles.get(position);
            ((ContentViewHolder) holder).textTitle.setText(article.getTitle());
            ((ContentViewHolder) holder).textDate.setText(
                    mContext.getString(R.string.main_date, article.getDate()));

        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //持有顶部刷新视图
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    //持有底部视图
    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    //持有内容视图
    class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDate;

        public ContentViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_item_title);
            textDate = (TextView) itemView.findViewById(R.id.text_item_date);
        }
    }
//
//    @Override
//    public int getCount() {
//        if (mArticles == null) {
//            return 0;
//        }
//        return mArticles.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mArticles.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            view = LayoutInflater.from(viewGroup.getContext()).
//                    inflate(R.layout.item_main, viewGroup, false);
//            view.setTag(new ViewHolder(view));
//        }
//        Article article = mArticles.get(i);
//        ViewHolder viewHolder = (ViewHolder) view.getTag();
//        viewHolder.textTitle.setText(article.getTitle());
//        viewHolder.textDate.setText(mContext.getString(R.string.main_date, article.getDate()));
//
//        return view;
//    }
//
//    public boolean add(Article article) {
//        return mArticles.add(article);
//    }
//
//    public Article remove(int position) {
//        return mArticles.remove(position);
//    }
}
