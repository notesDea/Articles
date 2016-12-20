package com.notesdea.articles.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.notesdea.articles.R;

public class PostsActivity extends AppCompatActivity {

    //文章标题
    private TextView mTextTitle,
    //文章内容
            mTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        initView();
        initData();
    }

    //初始化视图
    private void initView() {
        mTextTitle = (TextView) findViewById(R.id.text_posts_title);
        mTextContent = (TextView) findViewById(R.id.text_posts_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_posts);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        mTextTitle.setText(title);
        getSupportActionBar().setTitle(title);
        mTextContent.setText(content);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
