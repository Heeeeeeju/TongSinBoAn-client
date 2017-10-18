package k4284.tongsinboan.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class AddMDMPolicyActivity extends AppCompatActivity {

    private EditText policyName;
    private EditText policyDetail;
    private ListView policyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mdm_policy);

        AddMDMPolicyAdapter adapter = new AddMDMPolicyAdapter();
        // TODO : 서버에서 데이터 받아온 후 정책 설정
        adapter.AddItem("정책 설정 1");
        adapter.AddItem("정책 설정 2");
        adapter.AddItem("정책 설정 3");
        adapter.AddItem("정책 설정 4");
        adapter.InitIsSelected();

        policyList = (ListView)findViewById(R.id.add_policy_list);
        policyList.setAdapter(adapter);

        Button submit = (Button)findViewById(R.id.add_policy_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = CheckRequired();
                if (result) {
                    // TODO : 서버 통신해서 정책 추가
                }
            }
        });

        Button cancel = (Button)findViewById(R.id.add_policy_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        policyName = (EditText)findViewById(R.id.add_policy_name);
        policyDetail = (EditText)findViewById(R.id.add_policy_detail);
    }

    private boolean CheckRequired()
    {
        if(policyName.getText().toString().isEmpty()) {
            App.MakeToastMessage("정책 이름을 입력하세요");
            return false;
        } else if (policyDetail.getText().toString().isEmpty()) {
            App.MakeToastMessage("정책 설명을 입력하세요");
            return false;
        }

        boolean[] isSelected = ((AddMDMPolicyAdapter)policyList.getAdapter()).GetIsSelected();
        for (int i=0; i<isSelected.length; i++) {
            if (false == isSelected[i]) {
                App.MakeToastMessage("정책 옵션을 전부 선택해주세요");
                return false;
            }
        }

        return true;
    }
}
