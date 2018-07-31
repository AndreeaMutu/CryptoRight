package com.andreea.cryptoright.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.andreea.cryptoright.R;
import com.andreea.cryptoright.helper.Constants;
import com.andreea.cryptoright.model.CoinPrice;
import com.andreea.cryptoright.model.CoinPriceResponse;
import com.andreea.cryptoright.web.CryptoCompareService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CryptoWidgetConfigureActivity CryptoWidgetConfigureActivity}
 */
public class CryptoWidget extends AppWidgetProvider {

    private static final String TAG = CryptoWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String coinSymbol = CryptoWidgetConfigureActivity.loadSymbolPref(context, appWidgetId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.COIN_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d(TAG, "updateAppWidget: ");
        CryptoCompareService service = retrofit.create(CryptoCompareService.class);
        Call<CoinPriceResponse> coinPriceResponseCall = service.getCoinPrices(coinSymbol, "USD");
        coinPriceResponseCall.enqueue(new Callback<CoinPriceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinPriceResponse> call, @NonNull Response<CoinPriceResponse> response) {
                if (response.isSuccessful()) {
                    CoinPriceResponse body = response.body();
                    if (body != null) {
                        Map<String, Map<String, CoinPrice>> priceData = body.getPriceData();
                        Log.d(TAG, "onResponse price data: " + priceData);
                        CoinPrice coinPrice = priceData.get(coinSymbol).get("USD");

                        // Construct the RemoteViews object
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.crypto_widget);
                        views.setTextViewText(R.id.coin_symbol_tv, coinSymbol);
                        views.setTextViewText(R.id.price_tv, coinPrice.getPrice());
                        views.setTextViewText(R.id.change_percent_tv, coinPrice.getChangepctday() + " %");

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoinPriceResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Call to price data failed", t);
            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CryptoWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

