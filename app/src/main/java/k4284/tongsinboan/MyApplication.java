package k4284.tongsinboan;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Administrator on 2017-10-16.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/D2Coding")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
