package k4284.tongsinboan.Admin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import k4284.tongsinboan.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageMDMPolicyFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));
    }

    public ManageMDMPolicyFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_mdm_policy, container, false);

        ManageMDMPolicyAdapter adapter = new ManageMDMPolicyAdapter();
        // TODO : 서버에서 정책 받아오기
        adapter.AddItem("정책 1");
        adapter.AddItem("정책 2");
        adapter.AddItem("정책 3");

        ListView policyList = view.findViewById(R.id.mdm_admin_policy_list);
        policyList.setAdapter(adapter);
        policyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO : 서버 통신
                Intent intent = new Intent(getContext(), MDMPeopleListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
