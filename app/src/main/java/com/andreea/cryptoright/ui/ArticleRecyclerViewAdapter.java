package com.andreea.cryptoright.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.ArticleItemBinding;
import com.andreea.cryptoright.model.NewsArticle;
import com.andreea.cryptoright.ui.ArticleFragment.OnListFragmentInteractionListener;
import com.andreea.cryptoright.ui.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private List<NewsArticle> articles = new ArrayList<>();

    public ArticleRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ArticleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.article_item,
                        parent, false);
        //binding.setCallback(mListener);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {
        NewsArticle article = articles.get(position);
        holder.binding.setArticle(article);
        String imageUrl = article.getImageurl();
        Picasso picasso = Picasso.get();
        RequestCreator loadRequest;
        if (TextUtils.isEmpty(imageUrl)) {
            loadRequest = picasso.load(R.drawable.ic_coin_black_24dp);
        } else {
            loadRequest = picasso.load(imageUrl);
        }
        loadRequest.placeholder(android.R.drawable.stat_notify_error).into(holder.binding.articleImage);

        // TODO add article date

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(article);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(List<NewsArticle> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        private ArticleItemBinding binding;

        public ArticleViewHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
