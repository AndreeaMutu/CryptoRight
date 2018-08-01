package com.andreea.cryptoright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinListBinding;
import com.andreea.cryptoright.model.Coin;

public class CoinsFragment extends Fragment {

    private IClickCallback<Coin> mListener;

    private FragmentCoinListBinding mBinding;
    private CoinsRecyclerViewAdapter mAdapter;

    public CoinsFragment() {
    }

    @SuppressWarnings("unused")
    public static CoinsFragment newInstance() {
        return new CoinsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final CoinsViewModel viewModel =
                ViewModelProviders.of(this).get(CoinsViewModel.class);
        mBinding.setIsLoading(true);
        viewModel.getCoins().observe(this, coins -> {
            if (coins != null) {
                mBinding.setIsLoading(false);
                mAdapter.setCoinList(coins);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_list, container, false);
        View root = mBinding.getRoot();
        mAdapter = new CoinsRecyclerViewAdapter(mListener);
        mBinding.coinList.setAdapter(mAdapter);
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IClickCallback) {
            mListener = (IClickCallback<Coin>) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IClickCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
