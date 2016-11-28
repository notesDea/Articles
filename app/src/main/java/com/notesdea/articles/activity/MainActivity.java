package com.notesdea.articles.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.notesdea.articles.R;
import com.notesdea.articles.adapter.HomeAdapter;
import com.notesdea.articles.model.Article;
import com.notesdea.articles.model.ArticleFactory;
import com.notesdea.articles.view.RefreshView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshView mRefreshView;

    private ListView mListArticles;
    private List<Article> mArticles = ArticleFactory.getArticles();
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRefreshView = (RefreshView) findViewById(R.id.refresh_main);
        mAdapter = new HomeAdapter(mArticles, getApplicationContext());
        mListArticles = (ListView) findViewById(R.id.list_articles);
        mListArticles.setAdapter(mAdapter);

        mRefreshView.setOnRefreshListener(new RefreshView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mRefreshView.finishRefreshing();
            }
        });
    }
}
