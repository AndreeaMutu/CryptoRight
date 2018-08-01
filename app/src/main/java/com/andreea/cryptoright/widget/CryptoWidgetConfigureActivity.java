package com.andreea.cryptoright.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.model.Coin;
import com.andreea.cryptoright.ui.CoinsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link CryptoWidget CryptoWidget} AppWidget.
 */
public class CryptoWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.andreea.cryptoright.widget.CryptoWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String SYMBOL_SUFFIX = "sym";
    public static final String CCY_SUFFIX = "ccy";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner coinSpinner;
    private Spinner ccySpinner;
    //EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = CryptoWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String coinSymbol = String.valueOf(coinSpinner.getSelectedItem());
            if (TextUtils.isEmpty(coinSymbol)) {
                Toast.makeText(CryptoWidgetConfigureActivity.this, "Please pick a coin", Toast.LENGTH_LONG).show();
                return;
            }
            saveSymbolPref(context, mAppWidgetId, coinSymbol);

            String ccy = String.valueOf(ccySpinner.getSelectedItem());
            if (TextUtils.isEmpty(ccy)) {
                Toast.makeText(CryptoWidgetConfigureActivity.this, "Please pick a currency", Toast.LENGTH_LONG).show();
                return;
            }
            saveCcyPref(context, mAppWidgetId, ccy);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            CryptoWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public CryptoWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveSymbolPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + SYMBOL_SUFFIX, text);
        prefs.apply();
    }

    static void saveCcyPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + CCY_SUFFIX, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadSymbolPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String symbol = prefs.getString(PREF_PREFIX_KEY + appWidgetId + SYMBOL_SUFFIX, null);
        if (symbol != null) {
            return symbol;
        }
        return context.getString(R.string.coin_widget_default_coin_symbol);
    }

    static String loadCcyPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String ccy = prefs.getString(PREF_PREFIX_KEY + appWidgetId + CCY_SUFFIX, null);
        if (ccy != null) {
            return ccy;
        }
        return context.getString(R.string.ccy_eur);
    }

    static void deleteSymbolPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + SYMBOL_SUFFIX);
        prefs.apply();
    }

    static void deleteCcyPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + CCY_SUFFIX);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.crypto_widget_configure);
        coinSpinner = findViewById(R.id.spinner_coins);


        CoinsViewModel model = ViewModelProviders.of(this).get(CoinsViewModel.class);
        model.getCoins().observe(this, coins -> {
            String[] items;
            if (coins != null) {
                List<String> list = new ArrayList<>();
                for (Coin coin : coins) {
                    String symbol = coin.getSymbol();
                    list.add(symbol);
                }
                items = list.toArray(new String[0]);
            } else {
                items = new String[]{getString(R.string.coin_widget_default_coin_symbol)};
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, items);
            coinSpinner.setAdapter(adapter);
        });

        ccySpinner = findViewById(R.id.spinner_ccy);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }
}

