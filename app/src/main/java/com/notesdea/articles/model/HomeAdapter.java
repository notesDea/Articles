package com.notesdea.articles.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.notesdea.articles.R;

import java.util.List;

/**
 * Created by notesdea on 11/18/16.
 */

public class HomeAdapter extends RecyclerView.Adapter{


    private static final int VIEW_TYPE_CONTENT = 100;

    //文章的数据
    private List<Post> mPosts;
    private Context mContext;

    public HomeAdapter(List<Post> posts, Context context) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_CONTENT;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CONTENT:
                View layout = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
                return new ContentViewHolder(layout);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            //设置 item 视图
            Post post = mPosts.get(position);
            ((ContentViewHolder) holder).textTitle.setText(post.getTitle());
            ((ContentViewHolder) holder).textDate.setText(
                    mContext.getString(R.string.main_date, post.getDate()));

        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
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
}
