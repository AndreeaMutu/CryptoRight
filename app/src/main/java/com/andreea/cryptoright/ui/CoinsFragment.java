package com.andreea.cryptoright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinListBinding;
import com.andreea.cryptoright.model.Coin;

import static com.andreea.cryptoright.helper.Constants.ARG_COLUMN_COUNT;

public class CoinsFragment extends Fragment {

    private int mColumnCount = 1;
    private IClickCallback<Coin> mListener;

    private FragmentCoinListBinding mBinding;
    private CoinsRecyclerViewAdapter mAdapter;

    public CoinsFragment() {
    }

    @SuppressWarnings("unused")
    public static CoinsFragment newInstance(int columnCount) {
        CoinsFragment fragment = new CoinsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
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
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_list, container, false);
        View root = mBinding.getRoot();
        Context context = root.getContext();
        if (mColumnCount <= 1) {
            mBinding.coinList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mBinding.coinList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

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
