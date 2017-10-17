package k4284.tongsinboan.Scanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScannerFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    public ScannerFragment()
    {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (null != result) {
            if (null == result.getContents()) {
                Toast.makeText(getContext(), "스캔을 취소했습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                // TODO : 출입 데이터 서버 전송
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
        return view;
    }

    public void OpenScanner()
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
}
