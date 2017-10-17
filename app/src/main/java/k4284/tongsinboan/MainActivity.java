package k4284.tongsinboan;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import k4284.tongsinboan.MDM.MDMFragment;
import k4284.tongsinboan.Passport.PassportFragment;
import k4284.tongsinboan.Profile.ProfileFragment;
import k4284.tongsinboan.Scanner.ScannerFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private final int PAGE_PROFILE = 0;
    private final int PAGE_PASSPORT = 1;
    private final int PAGE_MDM = 2;
    private final int PAGE_SCANNER = 3;

    private int pageNumber = 3;

    Button tabProfile;
    Button tabPassport;
    Button tabMDM;
    Button tabScanner;

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
            String userType = intent.getStringExtra("userType");
            if (userType.equals("guard")) {
                LinearLayout scannerContainer = (LinearLayout)findViewById(R.id.main_tab_scanner_container);
                scannerContainer.setVisibility(View.VISIBLE);
                pageNumber = 4;
            } else {
                pageNumber = 3;
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
                TextView textRemainTime = passportFragment.getView().findViewById(R.id.passport_remain_time);

                if (PAGE_PASSPORT == position) {
                    ImageView qrCodeView = passportFragment.getView().findViewById(R.id.passport_qr_code);
                    passportFragment.GenerateQrCode(qrCodeView, textRemainTime);
                } else {
                    passportFragment.DeleteQrCode();
                    passportFragment.UpdateRemainTime(passportFragment.RE_GENERATE_TIME, textRemainTime);

                    if (PAGE_SCANNER == position) {
                        ScannerFragment scannerFragment =
                                (ScannerFragment) viewPager.getAdapter().instantiateItem(viewPager, PAGE_SCANNER);
                        scannerFragment.OpenScanner();
                    }
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
    }

    private void SelectTab(int position)
    {
        tabProfile.setBackgroundColor(App.UnSelectedColor);
        tabPassport.setBackgroundColor(App.UnSelectedColor);
        tabMDM.setBackgroundColor(App.UnSelectedColor);
        tabScanner.setBackgroundColor(App.UnSelectedColor);

        if (PAGE_PROFILE == position) {
            tabProfile.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_PASSPORT == position) {
            tabPassport.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_MDM == position) {
            tabMDM.setBackgroundColor(App.SelectedColor);
        } else if (PAGE_SCANNER == position) {
            tabScanner.setBackgroundColor(App.SelectedColor);
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter
    {
        Fragment fragmentProfile;
        Fragment fragmentPassport;
        Fragment fragmentMDM;
        Fragment fragmentScanner;

        public PagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case PAGE_PROFILE:
                    if (null == fragmentProfile)
                        return new ProfileFragment();
                    return fragmentProfile;
                case PAGE_PASSPORT:
                    if (null == fragmentPassport)
                        return new PassportFragment();
                    return fragmentPassport;
                case PAGE_MDM:
                    if (null == fragmentMDM)
                        return new MDMFragment();
                    return fragmentMDM;
                case PAGE_SCANNER:
                    if (null == fragmentScanner)
                        return new ScannerFragment();
                    return fragmentScanner;
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return pageNumber;
        }
    }
}
