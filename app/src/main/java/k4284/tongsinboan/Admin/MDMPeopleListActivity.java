package k4284.tongsinboan.Admin;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class MDMPeopleListActivity extends AppCompatActivity {

    private ListView adminList;
    private boolean[] checkedList;
    private int[] userIdxList;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_people_list);

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        adminList = (ListView)findViewById(R.id.mdm_people_listview);
        adminList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MDMPeopleListItem item = (MDMPeopleListItem)parent.getItemAtPosition(position);
                boolean checked = !item.GetChecked();
                item.SetChecked(checked);
                UpdateRow(position, checked);
                checkedList[position] = checked;
            }
        });
        new Thread() {
            public void run() {
                GetMemberList();
            }
        }.start();

        Button submit = (Button)findViewById(R.id.mdm_people_add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        AddPolicy(isAdmin);
                    }
                }.start();
            }
        });
    }

    private void AddPolicy(boolean isAdmin)
    {
        int policyIdx = getIntent().getIntExtra("policyIdx", -1);
        String requestName = "/policy/" + policyIdx;
        if (isAdmin) {
            requestName += "/admin";
        } else {
            requestName += "/user";
        }

        try {
            for (int i = 0; i < userIdxList.length; i++) {
                if (checkedList[i]) {
                    JSONObject param = new JSONObject();
                    param.put("member_idx", userIdxList[i]);

                    App.ServerRequest(App.REQUEST_POST, requestName, param);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

    private void GetMemberList()
    {
        try {
            String requestName = "/member";

            JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
            boolean result = response.getBoolean("result");
            if (result) {
                JSONArray data = response.getJSONArray("data");
                UpdateAdminList(data);
            } else {
//                App.ShowErrorMessage(errorMessage);
            }
        } catch (Exception e) {
            Log.e("MDMPeople", e.toString());
        }
    }

    private void UpdateAdminList(JSONArray data)
    {
        try {
            final MDMPeopleListAdapter adapter = new MDMPeopleListAdapter();
            int size = data.length();
            checkedList = new boolean[size];
            userIdxList = new int[size];
            for (int i=0; i<size; i++) {
                JSONObject admin = data.getJSONObject(i);

                String name = admin.getString("name");

                String belong = admin.getString("belong");
                if (belong.equals("null")) {
                    belong = App.NoBelong;
                }

                String profileImage
                        = App.ServerDomain + "/upload/" + admin.getString("profile_img");
                Uri profileUri = Uri.parse(profileImage);

                userIdxList[i] = admin.getInt("idx");

                adapter.AddItem(name, profileUri, belong);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adminList.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            Log.e("UpdateAdminList", e.toString());
        }
    }

    private void UpdateRow(int index, boolean checked)
    {
        View view = adminList.getChildAt(index - adminList.getFirstVisiblePosition());
        if(view == null) {
            return;
        }

        ImageView policyView = view.findViewById(R.id.mdm_people_check_state);
        if (checked) {
            policyView.setImageResource(R.drawable.check);
        } else {
            policyView.setImageResource(R.drawable.uncheck);
        }
    }
}
