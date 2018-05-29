package gcm.play.android.samples.com.gcmquickstart.demo_object.shop;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

import gcm.play.android.samples.com.gcmquickstart.FCMSampleApplication;
import gcm.play.android.samples.com.gcmquickstart.R;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class ShopStation {

    private static final String STORAGE = "shop";

    public static ShopStation get() {
        return new ShopStation();
    }

    private SharedPreferences storage;

    private ShopStation() {
        storage = FCMSampleApplication.getAppContext().getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }

    public List<ShopItem> getData() {
        return Arrays.asList(
                new ShopItem(1, "Everyday Candle", R.drawable.shop1, 888, "$12.00 USD"),
                new ShopItem(2, "Small Porcelain Bowl", R.drawable.shop2, 559, "$50.00 USD"),
                new ShopItem(3, "Favourite Board", R.drawable.shop3, 423, "$265.00 USD"),
                new ShopItem(4, "Earthenware Bowl", R.drawable.shop4, 1100, "$18.00 USD"),
                new ShopItem(5, "Porcelain Dessert Plate", R.drawable.shop5, 250, "$36.00 USD"),
                new ShopItem(6, "Detailed Rolling Pin", R.drawable.shop6, 123, "$145.00 USD"));
    }

    public boolean isRated(int itemId) {
        return storage.getBoolean(String.valueOf(itemId), false);
    }

    public void setRated(int itemId, boolean isRated) {
        storage.edit().putBoolean(String.valueOf(itemId), isRated).apply();
    }
}
