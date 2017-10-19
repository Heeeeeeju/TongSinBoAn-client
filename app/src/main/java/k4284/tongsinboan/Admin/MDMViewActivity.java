package k4284.tongsinboan.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class MDMViewActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (App.ADD_POLICY == resultCode) {
            String policyString = data.getStringExtra("policies");
            try {
                JSONObject policy = new JSONObject(policyString);
                UpdatePolicyList(policy);
            } catch (Exception e) {
                Log.e("MDMView", e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_view);

        try {
            String policyString = getIntent().getStringExtra("data");
            JSONObject policy = new JSONObject(policyString);

            TextView policyNameView = (TextView) findViewById(R.id.mdm_view_name);
            policyNameView.setText(policy.getString("name"));

            TextView policyDetailView = (TextView) findViewById(R.id.mdm_view_detail);
            policyDetailView.setText(policy.getString("comment"));

            UpdatePolicyList(policy);
        } catch (Exception e) {
            Log.e("MDMViewActivity", e.toString());
        }

        Button editPolicy = (Button) findViewById(R.id.mdm_view_edit);
        editPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMDMPolicyActivity.class);
                String policyString = getIntent().getStringExtra("data");
                intent.putExtra("data", policyString);
                startActivityForResult(intent, App.ADD_POLICY);
            }
        });

        Button removePolicy = (Button) findViewById(R.id.mdm_view_remove);
        removePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button addAdmin = (Button) findViewById(R.id.mdm_view_add_admin);
        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        Button addUser = (Button) findViewById(R.id.mdm_view_add_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void UpdatePolicyList(JSONObject policy)
    {
        try {
            AddMDMPolicyAdapter adapter = new AddMDMPolicyAdapter(false);
            for (int i = 0; i < App.MdmKeys.length; i++) {
                int value = policy.getInt(App.MdmKeys[i]);
                adapter.AddItem(App.MdmNames[i], value);
            }

            ListView policyList = (ListView) findViewById(R.id.mdm_view_policy_list);
            policyList.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("MDMView", e.toString());
        }
    }
}
