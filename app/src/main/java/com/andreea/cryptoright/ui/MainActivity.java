package com.andreea.cryptoright.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.web.DownloadService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CoinClickCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolbar;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_coins:
                        toolbar.setTitle(R.string.title_coins);
                        fragment = CoinsFragment.newInstance(1);
                        return loadFragment(fragment, false);
                    case R.id.navigation_news:

                        return true;
                    case R.id.navigation_profile:

                        return true;
                }
                return false;
            };
    private boolean loadFragment(Fragment fragment, boolean save) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment);
            if (save) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = getSupportActionBar();


        if (savedInstanceState == null) {
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_coins);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(() -> {
                int stackHeight = fragmentManager.getBackStackEntryCount();
                if (stackHeight > 0) {
                    toolbar.setHomeButtonEnabled(true);
                    toolbar.setDisplayHomeAsUpEnabled(true);
                } else {
                    toolbar.setDisplayHomeAsUpEnabled(false);
                    toolbar.setHomeButtonEnabled(false);
                }
            });

            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Job myJob = dispatcher.newJobBuilder()
                    .setService(DownloadService.class) // the JobService that will be called
                    .setTag("coin-tag")        // uniquely identifies the job
                    .build();

            //dispatcher.mustSchedule(myJob);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        loadFragment(detailsFragment, true);
    }
}
