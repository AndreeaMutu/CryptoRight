package com.andreea.cryptoright.ui;

import android.content.res.Resources;
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

    private List<Pair<Integer, String>> mValues;

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
        Pair<Integer, String> labelValuePair = mValues.get(position);
        Resources resources = holder.binding.getRoot().getResources();
        holder.binding.setLabel(resources.getString(labelValuePair.first));
        holder.binding.setValue(labelValuePair.second);
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setDetails(List<Pair<Integer, String>> details) {

            mValues = details;
            notifyDataSetChanged();

    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        private CoinDetailItemBinding binding;

        public DetailViewHolder(CoinDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
