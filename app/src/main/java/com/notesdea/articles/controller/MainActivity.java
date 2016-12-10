package com.notesdea.articles.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.notesdea.articles.ItemClickListener;
import com.notesdea.articles.model.LoadOnScrollListener;
import com.notesdea.articles.R;
import com.notesdea.articles.model.HomeAdapter;
import com.notesdea.articles.model.NetworkUtils;
import com.notesdea.articles.model.Post;
import com.notesdea.articles.model.PostsWithStatus;
import com.notesdea.articles.model.WpPostInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainFragment.OnReplaceFragmentListener {

    private static final String TAG = "MainActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .commit();
    }

    //切换 Fragment
    //todo 切换后又要重新加载  Fragment 不重新加载 http://www.jianshu.com/p/91fa45c10eb7 http://waylenw.github.io/Android/android-fragment-change-one/
    @Override
    public void onReplace(String title, String content) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        detailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
