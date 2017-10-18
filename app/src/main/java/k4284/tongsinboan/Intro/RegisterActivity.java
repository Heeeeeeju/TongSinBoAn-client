package k4284.tongsinboan.Intro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class RegisterActivity extends AppCompatActivity {

    private final int RESULT_SELECT_IMAGE = 6;

    EditText userNameView;
    EditText idView;
    EditText passwordView;
    EditText passwordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idView = (EditText) findViewById(R.id.register_id);
        passwordView = (EditText) findViewById(R.id.register_password);
        passwordConfirmView = (EditText) findViewById(R.id.register_password_confirm);
        userNameView = (EditText) findViewById(R.id.register_user_name);

        Button submit = (Button) findViewById(R.id.register_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 서버통신
                boolean result = CheckRequired();
                if (result) {
                    String id = idView.getText().toString();
                    String password = passwordView.getText().toString();
                    String name = userNameView.getText().toString();

                    try {
                        JSONObject accountInfo = new JSONObject();
                        accountInfo.put("userid", id);
                        accountInfo.put("passwd", password);
                        accountInfo.put("name", name);

                        RegisterAccountRequest(accountInfo);
                    } catch (Exception e) {
                        Log.e("Register", e.toString());
                    }
                }
            }
        });
    }

    private void RegisterAccountRequest(final JSONObject accountInfo)
    {
        new Thread() {
            public void run() {
                String requestName = "/member";
                JSONObject response = App.ServerRequest(App.REQUEST_POST, requestName, accountInfo);
                HandleResponse(response);
            }
        }.start();
    }

    private void HandleResponse(JSONObject response)
    {
        try {
            boolean result = response.getBoolean("result");
            if (result) {
                finish();
            } else {
                String errorMessage = response.getString("msg");
                if (errorMessage.equals("member_userid_exists")) {
                    App.MakeToastMessage("이미 존재하는 아이디입니다");
                } else if (errorMessage.equals("member_create_failed")) {
                    App.MakeToastMessage("서버 오류입니다. 나중에 다시 시도해주세요.");
                }
            }
        } catch (Exception e) {
            Log.e("Register", e.toString());
        }
    }

    private boolean CheckRequired()
    {
        String id = idView.getText().toString();
        if (id.isEmpty()) {
            App.MakeToastMessage("아이디를 입력하세요");
            return false;
        }

        String password = passwordView.getText().toString();
        if (password.isEmpty()) {
            App.MakeToastMessage("비밀번호를 입력하세요");
            return false;
        }

        String passwordConfirm = passwordConfirmView.getText().toString();
        if (!password.equals(passwordConfirm)) {
            App.MakeToastMessage("비밀번호가 일치하지 않습니다");
            return false;
        }

        String userName = userNameView.getText().toString();
        if (userName.isEmpty()) {
            App.MakeToastMessage("계급과 이름을 입력하세요");
            return false;
        }

        return true;
    }
}
