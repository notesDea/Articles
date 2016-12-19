package com.notesdea.articles.controller;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.notesdea.articles.R;
import com.notesdea.articles.data.CachedDBHelper;
import com.notesdea.articles.model.OnSwitchFragmentListener;

public class MainActivity extends AppCompatActivity implements OnSwitchFragmentListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();
        mainFragment.setOnSwitchFragmentListener(this);//todo 测试行不行 是否有完成任务打勾 删除了数据库代码
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainFragment)
                .commit();
    }

    //切换 DetailFragment
    @Override
    public void onSwitchFragmentWithData(Bundle bundle) {
        DetailFragment detailFragment = new DetailFragment();

        detailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
