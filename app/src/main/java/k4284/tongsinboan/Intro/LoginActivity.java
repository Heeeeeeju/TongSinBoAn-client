package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import k4284.tongsinboan.Intro.InitialSetting.InitialSettingActivity;
import k4284.tongsinboan.Intro.Register.RegisterActivity;
import k4284.tongsinboan.MainActivity;
import k4284.tongsinboan.R;

public class LoginActivity extends AppCompatActivity {

    private final int LOGIN_FAIL = 0;
    private final int LOGIN_SUCCESS = 1;
    private final int INITIAL_SETTING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final EditText idField = (EditText) findViewById(R.id.intro_id);
        final EditText passwordField = (EditText) findViewById(R.id.intro_password);

        Button buttonLogin = (Button)findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idField.getText().toString();
                String password = passwordField.getText().toString();
                int result = RequestLoginToServer(id, password);
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

    private int RequestLoginToServer(String id, String password)
    {
        // TODO : 서버와 통신해서 로그인
        return INITIAL_SETTING;
    }

    private void HandleLoginDependOnResult(int result)
    {
        switch (result) {
            case LOGIN_FAIL:
                Toast.makeText(getApplicationContext(), "입력한 정보가 틀렸습니다", Toast.LENGTH_SHORT).show();
                break;
            case LOGIN_SUCCESS:
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            case INITIAL_SETTING:
                Intent settingIntent = new Intent(LoginActivity.this, InitialSettingActivity.class);
                startActivity(settingIntent);
                finish();
                break;
        }
    }
}
