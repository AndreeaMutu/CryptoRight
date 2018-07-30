package com.andreea.cryptoright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentArticleListBinding;
import com.andreea.cryptoright.helper.Constants;

public class NewsFragment extends Fragment {

    private int mColumnCount = 2;
    private FragmentArticleListBinding mBinding;
    private ArticleRecyclerViewAdapter mAdapter;

    public NewsFragment() {
    }

    @SuppressWarnings("unused")
    public static NewsFragment newInstance(int columnCount) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(Constants.ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final NewsViewModel viewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        mBinding.setIsLoading(true);
        viewModel.getArticles().observe(this, articles -> {
            if (articles != null) {
                mBinding.setIsLoading(false);
                mAdapter.setArticles(articles);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);
        View root = mBinding.getRoot();
        Context context = root.getContext();
        if (mColumnCount <= 1) {
            mBinding.articleList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mBinding.articleList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mAdapter = new ArticleRecyclerViewAdapter();
        mBinding.articleList.setAdapter(mAdapter);
        return root;
    }
}
