package k4284.tongsinboan;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import k4284.tongsinboan.Admin.ManageMDMPolicyFragment;
import k4284.tongsinboan.MDM.MDMFragment;
import k4284.tongsinboan.Passport.PassportFragment;
import k4284.tongsinboan.Profile.ProfileFragment;
import k4284.tongsinboan.Scanner.ScannerActivity;
import k4284.tongsinboan.Scanner.ScannerListFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private final int PAGE_PROFILE = 0;
    private final int PAGE_PASSPORT = 1;
    private final int PAGE_MDM = 2;
    private final int PAGE_SCANNER = 3;
    private final int PAGE_MANAGE = 3;

    private int pageNumber = 3;

    private final int USER = 0;
    private final int ADMIN = 1;
    private final int GUARD = 2;
    private int userType = USER;

    Button tabProfile;
    Button tabPassport;
    Button tabMDM;
    Button tabScanner;
    Button tabManage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (null != intent) {
            String userName = intent.getStringExtra("userType");
            if (userName.equals("user")) {
                pageNumber = 3;
                userType = USER;
            } else {
                pageNumber = 4;
                if (userName.equals("guard")) {
                    userType = GUARD;
                    LinearLayout scannerContainer = (LinearLayout)findViewById(R.id.main_tab_scanner_container);
                    scannerContainer.setVisibility(View.VISIBLE);
                } else if (userName.equals("admin")) {
                    userType = ADMIN;
                    LinearLayout manageContainer = (LinearLayout)findViewById(R.id.main_tab_manage_container);
                    manageContainer.setVisibility(View.VISIBLE);
                }
            }
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(pageNumber);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SelectTab(position);

                PassportFragment passportFragment =
                        (PassportFragment) viewPager.getAdapter().instantiateItem(viewPager, PAGE_PASSPORT);

                if (PAGE_PASSPORT == position) {
                    passportFragment.PassTokenRequest();
                } else {
                    passportFragment.DeleteQrCode();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabProfile = (Button)findViewById(R.id.main_tab_profile);
        tabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTab(PAGE_PROFILE);
                viewPager.setCurrentItem(PAGE_PROFILE, true);
            }
        });

        tabPassport = (Button)findViewById(R.id.main_tab_passport);
        tabPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTab(PAGE_PASSPORT);
                viewPager.setCurrentItem(PAGE_PASSPORT, true);
            }
        });

        tabMDM = (Button)findViewById(R.id.main_tab_mdm);
        tabMDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTab(PAGE_MDM);
                viewPager.setCurrentItem(PAGE_MDM, true);
            }
        });

        tabScanner = (Button)findViewById(R.id.main_tab_scanner);
        tabScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTab(PAGE_SCANNER);
                viewPager.setCurrentItem(PAGE_SCANNER, true);
            }
        });

        tabManage = (Button)findViewById(R.id.main_tab_manage);
        tabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTab(PAGE_MANAGE);
                viewPager.setCurrentItem(PAGE_MANAGE, true);
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(bluetoothReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        int currentPage = viewPager.getCurrentItem();
        if (2 == App.User.level && PAGE_SCANNER == currentPage) {
            Fragment scannerListFragment = ((PagerAdapter) viewPager.getAdapter()).fragmentScanner;
            scannerListFragment.onActivityResult(requestCode, resultCode, data);
        } else if (PAGE_PROFILE == currentPage) {
            Fragment profileFragment = ((PagerAdapter) viewPager.getAdapter()).fragmentProfile;
            profileFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (App.DEVICE_ADMIN == requestCode) {
            if (-1 != resultCode) {
                // TODO : make below public
                // TODO : Toast 메세지 띄워주기 현재는 방법을 못찾았음
            }
        }
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        App.MakeToastMessage("정책에 의해 블루투스 사용이 불가능합니다");
                        Log.d("MainActivity", "Bluetooth STATE_TURNING_ON");
                    case BluetoothAdapter.STATE_ON:
                        Log.d("MainActivity", "Bluetooth STATE_ON");
//                        DisableBluetooth();
                        // TODO :
                        break;
                }
            }
        }
    };

    private void SelectTab(int position)
    {
        tabProfile.setBackgroundColor(App.UnSelectedColor);
        tabPassport.setBackgroundColor(App.UnSelectedColor);
        tabMDM.setBackgroundColor(App.UnSelectedColor);
        tabScanner.setBackgroundColor(App.UnSelectedColor);
        tabManage.setBackgroundColor(App.UnSelectedColor);

        if (PAGE_PROFILE == position) {
            tabProfile.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_PASSPORT == position) {
            tabPassport.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_MDM == position) {
            tabMDM.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_SCANNER == position) {
            tabScanner.setBackgroundColor(App.SelectedColor);
            tabManage.setBackgroundColor(App.SelectedColor);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter
    {
        private Fragment fragmentProfile;
        private Fragment fragmentPassport;
        private Fragment fragmentMDM;
        private Fragment fragmentScanner;
        private Fragment fragmentManage;

        public PagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            // TODO : Refactoring
            if (PAGE_PROFILE == position) {
                if (null == fragmentProfile)
                    fragmentProfile = new ProfileFragment();
                return fragmentProfile;
            } else if (PAGE_PASSPORT == position) {
                if (null == fragmentPassport)
                    fragmentPassport = new PassportFragment();
                return fragmentPassport;
            } else if (PAGE_MDM == position) {
                if (null == fragmentMDM)
                    fragmentMDM = new MDMFragment();
                return fragmentMDM;
            } else if (PAGE_SCANNER == position) {
                if (GUARD == userType) {
                    if (null == fragmentScanner)
                        fragmentScanner = new ScannerListFragment();
                    return fragmentScanner;
                } else if (ADMIN == userType) {
                    if (null == fragmentManage)
                        fragmentManage = new ManageMDMPolicyFragment();
                    return fragmentManage;
                }
            }

            return null;
        }
        @Override
        public int getCount()
        {
            return pageNumber;
        }
    }
}
