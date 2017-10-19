package k4284.tongsinboan.Scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScannerListFragment extends Fragment {

    private ListView policyList;
    private String selectedPolicyName;
    private String selectedPolicyIdx;

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    public ScannerListFragment()
    {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result) {
            if (null == result.getContents()) {
                App.MakeToastMessage("스캔을 취소했습니다");
            } else {
                Log.d("Scanner", result.getContents());
                SendScanResultToServer(result.getContents());
                // TODO
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        policyList = view.findViewById(R.id.scanner_policy_list);
        policyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScannerListItem item = (ScannerListItem)parent.getItemAtPosition(position);
                selectedPolicyName = item.GetPolicyName();
                selectedPolicyIdx = item.GetPolicyIdx();
                OpenScanner();
            }
        });
        GetPolicyListFromServer();

        return view;
    }

    private void GetPolicyListFromServer()
    {
        new Thread() {
            public void run() {
                String requestName = "/policy/admin";
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        JSONArray policies = response.getJSONArray("data");
                        SetPolicyInAdapter(policies);
                    } else {
                        String errorMessage = response.getString("msg");
                        ShowGetPolicyErrorMessage(errorMessage);
                    }
                } catch (Exception e) {
                    Log.e("GetPolicyListFromServer", e.toString());
                }
            }
        }.start();
    }

    private void SetPolicyInAdapter(JSONArray policies)
    {
        int size = policies.length();
        final ScannerListAdapter adapter = new ScannerListAdapter();
        for (int i=0; i<size; i++) {
            try {
                JSONObject policy = policies.getJSONObject(i);
                String policyName = policy.getString("name");
                String policyDetail = policy.getString("comment");
                String policyIdx = policy.getString("idx");
                adapter.AddItem(policyName, policyDetail, policyIdx);
            } catch (Exception e) {
                Log.e("SetPolicyInAdapter", e.toString());
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                policyList.setAdapter(adapter);
            }
        });

    }

    private void ShowGetPolicyErrorMessage(String errorMessage)
    {
        if (errorMessage.equals("authentication_required")) {
            App.MakeToastMessage("로그인 되어있지 않습니다");
        } else if (errorMessage.equals("policy_read_failed")) {
            App.MakeToastMessage("서버 오류로 인해 정책 조회에 실패했습니다");
        }
    }

    private void OpenScanner()
    {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(ScannerCaptureActivity.class);
        integrator.initiateScan();
    }

    public void SendScanResultToServer(final String token)
    {
        new Thread() {
            public void run() {
                try {
                    String requestName = "/policy/verify/" + selectedPolicyIdx;
                    JSONObject param = new JSONObject();
                    param.put("token", token);

                    JSONObject response = App.ServerRequest(App.REQUEST_POST, requestName, param);
                    HandleScanResult(response);
                } catch (Exception e) {
                    Log.e("SendScanResultToServer", e.toString());
                }
            }
        }.start();
    }

    private void HandleScanResult(JSONObject response)
    {
        try {
            Intent intent = new Intent(getContext(), ScanResultActivity.class);

            boolean result = response.getBoolean("result");
            if (!result) {
                String errorMessage = response.getString("msg");
                ShowScanErrorMessage(errorMessage);
            } else {
                JSONObject data = response.getJSONObject("data");
                intent.putExtra("userData", data.toString());
                intent.putExtra("result", result);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("HandleScanResult", e.toString());
        }
    }

    private void ShowScanErrorMessage(final String errorMessage)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorMessage.equals("authentication_required")) {
                    App.MakeToastMessage("검증 권한이 없습니다");
                } else if (errorMessage.equals("no_permission")) {
                    App.MakeToastMessage("정책에 대한 권한이 없습니다");
                } else if (errorMessage.equals("policy_verify_failed")) {
                    App.MakeToastMessage("서버 오류로 인해 정책 검증에 실패했습니다");
                }
            }
        });
    }
}