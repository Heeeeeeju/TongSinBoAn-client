package k4284.tongsinboan.MDM;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MDMFragment extends Fragment {

    Drawable policyOff;
    Drawable policyOn;

    ListView policyList;
    ListView passRightList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    public MDMFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LoadPolicyFromServer();
        LoadPassRightFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mdm, container, false);

        policyOff = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_off, null);
        policyOn = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_on, null);

        // TODO : 서버에서 정책 값 받아와서 적용
        MDMPolicyAdapter adapter = new MDMPolicyAdapter();
        adapter.AddItem("전화", policyOff);
        adapter.AddItem("와이파이", policyOff);
        adapter.AddItem("카메라", policyOff);
        adapter.AddItem("GPS", policyOff);
        adapter.AddItem("블루투스", policyOff);

        policyList = view.findViewById(R.id.mdm_policy_list);
        policyList.setAdapter(adapter);

        passRightList = view.findViewById(R.id.mdm_pass_right_list);

        return view;
    }

    private void LoadPolicyFromServer()
    {
        new Thread() {
            public void run() {
                String requestName = "/policy";
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        SetPolicy(response.getJSONObject("data"));
                    } else {
                        String errorMessage = response.getString("msg");
                        if (errorMessage.equals(App.ERROR_NOT_LOGIN)) {
                            App.MakeToastMessage(App.ERROR_NOT_LOGIN_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    Log.e("MDM", e.toString());
                }
            }
        }.start();
    }

    private void SetPolicy(JSONObject data)
    {
        

        MDMPolicyAdapter adapter = (MDMPolicyAdapter) policyList.getAdapter();
        int policySize = adapter.getCount();
        for(int i=0; i<policySize; i++) {
            MDMPolicyItem item = (MDMPolicyItem) adapter.getItem(i);
            // TODO : policy On/Off 데이터 파싱해서 이미지 변경
            item.SetPolicyValue(policyOn);
        }
    }

    private void LoadPassRightFromServer()
    {
        new Thread() {
            public void run() {
                String requestName = "";
                final JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SetPassRight(response);
                    }
                });
            }
        }.start();
    }

    private void SetPassRight(JSONObject response)
    {
        // TODO :
//        String[] passRight;
//
//        int itemLayout = android.R.layout.simple_list_item_1;
//        int textId = android.R.id.text1;
//        ArrayAdapter<String> adapter
//                = new ArrayAdapter<String>(getContext(), itemLayout, textId, passRight);
//        passRightList.setAdapter(adapter);
    }
}
