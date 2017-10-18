package k4284.tongsinboan.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import k4284.tongsinboan.R;

public class MDMPeopleListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm_people_list);

        MDMPeopleListAdapter adapter = new MDMPeopleListAdapter();
        // TODO : 서버 통신 후 선택한 정책에 해당하는 사람들 추가
        adapter.AddItem("이름 1", null);
        adapter.AddItem("이름 2", null);
        adapter.AddItem("이름 3", null);
        adapter.AddItem("이름 4", null);
        adapter.AddItem("이름 5", null);

        ListView listView = (ListView)findViewById(R.id.mdm_people_listview);
        listView.setAdapter(adapter);
    }
}
