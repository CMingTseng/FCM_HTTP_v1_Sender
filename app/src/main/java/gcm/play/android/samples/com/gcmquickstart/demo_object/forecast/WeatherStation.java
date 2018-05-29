package gcm.play.android.samples.com.gcmquickstart.demo_object.forecast;

import java.util.Arrays;
import java.util.List;

import gcm.play.android.samples.com.gcmquickstart.R;

/**
 * Created by Neo on 2018/5/29.
 */

public class WeatherStation {
    public static WeatherStation get() {
        return new WeatherStation();
    }

    private WeatherStation() {
    }

    public List<Forecast> getForecasts() {
        return Arrays.asList(
                new Forecast(0,"Pisa", R.drawable.pisa,726, "16", Weather.PARTLY_CLOUDY),
                new Forecast(1,"Paris", R.drawable.paris, 243,"14", Weather.CLEAR),
                new Forecast(2,"New York", R.drawable.new_york, 22,"9", Weather.MOSTLY_CLOUDY),
                new Forecast(3,"Rome", R.drawable.rome, 123,"18", Weather.PARTLY_CLOUDY),
                new Forecast(4,"London", R.drawable.london,78, "6", Weather.PERIODIC_CLOUDS),
                new Forecast(5,"Washington", R.drawable.washington,7693, "20", Weather.CLEAR));
    }
}
