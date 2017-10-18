package k4284.tongsinboan.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import k4284.tongsinboan.R;

public class ManageMDMPolicyAdapter extends BaseAdapter {

    private ArrayList<ManageMDMPolicyItem> listViewItemList = new ArrayList<ManageMDMPolicyItem>();

    public ManageMDMPolicyAdapter()
    {
    }
    
    @Override
    public int getCount()
    {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mdm_admin_listview_item, parent, false);
        }

        ManageMDMPolicyItem listViewItem = listViewItemList.get(position);

        TextView policyNameView = convertView.findViewById(R.id.mdm_admin_item_policy_name);
        policyNameView.setText(listViewItem.GetPolicyName());

        Button policyEdit = convertView.findViewById(R.id.mdm_admin_item_policy_edit);
        policyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMDMPolicyActivity.class);
                context.startActivity(intent);
            }
        });

        Button policyRemove = convertView.findViewById(R.id.mdm_admin_item_policy_remove);
        policyRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 서버 통신 후 정책 삭제
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Object getItem(int position)
    {
        return listViewItemList.get(position);
    }

    public void AddItem(String title)
    {
        ManageMDMPolicyItem item = new ManageMDMPolicyItem();
        item.SetPolicyName(title);
        listViewItemList.add(item);
    }
}
