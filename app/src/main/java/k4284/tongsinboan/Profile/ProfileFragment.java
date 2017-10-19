package k4284.tongsinboan.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == App.User.profileImageUri) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "프로필 이미지 선택"), App.PICK_IMAGE);
                }
            }
        });

        userNameView = view.findViewById(R.id.profile_name);
        groupNameView = view.findViewById(R.id.profile_group);

        SetProfileData();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (App.PICK_IMAGE == requestCode) {
            Uri selectedImage = data.getData();
            UploadProfileImage(selectedImage);
        }
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

    private void UploadProfileImage(final Uri selectedImage)
    {
        new Thread() {
            public void run() {
                String requestName = "/upload";
                byte[] image = UriToBytes(selectedImage);
                JSONObject response = App.ServerRequest(App.REQUEST_POST, requestName, image);

                try {
                    boolean result = response.getBoolean("result");
                    if (result) {
                        UpdateUserData();
                    } else {
                        String errorMessage = response.getString("msg");
                        ShowErrorMessage(errorMessage);
                    }
                } catch (Exception e) {
                    Log.e("Profile", e.toString());
                }
            }
        }.start();
    }

    private void UpdateUserData()
    {
        new Thread() {
            public void run() {
                String requestName = "/member/me";
                JSONObject response = App.ServerRequest(App.REQUEST_GET, requestName);
                try {
                    JSONObject data = response.getJSONObject("data");
                    App.SaveUserData(data);
                    UpdateProfileImage();
                } catch (Exception e) {
                    Log.e("UpdateProfileImage", e.toString());
                }
            }
        }.start();
    }

    private void UpdateProfileImage()
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SetProfileData();
            }
        });
    }

    private void ShowErrorMessage(String errorMessage)
    {
        if (errorMessage.equals("upload_server_error")) {
            App.MakeToastMessage("서버 오류");
        } else if (errorMessage.equals("upload_img_exists")) {
            App.MakeToastMessage("이미 존재하는 프로필 이미지입니다");
        } else if (errorMessage.equals("upload_failed")) {
            App.MakeToastMessage("서버 오류로 인해 업로드 실패했습니다");
        } else if (errorMessage.equals("upload_wrong_image")) {
            App.MakeToastMessage("잘못된 이미지입니다");
        } else if (errorMessage.equals("upload_process_failed")) {
            App.MakeToastMessage("서버 오류로 인해 업로드 실패했습니다");
        }
    }

    public String GetPathFromUri(Uri uri){
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }

    public byte[] UriToBytes(Uri uri)
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (Exception e) {
            Log.e("Profile", e.toString());
        }

        return byteBuffer.toByteArray();
    }
}
