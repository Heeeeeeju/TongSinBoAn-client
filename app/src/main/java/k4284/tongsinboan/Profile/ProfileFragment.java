package k4284.tongsinboan.Profile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import k4284.tongsinboan.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LoadProfile();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void LoadProfile()
    {
        // TODO : Load Image, Name, Department
        ImageView profileImage = getActivity().findViewById(R.id.profile_image);
//        Picasso.with(getContext())
//                .load()
//                .into(profileImage);

        TextView name = getActivity().findViewById(R.id.profile_name);

        TextView department = getActivity().findViewById(R.id.profile_department);
    }
}
