package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinDetailsBinding;
import com.andreea.cryptoright.helper.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {

    private String coinId;
    private FragmentCoinDetailsBinding binding;
    private CoinsDetailsRecyclerViewAdapter mAdapter;
    private List<Pair<Integer, String>> coinDetails = new ArrayList<>();

    public CoinDetailsFragment() {
        // Required empty public constructor
    }

    public static CoinDetailsFragment newInstance(String id) {
        CoinDetailsFragment fragment = new CoinDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_COIN_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coinId = getArguments().getString(Constants.ARG_COIN_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CoinsViewModel viewModel =
                ViewModelProviders.of(this).get(CoinsViewModel.class);

        viewModel.getCoinDetails(coinId).observe(this, details -> {
            if (details != null) {
                coinDetails.addAll(details);
                mAdapter.setDetails(coinDetails);
            }
        });

        String refCcy = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(Constants.PREF_REF_CCY_SYMBOL, getString(R.string.ccy_eur));

        viewModel.getCoinById(coinId).observe(this, coin -> {
            binding.setClickHandler(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(coin.getUrl()))));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(coin.getCoinName());
            viewModel.getCoinPriceDetails(coin.getSymbol(), refCcy).observe(this, pairs -> {
                if (pairs != null) {
                    coinDetails.addAll(pairs);
                    mAdapter.setDetails(coinDetails);
                }
            });
        });
        binding.setIsCoinInWatchlist(false);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            viewModel.getUserWatchlist(acct.getId()).observe(this, watchlist -> {
                if (watchlist != null && watchlist.getUserCoinIds().contains(coinId)) {
                    binding.setIsCoinInWatchlist(true);
                }
            });
        }

        binding.addToWatchlistFab.setOnClickListener(v -> {
            if (acct != null) {
                String userId = acct.getId();
                // add current coin to user watchlist
                viewModel.getUserWatchlist(userId).observe(this, watchlist ->
                        viewModel.addCoinToWatchlist(userId, coinId, watchlist));
            } else {
                Snackbar.make(binding.detailsCoordLayout, R.string.message_sign_in,
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_details, container, false);
        mAdapter = new CoinsDetailsRecyclerViewAdapter();
        binding.detailsContainer.setAdapter(mAdapter);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        binding.adView.loadAd(adRequest);
        return binding.getRoot();
    }
}
