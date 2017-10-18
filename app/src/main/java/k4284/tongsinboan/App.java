package k4284.tongsinboan;

import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
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

    public static void DisableWifi()
    {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    public static void DisableGps()
    {
//        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }
}
