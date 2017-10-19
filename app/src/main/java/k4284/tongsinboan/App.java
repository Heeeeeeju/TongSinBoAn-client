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
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Administrator on 2017-10-16.
 */

public class App extends Application {

    private static Context context;

    public static final int DEVICE_ADMIN = 777;
    public static final int PICK_IMAGE = 11111;
    public static final int CHANGE_POLICY = 1048;
    public static final int ADD_ADMIN = 8080;
    public static final int ADD_USER = 808;

    public static final String[] MdmNames = {
            "카메라",
            "마이크",
            "GPS",
            "Wifi",
            "핫스팟",
            "블루투스"
    };

    public static final String[] MdmKeys = {
            "mdm_camera",
            "mdm_mic",
            "mdm_gps",
            "mdm_wifi",
            "mdm_hotspot",
            "mdm_bluetooth"
    };

    public static final String NoGroup = "(등록된 부대가 없습니다)";
    public static final String NoBelong = "(등록된 소속이 없습니다)";

    public static int SelectedColor;
    public static int UnSelectedColor;

    public static final String ERROR_NOT_LOGIN = "authentication_required";
    public static final String ERROR_NOT_LOGIN_MESSAGE = "로그인 되어있지 않습니다";

    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_PUT = "PUT";
    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_DELETE = "DELETE";

    public static final String ServerDomain = "https://swcamp.cpsoft.co.kr";

    private static String cookie = "";

    public static UserData User;
    public class UserData {
        public String name;
        public String groupIdx;
        public String groupName;
        public String belongName;
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

    public static void DisableWifi(boolean enable)
    {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static JSONObject ServerRequest(String requestMethod, String requestName)
    {
        return ServerRequest(requestMethod, requestName, null);
    }

    public static JSONObject ServerRequest(String requestMethod, String requestName, Object params)
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
                if (params instanceof byte[]) {
                    outputStream.write((byte[])params);
                } else {
                    outputStream.write(params.toString().getBytes());
                }
                outputStream.close();
            }

            urlConnection.connect();
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");
            if (cookiesHeader != null) {
                String tmp = "";
                for (String cookie_o : cookiesHeader) {
                    tmp = tmp + cookie_o.split(";")[0] + ';';
                }
                cookie = tmp;
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

    public static void SaveUserData(JSONObject data)
    {
        try {
            App.User.name = data.getString("name");
            String groupIdx = data.getString("group_idx");
            if (!groupIdx.equals("null")) {
                App.User.groupIdx = groupIdx;
            }
            App.User.groupName = data.getString("group_name");
            String belongName = data.getString("belong");
            if (belongName.equals("null")) {
                App.User.belongName = App.NoBelong;
            } else {
                App.User.belongName = belongName;
            }
            App.User.level = data.getInt("level");
            String profileImageUri = data.getString("profile_img");
            if (!profileImageUri.equals("null")) {
                App.User.profileImageUri
                        = App.ServerDomain + "/upload/" + data.getString("profile_img");
            }
        } catch (Exception e) {
            Log.e("SaveUserData", e.toString());
        }
    }

//    public static void ShowErrorMessage(String errorMessage)
//    {
//        context
//        if (errorMessage.equals("authentication_required")) {
//            MakeToastMessage("로그인 되어있지 않습니다");
//        } else if (errorMessage.equals("policy_read_admin_failed")) {
//            MakeToastMessage("서버 오류로 인해 조회에 실패했습니다");
//        } else if (errorMessage.equals("policy_create_admin_failed")) {
//            MakeToastMessage("서버 오류로 인해 추가에 실패했습니다");
//        } else if (errorMessage.equals("no_permission")) {
//            MakeToastMessage("정책에 대한 권한이 없습니다");
//        } else if (errorMessage.equals("policy_verify_failed")) {
//            MakeToastMessage("서버 오류로 인해 검증에 실패했습니다");
//        } else if (errorMessage.equals("policy_member_read_failed")) {
//            MakeToastMessage("서버 오류로 인해 조회에 실패했습니다");
//        } else if (errorMessage.equals("upload_server_error")) {
//            MakeToastMessage("서버 오류로 인해 업로드가 실패했습니다");
//        } else if (errorMessage.equals("upload_img_exists")) {
//            MakeToastMessage("이미 프로필 이미지가 존재합니다");
//        } else if (errorMessage.equals("upload_failed")) {
//            MakeToastMessage("서버 오류로 인해 업로드가 실패했습니다");
//        } else if (errorMessage.equals("upload_wrong_image")) {
//            MakeToastMessage("잘못된 이미지를 업로드했습니다");
//        } else if (errorMessage.equals("upload_process_failed")) {
//            MakeToastMessage("서버 오류로 인해 업로드가 실패했습니다");
//        } else if (errorMessage.equals("group_exists")) {
//            MakeToastMessage("이미 부대를 생성했거나 가입되어 있습니다");
//        } else if (errorMessage.equals("group_create_failed")) {
//            MakeToastMessage("서버 오류로 인해 부대 생성에 실패했습니다");
//        } else if (errorMessage.equals("group_join_failed")) {
//            MakeToastMessage("서버 오류로 인해 부대 가입에 실패했습니다");
//        } else if (errorMessage.equals("group_join_failed")) {
//            MakeToastMessage("잘못된 부대 번호입니다");
//        } else if (errorMessage.equals("member_read_failed")) {
//            MakeToastMessage("서버 오류로 인해 조회에 실패했습니다");
//        } else if (errorMessage.equals("member_userid_exists")) {
//            MakeToastMessage("이미 존재하는 아이디입니다");
//        } else if (errorMessage.equals("member_create_failed")) {
//            MakeToastMessage("서버 오류로 인해 회원가입에 실패했습니다");
//        }
//    }
}
