package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinDetailsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {
    private static final String COIN_KEY = "coin-id";

    private String coinId;
    private FragmentCoinDetailsBinding binding;
    private CoinsDetailsRecyclerViewAdapter mAdapter;

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
            List<Pair<String, String>> details = new ArrayList<>();
            details.add(new Pair<>("Name", coin.getCoinName()));
            details.add(new Pair<>("Symbol", coin.getSymbol()));
            details.add(new Pair<>("Algorithm", coin.getAlgorithm()));
            details.add(new Pair<>("Proof Type", coin.getProofType()));
            details.add(new Pair<>("Total Coin Supply", coin.getTotalCoinSupply()));
            details.add(new Pair<>("Price", "6000$"));
            details.add(new Pair<>("Market", "CryptoCompare Index"));
            details.add(new Pair<>("Open Day", "6233 $"));
            details.add(new Pair<>("High Day", "6233 $"));
            details.add(new Pair<>("Low Day", "6233 $"));
            details.add(new Pair<>("Daily change Percent", "2.06%"));

            mAdapter.setDetails(details);
//            binding.setClickHandler(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(coin.getUrl()))));
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(coin.getCoinName());
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_details, container, false);
        mAdapter = new CoinsDetailsRecyclerViewAdapter();
        binding.detailsContainer.setAdapter(mAdapter);
        return binding.getRoot();
    }
}
