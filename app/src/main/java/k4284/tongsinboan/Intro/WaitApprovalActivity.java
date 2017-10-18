package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import k4284.tongsinboan.R;

public class WaitApprovalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_approval);

        final String id = getIntent().getStringExtra("id");
        final String password = getIntent().getStringExtra("password");

        TextView groupCodeView = (TextView) findViewById(R.id.wait_group_code);
        final String groupIdx = getIntent().getStringExtra("groupIdx");
        if (null != groupIdx) {
            groupCodeView.setText("부대 코드 : " + groupIdx);
        }

        Button editGroupCode = (Button) findViewById(R.id.wait_edit_group_code);
        editGroupCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitApprovalActivity.this, SelectUserTypeActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("password", password);
                intent.putExtra("groupIdx", groupIdx);
                startActivity(intent);
                finish();
            }
        });
    }
}
