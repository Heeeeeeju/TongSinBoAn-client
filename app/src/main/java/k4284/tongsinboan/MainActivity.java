package k4284.tongsinboan;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import k4284.tongsinboan.MDM.MDMFragment;
import k4284.tongsinboan.Profile.Profile;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private class PagerAdapter extends FragmentStatePagerAdapter
    {
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
                    return new MDMFragment();
                case 1:
                    return new Profile();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }
    }
}
