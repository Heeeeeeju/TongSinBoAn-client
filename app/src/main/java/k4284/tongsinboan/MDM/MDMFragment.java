package k4284.tongsinboan.MDM;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MDMFragment extends Fragment {

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

        Drawable policyOff = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_off, null);
        Drawable policyOn = ResourcesCompat.getDrawable(getResources(), R.drawable.policy_on, null);

        MDMPolicyAdapter adapter = new MDMPolicyAdapter();
        adapter.AddItem("전화", policyOff);
        adapter.AddItem("와이파이", policyOff);
        adapter.AddItem("카메라", policyOff);
        adapter.AddItem("GPS", policyOff);
        adapter.AddItem("블루투스", policyOff);

        ListView policyList = view.findViewById(R.id.mdm_policy_list);
        policyList.setAdapter(adapter);

        ListView passRightList = view.findViewById(R.id.mdm_pass_right_list);

        return view;
    }
}
