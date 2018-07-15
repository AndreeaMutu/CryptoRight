package com.andreea.cryptoright.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.CoinDetailItemBinding;

import java.util.List;

public class CoinsDetailsRecyclerViewAdapter extends RecyclerView.Adapter<CoinsDetailsRecyclerViewAdapter.DetailViewHolder> {

    private List<Pair<String, String>> mValues;

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CoinDetailItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.coin_detail_item,
                        parent, false);
        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailViewHolder holder, int position) {
        Pair<String, String> labelValuePair = mValues.get(position);
        holder.binding.setLabel(labelValuePair.first);
        holder.binding.setValue(labelValuePair.second);
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setDetails(List<Pair<String, String>> details) {
        if (mValues == null) {
            mValues = details;
            notifyDataSetChanged();
        }
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        private CoinDetailItemBinding binding;

        public DetailViewHolder(CoinDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
