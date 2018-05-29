package gcm.play.android.samples.com.gcmquickstart.demo_object.shop;

import gcm.play.android.samples.com.gcmquickstart.demo_object.DemoItem;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class ShopItem extends DemoItem {
    private final String price;

    public ShopItem(int id, String name, int image, long device_id, String price) {
        super(id, name, image, device_id);
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
