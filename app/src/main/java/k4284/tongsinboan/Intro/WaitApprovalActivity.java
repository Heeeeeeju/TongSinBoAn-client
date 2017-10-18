package k4284.tongsinboan.Intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import k4284.tongsinboan.R;

public class WaitApprovalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_approval);

        String groupIdx = getIntent().getStringExtra("groupIdx");
        TextView groupCodeView = (TextView) findViewById(R.id.wait_group_code);
        groupCodeView.setText("부대 코드 : " + groupIdx);

        Button editGroupCode = (Button) findViewById(R.id.wait_edit_group_code);
        editGroupCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitApprovalActivity.this, SelectUserTypeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
