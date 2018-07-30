package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentCoinDetailsBinding;
import com.andreea.cryptoright.helper.Constants;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.model.CoinPrice;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import static com.andreea.cryptoright.helper.Constants.PREF_REF_CCY_SYMBOL;

public class CoinDetailsFragment extends Fragment {

    private static final String TAG = CoinDetailsFragment.class.getSimpleName();
    private String coinId;
    private FragmentCoinDetailsBinding binding;
    private CoinsDetailsRecyclerViewAdapter mAdapter;
    private ShareActionProvider mShareActionProvider;
    private CoinsViewModel coinsViewModel;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        if (key.equals(PREF_REF_CCY_SYMBOL)) {
            Log.d(TAG, "Preferences updated: " + sharedPreferences.getString(key, ""));
            updateUiWithCoinPriceDetails(coinsViewModel, sharedPreferences.getString(key, getString(R.string.ccy_eur)));
        }
    };
    private ActionBar toolbar;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            coinId = getArguments().getString(Constants.ARG_COIN_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        coinsViewModel = ViewModelProviders.of(this).get(CoinsViewModel.class);

        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        toolbar = ((AppCompatActivity) activity).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setHomeButtonEnabled(true);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        String refCcy = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(PREF_REF_CCY_SYMBOL, getString(R.string.ccy_eur));

        updateUiWithCoinPriceDetails(coinsViewModel, refCcy);
        binding.setIsCoinInWatchlist(false);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            coinsViewModel.getUserWatchlist(acct.getId()).observe(this, watchlist -> {
                if (watchlist != null && watchlist.getUserCoinIds().contains(coinId)) {
                    binding.setIsCoinInWatchlist(true);
                }
            });
        }

        binding.addToWatchlistFab.setOnClickListener(v -> {
            if (acct != null) {
                String userId = acct.getId();
                // add current coin to user watchlist
                coinsViewModel.getUserWatchlist(userId).observe(this, watchlist ->
                        coinsViewModel.addCoinToWatchlist(userId, coinId, watchlist));
            } else {
                Snackbar.make(binding.detailsCoordLayout, R.string.message_sign_in,
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUiWithCoinPriceDetails(CoinsViewModel viewModel, String refCcy) {
        viewModel.getCoinPriceDetails(coinId, refCcy).observe(this, coinWithPrice -> {

            if (coinWithPrice != null) {
                Coin coin = coinWithPrice.getCoin();
                CoinPrice coinPrice = coinWithPrice.getCoinPrice();
                toolbar.setTitle(coin.getCoinName());
                initShareAction(coin.getFullName() + " - " + coinPrice.getPrice() + "\n" + coin.getUrl());

                List<Pair<Integer, String>> details = new ArrayList<>();
                details.add(new Pair<>(R.string.label_coin_name, coin.getCoinName()));
                details.add(new Pair<>(R.string.label_coin_symbol, coin.getSymbol()));
                details.add(new Pair<>(R.string.label_coin_algorithm, coin.getAlgorithm()));
                details.add(new Pair<>(R.string.label_coin_proof_type, coin.getProofType()));
                details.add(new Pair<>(R.string.label_coin_supply, coin.getTotalCoinSupply()));

                details.add(new Pair<>(R.string.label_coin_price, coinPrice.getPrice()));
                details.add(new Pair<>(R.string.label_coin_market, coinPrice.getMarket()));
                details.add(new Pair<>(R.string.label_coin_open_day, coinPrice.getOpenday()));
                details.add(new Pair<>(R.string.label_coin_high_day, coinPrice.getHighday()));
                details.add(new Pair<>(R.string.label_coin_low_day, coinPrice.getLowday()));
                details.add(new Pair<>(R.string.label_coin_daily_change, coinPrice.getChangepctday() + " %"));

                mAdapter.setDetails(details);
            }

        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coin_details, container, false);
        mAdapter = new CoinsDetailsRecyclerViewAdapter();
        binding.detailsContainer.setAdapter(mAdapter);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        binding.adView.loadAd(adRequest);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String eur = getString(R.string.ccy_eur);
        String savedCcy = sharedPreferences.getString(PREF_REF_CCY_SYMBOL, eur);
        if (savedCcy.equals(eur)) {
            menu.findItem(R.id.ccy_eur).setChecked(true);
        } else {
            menu.findItem(R.id.ccy_usd).setChecked(true);
        }

        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }

    private void initShareAction(String text) {
        if (mShareActionProvider != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ccy_eur || id == R.id.ccy_usd) {
            item.setChecked(true);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sharedPreferences.edit()
                    .putString(PREF_REF_CCY_SYMBOL, String.valueOf(item.getTitle()))
                    .apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


}
