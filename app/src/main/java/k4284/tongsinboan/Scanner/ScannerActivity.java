package k4284.tongsinboan.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScannerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void SendScanResultToServer(final String result)
    {
        new Thread() {
            public void run() {
                // TODO :
//                String requestName = "";
//                JSONObject param = new JSONObject();
//                param.put("", result);
//
//                JSONObject response = App.ServerRequest(requestName, App.REQUEST_POST, param);
//                HandleScanResult(response);
            }
        }.start();
    }

    private void HandleScanResult(JSONObject response)
    {

    }
}
