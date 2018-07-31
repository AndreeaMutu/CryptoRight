package com.andreea.cryptoright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentArticleListBinding;

public class NewsFragment extends Fragment {

    private FragmentArticleListBinding mBinding;
    private ArticleRecyclerViewAdapter mAdapter;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mAdapter = new ArticleRecyclerViewAdapter();
        mBinding.articleList.setAdapter(mAdapter);
        return root;
    }
}
