package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import static com.andreea.cryptoright.helper.Constants.PREF_REF_CCY_SYMBOL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {

    private static final String TAG = CoinDetailsFragment.class.getSimpleName();
    private String coinId;
    private FragmentCoinDetailsBinding binding;
    private CoinsDetailsRecyclerViewAdapter mAdapter;
    private List<Pair<Integer, String>> coinDetails = new ArrayList<>();
    private ShareActionProvider mShareActionProvider;
    private CoinsViewModel coinsViewModel;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        if (key.equals(PREF_REF_CCY_SYMBOL)) {
            Log.d(TAG, "Preferences updated: "+ sharedPreferences.getString(key, getString(R.string.ccy_eur)));
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

        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setHomeButtonEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);

        coinsViewModel.getCoinDetails(coinId).observe(this, details -> {
            if (details != null) {
                coinDetails.addAll(details);
                mAdapter.setDetails(coinDetails);
            }
        });

        String refCcy = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(PREF_REF_CCY_SYMBOL, getString(R.string.ccy_eur));

        updateUiWithCoinPriceDetails(coinsViewModel, refCcy);
        binding.setIsCoinInWatchlist(false);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
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
        Log.d(TAG, "updateUiWithCoinPriceDetails: ref ccy"+refCcy);
        viewModel.getCoinById(coinId).observe(this, coin -> {
            binding.setClickHandler(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(coin.getUrl()))));
            toolbar.setTitle(coin.getCoinName());
            viewModel.getCoinPriceDetails(coin.getSymbol(), refCcy).observe(this, pairs -> {
                if (pairs != null) {
                    coinDetails.addAll(pairs);
                    mAdapter.setDetails(coinDetails);
                    initShareAction(coin.getFullName() + " - " + pairs.get(0).second + " " + refCcy);
                }
            });
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
