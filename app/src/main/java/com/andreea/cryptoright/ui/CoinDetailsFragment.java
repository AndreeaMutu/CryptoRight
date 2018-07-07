package com.andreea.cryptoright.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreea.cryptoright.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {
    private static final String COIN_KEY = "coin-id";

    private String coinId;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_details, container, false);
    }

}
