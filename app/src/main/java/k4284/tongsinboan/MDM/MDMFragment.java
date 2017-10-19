package k4284.tongsinboan.MDM;

import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.CameraDisableReceiver;
import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MDMFragment extends Fragment {

    Drawable[] policyOption;
    private int PolicyOff = 0;
    private int PolicyOn = 1;

    ListView policyList;
    ListView passRightList;

    private final int Ignore = 2;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mdm, container, false);

        policyOption = new Drawable[2];
        policyOption[PolicyOn] = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_on, null);
        policyOption[PolicyOff] = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_off, null);

        MDMPolicyAdapter policyAdapter = new MDMPolicyAdapter();
        for (int i=0; i<App.MdmNames.length; i++) {
            policyAdapter.AddItem(App.MdmNames[i], policyOption[PolicyOff]);
        }

        policyList = view.findViewById(R.id.mdm_policy_list);
        policyList.setAdapter(policyAdapter);

        passRightList = view.findViewById(R.id.mdm_pass_right_list);

        LoadPolicyFromServer();
        LoadPassRightFromServer();

        return view;
    }

    private void LoadPolicyFromServer()
    {
        new Thread() {
            public void run() {
                String requestName = "/member/me";
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        final JSONObject data = response.getJSONObject("data");
                        SetPolicyAfterDelay(data, 5 * 1000);
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

    private void SetPolicyAfterDelay(final JSONObject data, final int milliseconds)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SetPolicy(data);
                    }
                }, milliseconds);
            }
        });
    }

    private void SetPolicy(JSONObject data)
    {
        try {
            int mdmCamera = data.getInt("mdm_camera");
            int mdmMic = data.getInt("mdm_mic");
            int mdmGps = data.getInt("mdm_gps");
            int mdmWifi = data.getInt("mdm_wifi");
            int mdmHotspot = data.getInt("mdm_hotspot");
            int mdmBluetooth = data.getInt("mdm_bluetooth");

            if (Ignore != mdmCamera) {
                DisableCamera(IntToBoolean(mdmCamera));
                UpdateRow(0, policyOption[mdmCamera]);
            }
            if (Ignore != mdmMic) {
                DisableMic(IntToBoolean(mdmMic));
                UpdateRow(1, policyOption[mdmMic]);
            }
//            if (Ignore != mdmGps) {
//                DisableGps(IntToBoolean(mdmGps));
//            }
//            if (Ignore != mdmWifi) {
//                DisableWifi(IntToBoolean(mdmWifi));
//            }
//            if (Ignore != mdmHotspot) {
//                DisableHotspot(IntToBoolean(mdmHotspot));
//            }
            if (Ignore != mdmBluetooth) {
                DisableBluetooth(IntToBoolean(mdmBluetooth));
                UpdateRow(5, policyOption[mdmBluetooth]);
            }
        } catch (Exception e) {
            Log.e("MDM", e.toString());
        }
    }

    private void LoadPassRightFromServer()
    {
        new Thread() {
            public void run() {
                String requestName = "/policy";
                final JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray policies = response.getJSONArray("data");
                            SetPassRight(policies);
                        } catch (Exception e) {
                            Log.e("LoadPassRightFromServer", e.toString());
                        }
                    }
                });
            }
        }.start();
    }

    private void SetPassRight(JSONArray policies)
    {
        MDMPassRightAdapter adapter = new MDMPassRightAdapter();
        int size = policies.length();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject policy = policies.getJSONObject(i);
                String name = policy.getString("name");
                String detail = policy.getString("comment");
                adapter.AddItem(name, detail);
            }
            passRightList.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("SetPassRight", e.toString());
        }
    }

    private void UpdateRow(int index, Drawable policy)
    {
        View view = policyList.getChildAt(index - policyList.getFirstVisiblePosition());
        if(view == null) {
            return;
        }

        ImageView policyView = view.findViewById(R.id.mdm_user_item_policy_value);
        policyView.setImageDrawable(policy);
    }

    private void DisableCamera(boolean disable)
    {
        DevicePolicyManager policyManager
                = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(getContext(), CameraDisableReceiver.class);
        if (!policyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Disable Camera");
            startActivityForResult(intent, App.DEVICE_ADMIN);
        } else {
            policyManager.setCameraDisabled(componentName, disable);
        }
    }

    private void DisableMic(boolean mute)
    {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(mute);
    }

    private void DisableBluetooth(boolean disable)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (disable) {
            bluetoothAdapter.disable();
        } else {
            bluetoothAdapter.enable();
        }
    }

    private boolean IntToBoolean(int value)
    {
        if (0 == value) {
            return false;
        }
        return true;
    }
}
