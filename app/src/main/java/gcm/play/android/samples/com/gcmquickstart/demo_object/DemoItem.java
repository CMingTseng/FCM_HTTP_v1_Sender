package gcm.play.android.samples.com.gcmquickstart.demo_object;

/**
 * Created by Neo on 2018/5/29.
 */

public abstract class DemoItem {
    private final int id;
    private final String name;
    private final int image;
    private final long device_id;

    public DemoItem(int id, String name, int image, long device_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.device_id = device_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public long getDevice_id() {
        return device_id;
    }
}
