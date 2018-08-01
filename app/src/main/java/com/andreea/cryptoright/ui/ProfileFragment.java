package com.andreea.cryptoright.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.databinding.FragmentProfileBinding;
import com.andreea.cryptoright.model.Coin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class ProfileFragment extends Fragment {

    private static final int RC_SIGN_IN = 215;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;
    private FragmentProfileBinding binding;
    private CoinsRecyclerViewAdapter mAdapter;
    private CoinsViewModel viewModel;
    private IClickCallback<Coin> mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.setIsSignedIn(false);
        binding.signInButton.setOnClickListener(this::onClick);
        binding.signOutButton.setOnClickListener(this::onClick);
        mAdapter = new CoinsRecyclerViewAdapter(mListener);
        binding.watchlistCoins.setAdapter(mAdapter);
        return binding.getRoot();
    }

    private void signOut() {
        binding.setIsLoading(true);
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            binding.setIsLoading(false);
            binding.setIsSignedIn(false);
            binding.profileNameTv.setText("");
            mAdapter.setCoinList(Collections.emptyList());
            Picasso.get().load(R.drawable.ic_account_black_24dp).placeholder(R.drawable.ic_account_black_24dp)
                    .into(binding.profilePicture);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CoinsViewModel.class);

        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            updateUiForAccount(acct);
        }
    }

    private void signIn() {
        binding.setIsLoading(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUiForAccount(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            binding.setIsLoading(false);
        }
    }

    private void updateUiForAccount(GoogleSignInAccount account) {
        Log.d(TAG, "handleSignInResult: success" + account.getDisplayName() + " " + account.getPhotoUrl());
        binding.setIsLoading(false);
        binding.setIsSignedIn(true);
        if (account.getPhotoUrl() != null) {
            Picasso.get().load(account.getPhotoUrl()).placeholder(android.R.drawable.stat_notify_error)
                    .into(binding.profilePicture);
        }
        binding.profileNameTv.setText(account.getDisplayName());
        String userId = account.getId();
        loadUserWatchlist(userId);
    }

    private void loadUserWatchlist(String userId) {
        viewModel.getWatchlistCoins(userId).observe(this, coins -> {
            if (coins != null) {
                mAdapter.setCoinList(coins);
            }
        });
    }

    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
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
