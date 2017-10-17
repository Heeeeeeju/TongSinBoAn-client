package k4284.tongsinboan;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import k4284.tongsinboan.MDM.MDMFragment;
import k4284.tongsinboan.Passport.PassportFragment;
import k4284.tongsinboan.Profile.ProfileFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private final int PAGE_PROFILE = 0;
    private final int PAGE_PASSPORT = 1;
    private final int PAGE_MDM = 2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PassportFragment passportFragment =
                        (PassportFragment) viewPager.getAdapter().instantiateItem(viewPager, PAGE_PASSPORT);
                if (PAGE_PASSPORT == position) {
                    ImageView qrCodeView = passportFragment.getView().findViewById(R.id.passport_qr_code);
                    TextView textRemainTime = passportFragment.getView().findViewById(R.id.passport_remain_time);
                    passportFragment.GenerateQrCode(qrCodeView, textRemainTime);
                } else {
                    passportFragment.DeleteQrCode();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PagerAdapter extends FragmentPagerAdapter
    {
        Fragment fragmentProfile;
        Fragment fragmentPassport;
        Fragment fragmentMDM;

        public PagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    if (null == fragmentProfile)
                        return new ProfileFragment();
                    return fragmentProfile;
                case 1:
                    if (null == fragmentPassport)
                        return new PassportFragment();
                    return fragmentPassport;
                case 2:
                    if (null == fragmentMDM)
                        return new MDMFragment();
                    return fragmentMDM;
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 3;
        }
    }
}
