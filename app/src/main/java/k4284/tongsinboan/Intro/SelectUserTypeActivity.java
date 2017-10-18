package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import k4284.tongsinboan.App;
import k4284.tongsinboan.MainActivity;
import k4284.tongsinboan.R;

public class SelectUserTypeActivity extends AppCompatActivity {

    private final int ADMIN = 0;
    private final int GUARD = 1;
    private final int USER = 2;

    private int userType = -1;
    private String[] groupHint = {
            "관리 부대 이름을 입력하세요",
            "소속 부대 코드를 입력하세요",
            "소속 부대 코드를 입력하세요"
    };

    HashMap<String, String> errorMessages;

    MultiStateToggleButton userTypeView;
    EditText groupInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        String id = getIntent().getStringExtra("id");
        String password = getIntent().getStringExtra("password");
        if (null != id && null != password) {
            // TODO : 서버 통신해서 현재 상태 받아오기
        }

        errorMessages = new HashMap<>();
        errorMessages.put("group_exists", "이미 존재하는 부대 이름입니다");
        errorMessages.put("group_create_failed", "서버 오류 - 부대 생성에 실패했습니다");
        errorMessages.put("group_join_failed", "서버 오류 또는 잘못된 부대 코드입니다");

        userTypeView = (MultiStateToggleButton) findViewById(R.id.user_type_option);
        userTypeView.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                userType = value;
                groupInfoView.setHint(groupHint[value]);
            }
        });

        groupInfoView = (EditText) findViewById(R.id.user_type_group_name);

        Button submit = (Button) findViewById(R.id.user_type_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        String requestName = "";
                        if (ADMIN == userType) {
                            requestName = "/gruop";
                        } else if (GUARD == userType || USER == userType) {
                            requestName = "/group/join";
                        }
                        JSONObject param = HandleGroupRequest();

                        JSONObject response = App.PostRequest(requestName, param);
                        HandleGroupResponse(response);
                    }
                }.start();
            }
        });
    }

    private JSONObject HandleGroupRequest()
    {
        JSONObject param = new JSONObject();
        try {
            String groupInfo = groupInfoView.getText().toString();
            if (ADMIN == userType) {
                param.put("name", groupInfo);
            } else {
                param.put("group_idx", groupInfo);
            }
        } catch (Exception e) {
            Log.e("UserType", e.toString());
        }

        return param;
    }

    private void HandleGroupResponse(JSONObject response)
    {
        try {
            boolean result = response.getBoolean("result");
            if (result) {
                Intent intent;
                if (ADMIN == userType) {
                    intent = new Intent(SelectUserTypeActivity.this, MainActivity.class);
                    intent.putExtra("userType", "admin");
                } else {
                    intent = new Intent(SelectUserTypeActivity.this, WaitApprovalActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                final String errorMessage = response.getString("msg");
                if (errorMessages.containsKey(errorMessage)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.MakeToastMessage(errorMessages.get(errorMessage));
                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e("UserType", e.toString());
        }
    }
}
