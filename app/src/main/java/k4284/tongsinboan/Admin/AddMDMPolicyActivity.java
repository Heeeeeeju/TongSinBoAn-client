package k4284.tongsinboan.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class AddMDMPolicyActivity extends AppCompatActivity {

    private EditText policyNameView;
    private EditText policyDetailView;
    private ListView policyList;

    private boolean isEdit = false;
    private int idx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mdm_policy);

        policyNameView = (EditText)findViewById(R.id.add_policy_name);
        policyDetailView = (EditText)findViewById(R.id.add_policy_detail);

        AddMDMPolicyAdapter adapter = new AddMDMPolicyAdapter();

        String dataString = getIntent().getStringExtra("data");
        if (null != dataString) {
            try {
                JSONObject data = new JSONObject(dataString);

                String policyName = data.getString("name");
                policyNameView.setText(policyName);

                String policyDetail = data.getString("comment");
                policyDetailView.setText(policyDetail);

                int idx = data.getInt("idx");
                this.idx = idx;

                for (int i=0; i<App.MdmKeys.length; i++) {
                    int value = data.getInt(App.MdmKeys[i]);
                    adapter.AddItem(App.MdmNames[i], value);
                }

                isEdit = true;
            } catch (Exception e) {
                Log.e("AddMDMPolicy", e.toString());
            }
        } else {
            for (int i=0; i<App.MdmKeys.length; i++) {
                adapter.AddItem(App.MdmNames[i]);
            }
        }
        adapter.InitIsSelected();

        policyList = (ListView)findViewById(R.id.add_policy_list);
        policyList.setAdapter(adapter);

        Button submit = (Button)findViewById(R.id.add_policy_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = CheckRequired();
                if (result) {
                    new Thread() {
                        public void run() {
                            GeneratePolicy();
                        }
                    }.start();
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
    }

    private boolean CheckRequired()
    {
        if(policyNameView.getText().toString().isEmpty()) {
            App.MakeToastMessage("정책 이름을 입력하세요");
            return false;
        } else if (policyDetailView.getText().toString().isEmpty()) {
            App.MakeToastMessage("정책 설명을 입력하세요");
            return false;
        }

        if (isEdit) {
            return true;
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

    private void GeneratePolicy()
    {
        try {
            String requestMethod = App.REQUEST_POST;
            String requestName = "/policy";
            if (isEdit) {
                requestMethod = App.REQUEST_PUT;
                requestName = "/policy/" + idx;
            }

            JSONObject param = new JSONObject();
            param.put("name", policyNameView.getText().toString());
            param.put("comment", policyDetailView.getText().toString());

            AddMDMPolicyAdapter adapter = (AddMDMPolicyAdapter) policyList.getAdapter();
            for (int i=0; i<App.MdmKeys.length; i++) {
                AddMDMPolicyItem item = (AddMDMPolicyItem) adapter.getItem(i);
                param.put(App.MdmKeys[i], item.GetSelectedValue());
            }

            JSONObject response = App.ServerRequest(requestMethod, requestName, param);
            boolean result = response.getBoolean("result");
            if (result) {
                Intent intent = new Intent();
                intent.putExtra("policies", param.toString());
                setResult(App.ADD_POLICY, intent);
                finish();
            } else {
                String errorMessage = response.getString("msg");
                ShowErrorMessage(errorMessage);
            }
        } catch (Exception e) {
            Log.e("GeneratePolicy", e.toString());
        }
    }

    private void ShowErrorMessage(final String errorMessage)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMessage.equals("authentication_required")) {
                    App.MakeToastMessage("정책을 만들 권한이 없습니다");
                } else if (errorMessage.equals("policy_create_failed")) {
                    App.MakeToastMessage("서버 오류 또는 잘못된 정책 설정에 의해 실패했습니다");
                }
            }
        });
    }
}
