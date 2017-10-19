package k4284.tongsinboan.Admin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageMDMPolicyFragment extends Fragment {

    private ListView policyList;
    private JSONArray data;

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    public ManageMDMPolicyFragment()
    {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (App.CHANGE_POLICY == resultCode) {
            GetPolicyFromServer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_mdm_policy, container, false);

        GetPolicyFromServer();

        Button addPolicy = view.findViewById(R.id.mdm_admin_add_policy);
        addPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMDMPolicyActivity.class);
                startActivityForResult(intent, App.CHANGE_POLICY);
            }
        });

        policyList = view.findViewById(R.id.mdm_admin_policy_list);
        policyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != data) {
                    try {
                        Intent intent = new Intent(getContext(), MDMViewActivity.class);
                        JSONObject policy = data.getJSONObject(position);
                        intent.putExtra("data", policy.toString());
                        startActivityForResult(intent, App.CHANGE_POLICY);
                    } catch (Exception e) {
                        Log.e("PolicyItemClick", e.toString());
                    }
                }
            }
        });

        return view;
    }

    private void GetPolicyFromServer()
    {
        final String requestName = "/policy/admin";
        new Thread() {
            public void run() {
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        data = response.getJSONArray("data");
                        SetPolicyInAdapter(data);
                    } else {
                        String errorMessage = response.getString("msg");
                        ShowErrorMessage(errorMessage);
                    }
                } catch (Exception e) {
                    Log.e("GetPolicyFromServer", e.toString());
                }
            }
        }.start();
    }

    private void SetPolicyInAdapter(JSONArray policies)
    {
        final ManageMDMPolicyAdapter adapter = new ManageMDMPolicyAdapter();
        int size = policies.length();
        try {
            for (int i=0; i<size; i++) {
                JSONObject policy = policies.getJSONObject(i);
                adapter.AddItem(policy);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    policyList.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            Log.e("SetPolicyInAdapter", e.toString());
        }
    }

    private void ShowErrorMessage(final String errorMessage)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMessage.equals("authentication_required")) {
                    App.MakeToastMessage(App.ERROR_NOT_LOGIN_MESSAGE);
                } else if (errorMessage.equals("policy_read_failed")) {
                    App.MakeToastMessage("서버 오류로 인해 정책 조회에 실패했습니다");
                }
            }
        });
    }
}
