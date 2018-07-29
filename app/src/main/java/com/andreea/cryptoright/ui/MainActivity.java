package com.andreea.cryptoright.ui;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.ActivityMainBinding;
import com.andreea.cryptoright.helper.Constants;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.web.DownloadService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

public class MainActivity extends AppCompatActivity implements IClickCallback<Coin> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolbar;
    private String activeFragmentTag;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        String fragmentTag = null;
        switch (item.getItemId()) {
            case R.id.navigation_coins:
                fragmentTag = getString(R.string.title_coins);
                break;
            case R.id.navigation_news:
                fragmentTag = getString(R.string.title_news);
                break;
            case R.id.navigation_profile:
                fragmentTag = getString(R.string.title_profile);
                break;
        }
        switchFragment(activeFragmentTag, fragmentTag);
        return true;
    };

    /*
    Adapted from:
    https://medium.com/@oluwabukunmi.aluko/bottom-navigation-view-with-fragments-a074bfd08711
     */
    private void switchFragment(String currentFragmentTag, String newFragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        int stackHeight = fm.getBackStackEntryCount();
        if (stackHeight > 0) {
            fm.popBackStackImmediate();
        }
        Log.d(TAG, "switchFragment: Replacing fragment " + currentFragmentTag + " with " + newFragmentTag);

        Fragment currentFragment = fm.findFragmentByTag(currentFragmentTag);
        Fragment newFragment = fm.findFragmentByTag(newFragmentTag);
        fm.beginTransaction().hide(currentFragment).show(newFragment).commit();
        activeFragmentTag = newFragmentTag;
        toolbar.setTitle(activeFragmentTag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.ACTIVE_FRAGMENT_KEY, activeFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        toolbar = getSupportActionBar();
        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(() -> {
            int stackHeight = fm.getBackStackEntryCount();
            if (stackHeight > 0) {
                toolbar.setHomeButtonEnabled(true);
                toolbar.setDisplayHomeAsUpEnabled(true);
            } else {
                toolbar.setDisplayHomeAsUpEnabled(false);
                toolbar.setHomeButtonEnabled(false);
            }
        });
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            Fragment coinsFragment = CoinsFragment.newInstance(1);
            Fragment newsFragment = NewsFragment.newInstance(2);
            Fragment profileFragment = ProfileFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, profileFragment, getString(R.string.title_profile)).hide(profileFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, newsFragment, getString(R.string.title_news)).hide(newsFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, coinsFragment, getString(R.string.title_coins)).commit();
            activeFragmentTag = getString(R.string.title_coins);

            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Job myJob = dispatcher.newJobBuilder()
                    .setService(DownloadService.class) // the JobService that will be called
                    .setTag(Constants.COINS_JOB_TAG)        // uniquely identifies the job
                    .build();

            dispatcher.mustSchedule(myJob);
        } else {
            activeFragmentTag = savedInstanceState.getString(Constants.ACTIVE_FRAGMENT_KEY, getString(R.string.title_coins));
        }
        Log.d(TAG, "onCreate: Active fragment  " + activeFragmentTag);
        toolbar.setTitle(activeFragmentTag);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String eur = getString(R.string.ccy_eur);
        String savedCcy = sharedPreferences.getString(Constants.PREF_REF_CCY_SYMBOL, eur);
        if (savedCcy.equals(eur)) {
            menu.findItem(R.id.ccy_eur).setChecked(true);
        } else {
            menu.findItem(R.id.ccy_usd).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        if (id == R.id.ccy_eur || id == R.id.ccy_usd) {
            item.setChecked(true);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit()
                    .putString(Constants.PREF_REF_CCY_SYMBOL, String.valueOf(item.getTitle()))
                    .apply();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int stackHeight = fragmentManager.getBackStackEntryCount();
        if (stackHeight > 0) {
            fragmentManager.popBackStack();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(Coin coin) {
        CoinDetailsFragment detailsFragment = CoinDetailsFragment.newInstance(coin.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
        //activeFragmentTag = getString(R.string.title_coins);
    }
}
