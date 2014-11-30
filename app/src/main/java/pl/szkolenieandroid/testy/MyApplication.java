package pl.szkolenieandroid.testy;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParsePush;
import com.parse.PushService;

/**
 * Created by Bartek on 2014-11-30.
 */
public class MyApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "UOrCG55Fwt8lYlLXEXlQkyPVFojm9cPiUvfYF283", "yjGgVruj7ohhzY2itFqxdpFKn6VxUy0eeWVXpVvJ");
        ParsePush.subscribeInBackground("Giants");
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
