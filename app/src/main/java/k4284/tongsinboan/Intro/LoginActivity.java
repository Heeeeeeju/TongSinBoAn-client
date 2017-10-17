package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import k4284.tongsinboan.MainActivity;
import k4284.tongsinboan.R;

public class LoginActivity extends AppCompatActivity {

    private final int LOGIN_USER = 0;
    private final int LOGIN_GUARD = 1;
    private final int LOGIN_ADMIN = 2;
    private final int INCORRECT_ACCOUNT = 10;
    private final int BLANK_ID = 11;
    private final int BLANK_PASSWORD = 12;
    private final int INITIAL_SETTING = 20;
    private final int WAIT_APPROVAL = 30;

    private final String[] USER_TYPE = {"user", "guard", "admin"};

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
                String id = idField.getText().toString();
                String password = passwordField.getText().toString();
                int result = RequestLogin(id, password);
                HandleLoginDependOnResult(result);
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

    private int RequestLogin(String id, String password)
    {
        if (id.isEmpty()) {
            return BLANK_ID;
        } else if (password.isEmpty()) {
            return BLANK_PASSWORD;
        }

        // TODO : 서버와 통신해서 로그인
        return LOGIN_GUARD;
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
                message = "군번을 입력하세요";
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
                startActivity(waitIntent);
                break;
        }

        if (!message.isEmpty()) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
