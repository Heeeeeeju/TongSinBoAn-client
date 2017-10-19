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
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        LoadPolicyFromServer();
        LoadPassRightFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mdm, container, false);

        policyOption = new Drawable[2];
        policyOption[PolicyOn] = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_on, null);
        policyOption[PolicyOff] = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_off, null);

        MDMPolicyAdapter adapter = new MDMPolicyAdapter();
        adapter.AddItem("카메라", policyOption[PolicyOff]);
        adapter.AddItem("마이크", policyOption[PolicyOff]);
        adapter.AddItem("GPS", policyOption[PolicyOff]);
        adapter.AddItem("Wifi", policyOption[PolicyOff]);
        adapter.AddItem("핫스팟", policyOption[PolicyOff]);
        adapter.AddItem("블루투스", policyOption[PolicyOff]);

        LoadPolicyFromServer();

        policyList = view.findViewById(R.id.mdm_policy_list);
        policyList.setAdapter(adapter);

        passRightList = view.findViewById(R.id.mdm_pass_right_list);

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
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SetPolicy(data);
                            }
                        }, 5 * 1000);
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
        try {
            MDMPolicyAdapter adapter = (MDMPolicyAdapter) policyList.getAdapter();

            int mdmCamera = data.getInt("mdm_camera");
            int mdmMic = data.getInt("mdm_mic");
            int mdmGps = data.getInt("mdm_gps");
            int mdmWifi = data.getInt("mdm_wifi");
            int mdmHotspot = data.getInt("mdm_hotspot");
            int mdmBluetooth = data.getInt("mdm_bluetooth");

            if (Ignore != mdmCamera) {
                DisableCamera(IntToBoolean(mdmCamera));
                MDMPolicyItem item = (MDMPolicyItem) adapter.getItem(0);
                item.SetPolicyValue(policyOption[mdmCamera]);
            }
            if (Ignore != mdmMic) {
                DisableMic(IntToBoolean(mdmMic));
                MDMPolicyItem item = (MDMPolicyItem) adapter.getItem(1);
                item.SetPolicyValue(policyOption[mdmMic]);
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
                MDMPolicyItem item = (MDMPolicyItem) adapter.getItem(5);
                item.SetPolicyValue(policyOption[mdmBluetooth]);
            }
        } catch (Exception e) {
            Log.e("MDM", e.toString());
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
