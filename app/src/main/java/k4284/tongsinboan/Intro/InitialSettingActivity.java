package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import k4284.tongsinboan.App;
import k4284.tongsinboan.MainActivity;
import k4284.tongsinboan.R;

public class InitialSettingActivity extends AppCompatActivity {

    private boolean selectType = false;
    private boolean isUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);

        final EditText groupName = (EditText) findViewById(R.id.init_group_name);

        final Button admin = (Button) findViewById(R.id.init_admin);
        final Button user = (Button) findViewById(R.id.init_user);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setBackgroundColor(App.SelectedColor);
                user.setBackgroundColor(App.UnSelectedColor);
                groupName.setHint("관리 부대 이름 입력");
                isUser = false;
                selectType = true;
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setBackgroundColor(App.UnSelectedColor);
                user.setBackgroundColor(App.SelectedColor);
                groupName.setHint("소속 부대 코드 입력");
                isUser = true;
                selectType = true;
            }
        });

        Button submit = (Button) findViewById(R.id.init_submit_setting);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = IsEnterRequired(groupName.getText().toString());
                if (result) {
                    SubmitInitialSetting();
                }
            }
        });
    }

    private boolean IsEnterRequired(String groupName)
    {
        String message = "";
        if (!selectType) {
            message = "본인의 유형을 선택하세요";
        } else if (groupName.isEmpty()) {
            if (isUser) {
                message = "소속 부대 코드를 입력하세요";
            } else {
                message = "관리 부대 이름을 입력하세요";
            }
        }

        if (!message.isEmpty()) {
            App.MakeToastMessage(message);
            return false;
        }

        return true;
    }

    private void SubmitInitialSetting()
    {
        if (!selectType) {
            App.MakeToastMessage("본인의 유형을 선택하세요");
            return;
        }

        // TODO : 관리자/유저에 따라 서버 통신해서 처리
        if (isUser) {

        } else {

        }

        Intent intent = new Intent(InitialSettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
