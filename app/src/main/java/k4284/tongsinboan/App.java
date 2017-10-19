package k4284.tongsinboan;

import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Administrator on 2017-10-16.
 */

public class App extends Application {

    private static Context context;
    public static int SelectedColor;
    public static int UnSelectedColor;

    public static final String ERROR_NOT_LOGIN = "authentication_required";
    public static final String ERROR_NOT_LOGIN_MESSAGE = "로그인 되어있지 않습니다";

    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_PUT = "PUT";
    public static final String REQUEST_GET = "GET";

    public static final String ServerDomain = "http://10.53.128.145";

    private static String cookie = "";

    public static UserData User;
    public class UserData {
        public String name;
        public String groupIdx;
        public String groupName;
        public int level;
        public String profileImageUri;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        User = new UserData();

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

    public static JSONObject ServerRequest(String requestMethod, String requestName)
    {
        return ServerRequest(requestMethod, requestName, null);
    }

    public static JSONObject ServerRequest(String requestMethod, String requestName, JSONObject params)
    {
        HttpURLConnection urlConnection = null;
        JSONObject response = null;
        try {
            URL url = new URL(App.ServerDomain + requestName);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Cookie", cookie);

            if (requestMethod.equals(REQUEST_POST) || requestMethod.equals(REQUEST_PUT)) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(params.toString().getBytes());
                outputStream.close();
            }

            urlConnection.connect();
            String cookieTemp = urlConnection.getHeaderField("Set-Cookie");
            if (cookieTemp != null) {
                cookie = cookieTemp;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if(stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(line);
            }

            response = new JSONObject(stringBuilder.toString());
        } catch (Exception e) {
            Log.e("Server", e.toString());
            response = new JSONObject();
        } finally {
            urlConnection.disconnect();
        }

        return response;
    }
}
