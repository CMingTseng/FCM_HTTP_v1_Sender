package gcm.play.android.samples.com.gcmquickstart.demo_object.forecast;

import gcm.play.android.samples.com.gcmquickstart.demo_object.DemoItem;

/**
 * Created by Neo on 2018/5/28.
 */

public class Forecast extends DemoItem {
    private final String temperature;
    private final Weather weather;
    public Forecast(int id,String name, int image, long device_id, String temperature, Weather weather) {
        super(id,name, image, device_id);
        this.temperature = temperature;
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public Weather getWeather() {
        return weather;
    }
}
