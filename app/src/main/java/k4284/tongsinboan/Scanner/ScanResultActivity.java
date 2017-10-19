package k4284.tongsinboan.Scanner;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

public class ScanResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        TextView title = (TextView)findViewById(R.id.profile_title);
        title.setVisibility(View.INVISIBLE);

        ImageView profileImageView = (ImageView)findViewById(R.id.profile_image);
        TextView nameView = (TextView)findViewById(R.id.profile_name);
        TextView groupView = (TextView)findViewById(R.id.profile_group);
        TextView belongView = (TextView)findViewById(R.id.profile_belong);

        ImageView resultView = (ImageView)findViewById(R.id.profile_result);
        boolean result = getIntent().getBooleanExtra("result", false);
        if (result) {
            resultView.setImageResource(R.drawable.pass_success);
        } else {
            resultView.setImageResource(R.drawable.pass_fail);
        }

        try {
            JSONObject data = new JSONObject(getIntent().getStringExtra("userData"));

            String imageId = data.getString("profile_img");
            String imageUri = App.ServerDomain + "/upload/" + imageId;
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .placeholder(R.drawable.default_profile)
                    .into(profileImageView);

            String userName = data.getString("name");
            nameView.setText(userName);

            String groupName = data.getString("group_name");
            groupView.setText(groupName);

            String belong = data.getString("belong");
            if (null != belong) {
                belongView.setText(belong);
            } else {
                belongView.setText(App.NoBelong);
            }

        } catch (Exception e) {
            Log.e("ScanResult", e.toString());
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5 * 1000);
    }
}
