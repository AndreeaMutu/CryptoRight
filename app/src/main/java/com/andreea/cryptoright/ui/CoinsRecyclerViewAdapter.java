package com.andreea.cryptoright.ui;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.ui.CoinsFragment.OnListFragmentInteractionListener;
import com.andreea.cryptoright.ui.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CoinsRecyclerViewAdapter extends RecyclerView.Adapter<CoinsRecyclerViewAdapter.ViewHolder> {

    private List<Coin> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CoinsRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_coin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.get().load("https://cryptocompare.com"+mValues.get(position).getImageUrl()).into(holder.mImageView);
        holder.mContentView.setText(mValues.get(position).getCoinName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public Coin mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.coin_thumbnail);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
