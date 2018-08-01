package com.andreea.cryptoright.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.ArticleItemBinding;
import com.andreea.cryptoright.model.NewsArticle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.joda.time.format.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ArticleViewHolder> {

    private List<NewsArticle> articles = new ArrayList<>();

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArticleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.article_item,
                        parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, int position) {
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

        String date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant instant = Instant.ofEpochSecond(article.getPublishedOn());
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                            .withLocale(Locale.getDefault())
                            .withZone(ZoneId.systemDefault());
            date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);

        } else {
            org.joda.time.Instant instant = org.joda.time.Instant.ofEpochSecond(article.getPublishedOn());
            org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
            date = formatter.print(instant);
        }
        holder.binding.articleDate.setText(date);
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

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        private ArticleItemBinding binding;

        ArticleViewHolder(ArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
