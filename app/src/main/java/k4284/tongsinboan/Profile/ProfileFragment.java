package k4284.tongsinboan.Profile;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class ProfileFragment extends Fragment {

    ImageView profileImage;
    TextView userNameView;
    TextView groupNameView;

    public ProfileFragment()
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        userNameView = view.findViewById(R.id.profile_name);
        groupNameView = view.findViewById(R.id.profile_group);
        SetProfileData();

        return view;
    }

    private void SetProfileData()
    {
        Picasso.with(getContext())
                .load(App.User.profileImageUri)
                .placeholder(R.drawable.default_profile)
                .into(profileImage);
        userNameView.setText(App.User.name);
        groupNameView.setText(App.User.groupName);
    }
}
