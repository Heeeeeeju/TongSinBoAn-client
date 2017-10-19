package k4284.tongsinboan.Scanner;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import k4284.tongsinboan.R;

public class ScanResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5 * 1000);
    }
}
