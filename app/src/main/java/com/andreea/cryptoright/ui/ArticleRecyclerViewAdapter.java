package com.andreea.cryptoright.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.ArticleItemBinding;
import com.andreea.cryptoright.model.NewsArticle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolder> {

    private List<NewsArticle> articles = new ArrayList<>();

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ArticleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.article_item,
                        parent, false);
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochSecond(article.getPublishedOn());
            holder.binding.articleDate.setText(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_TIME));
        }
        holder.binding.getRoot().setOnClickListener(v -> {
            Context context = holder.binding.getRoot().getContext();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl())));
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
