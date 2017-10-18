package k4284.tongsinboan;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Administrator on 2017-10-16.
 */

public class App extends Application {

    private static Context context;
    public static int SelectedColor;
    public static int UnSelectedColor;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/D2Coding")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        SelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.selected);
        UnSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.unselected);
    }

    public static void MakeToastMessage(String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
