package com.andreea.cryptoright.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.CoinItemBinding;
import com.andreea.cryptoright.model.Coin;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;
import java.util.Objects;

public class CoinsRecyclerViewAdapter extends RecyclerView.Adapter<CoinsRecyclerViewAdapter.CoinViewHolder> {

    private List<Coin> mValues;
    private final CoinClickCallback mListener;

    public CoinsRecyclerViewAdapter(CoinClickCallback listener) {
        mListener = listener;
    }

    @Override
    public CoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CoinItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.coin_item,
                        parent, false);
        binding.setCallback(mListener);
        return new CoinViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoinViewHolder holder, int position) {
        Coin coin = mValues.get(position);
        holder.binding.setCoin(coin);
        String imageUrl = coin.getImageUrl();
        Picasso picasso = Picasso.get();
        RequestCreator loadRequest;
        if (TextUtils.isEmpty(imageUrl)) {
            loadRequest = picasso.load(R.drawable.ic_coin_black_24dp);
        } else {
            loadRequest = picasso.load(imageUrl);
        }
        loadRequest.placeholder(android.R.drawable.stat_notify_error).into(holder.binding.coinThumbnail);
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setCoinList(List<Coin> coinList) {
        if (mValues == null) {
            mValues = coinList;
            notifyItemRangeInserted(0, coinList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mValues.size();
                }

                @Override
                public int getNewListSize() {
                    return coinList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mValues.get(oldItemPosition).getId().equals(coinList.get(newItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Coin newCoin = coinList.get(newItemPosition);
                    Coin oldCoin = mValues.get(oldItemPosition);
                    return newCoin.getId().equals(oldCoin.getId())
                            && Objects.equals(newCoin.getCoinName(), oldCoin.getCoinName())
                            && Objects.equals(newCoin.getName(), oldCoin.getName())
                            && newCoin.getSymbol().equals(oldCoin.getSymbol());
                }
            });
            mValues = coinList;
            result.dispatchUpdatesTo(this);
        }
    }

    public class CoinViewHolder extends RecyclerView.ViewHolder {

        private CoinItemBinding binding;

        public CoinViewHolder(CoinItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
