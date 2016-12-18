package com.notesdea.articles.controller;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.notesdea.articles.R;
import com.notesdea.articles.data.CachedDBHelper;

public class MainActivity extends AppCompatActivity implements MainFragment.OnSwitchFragmentListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .commit();

        //todo 创建数据库
        CachedDBHelper dbHelper = new CachedDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    //切换 DetailFragment
    @Override
    public void onSwitchDetailFragment(String title, String content) {
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
