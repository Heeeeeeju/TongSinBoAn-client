package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import k4284.tongsinboan.App;
import k4284.tongsinboan.MainActivity;
import k4284.tongsinboan.R;

public class LoginActivity extends AppCompatActivity {

    private final int LOGIN_USER = 0;
    private final int LOGIN_GUARD = 1;
    private final int LOGIN_ADMIN = 2;
    private final int BLANK_ID = 10;
    private final int BLANK_PASSWORD = 11;
    private final int INCORRECT_ACCOUNT = 12;
    private final int FILL_REQUIRED = 13;
    private final int LOGIN_FAIL = 13;
    private final int INITIAL_SETTING = 20;
    private final int WAIT_APPROVAL = 30;
    private final int ENTER_GROUP = 31;

    private final String[] USER_TYPE = {"user", "guard", "admin"};

    private String id = "";
    private String password = "";
    private String groupIdx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idField = (EditText) findViewById(R.id.intro_id);
        final EditText passwordField = (EditText) findViewById(R.id.intro_password);

        Button buttonLogin = (Button)findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idField.getText().toString();
                final String password = passwordField.getText().toString();
                int checkResult = CheckRequired(id, password);
                if (FILL_REQUIRED == checkResult) {
                    new Thread() {
                        public void run() {
                            JSONObject response = RequestLogin(id, password);
                            int handleResult = HandleJsonData(response);
                            HandleLoginDependOnResult(handleResult);
                        }
                    }.start();
                } else {
                    HandleLoginDependOnResult(checkResult);
                }
            }
        });

        Button buttonRegister = (Button)findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private int CheckRequired(final String id, final String password)
    {
        if (id.isEmpty()) {
            return BLANK_ID;
        } else if (password.isEmpty()) {
            return BLANK_PASSWORD;
        }
        return FILL_REQUIRED;
    }

    private JSONObject RequestLogin(String id, String password)
    {
        this.id = id;
        this.password = password;

        JSONObject account = new JSONObject();
        try {
            account.put("userid", id);
            account.put("passwd", password);
        } catch (Exception e) {
            Log.e("Login", e.toString());
        }

        String requestName = "/member/login";
        JSONObject response = App.PostRequest(requestName, account);

        return response;
    }

    private int HandleJsonData(JSONObject response)
    {
        try {
            groupIdx = "";
            JSONObject data = response.getJSONObject("data");
            int level = data.getInt("level");
            if (0 == level) {
                groupIdx = data.getString("group_idx");
                if (groupIdx.equals("null")) {
                    return ENTER_GROUP;
                } else {
                    return WAIT_APPROVAL;
                }
            } else if (1 == level) {
                return LOGIN_USER;
            } else if (2 == level) {
                return LOGIN_GUARD;
            } else if (3 == level) {
                return LOGIN_ADMIN;
            }
        } catch (Exception e) {
            Log.e("Login", e.toString());
        }

        return LOGIN_FAIL;
    }

    private void HandleLoginDependOnResult(int result)
    {
        String message = "";
        switch (result) {
            case INITIAL_SETTING:
                Intent settingIntent = new Intent(LoginActivity.this, InitialSettingActivity.class);
                startActivity(settingIntent);
                finish();
                break;
            case INCORRECT_ACCOUNT:
                message = "입력한 정보가 일치하지 않습니다";
                break;
            case BLANK_ID:
                message = "아이디를 입력하세요";
                break;
            case BLANK_PASSWORD:
                message = "비밀번호를 입력하세요";
                break;
            case LOGIN_USER:
            case LOGIN_GUARD:
            case LOGIN_ADMIN:
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("userType", USER_TYPE[result]);
                startActivity(mainIntent);
                finish();
                break;
            case WAIT_APPROVAL:
                Intent waitIntent = new Intent(LoginActivity.this, WaitApprovalActivity.class);
                waitIntent.putExtra("id", id);
                waitIntent.putExtra("password", password);
                waitIntent.putExtra("groupIdx", groupIdx);
                startActivity(waitIntent);
                break;
            case ENTER_GROUP:
                Intent enterGroupIntent = new Intent(LoginActivity.this, SelectUserTypeActivity.class);
                enterGroupIntent.putExtra("id", id);
                enterGroupIntent.putExtra("password", password);
                startActivity(enterGroupIntent);
                break;
        }

        if (!message.isEmpty()) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
