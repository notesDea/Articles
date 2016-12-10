package com.notesdea.articles.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notesdea.articles.R;

/**
 * Created by notes on 2016/12/7.
 */

public class DetailFragment extends Fragment {

    //文章的标题
    private TextView mTextTitle,
        //文章的内容
        mTextContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mTextTitle = (TextView) root.findViewById(R.id.text_detail_title);
        mTextContent = (TextView) root.findViewById(R.id.text_detail_content);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        mTextTitle.setText(title);
        mTextContent.setText(content);
    }
}
