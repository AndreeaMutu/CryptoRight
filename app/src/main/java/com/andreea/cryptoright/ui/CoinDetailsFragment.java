package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {
    private static final String COIN_KEY = "coin-id";

    private String coinId;
    private FragmentCoinDetailsBinding binding;

    public CoinDetailsFragment() {
        // Required empty public constructor
    }

    public static CoinDetailsFragment newInstance(String id) {
        CoinDetailsFragment fragment = new CoinDetailsFragment();
        Bundle args = new Bundle();
        args.putString(COIN_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coinId = getArguments().getString(COIN_KEY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CoinsViewModel viewModel =
                ViewModelProviders.of(this).get(CoinsViewModel.class);

        viewModel.getCoinById(coinId).observe(this, coin -> {
            binding.setCoin(coin);
            binding.setClickHandler(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(coin.getUrl()))));
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_details, container, false);
        return binding.getRoot();
    }
}
