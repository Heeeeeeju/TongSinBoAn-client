package k4284.tongsinboan.Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import k4284.tongsinboan.R;

/**
 * Created by Administrator on 2017-10-18.
 */

public class AddMDMPolicyAdapter extends BaseAdapter {

    private ArrayList<AddMDMPolicyItem> listViewItemList = new ArrayList<AddMDMPolicyItem>();
    private boolean[] isSelected;

    public AddMDMPolicyAdapter() {

    }

    public void InitIsSelected()
    {
        isSelected = new boolean[getCount()];
        for (int i=0; i<getCount(); i++) {
            isSelected[i] = false;
        }
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mdm_add_listview_item, parent, false);
        }

        AddMDMPolicyItem listViewItem = listViewItemList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.mdm_add_item_policy_name);
        titleTextView.setText(listViewItem.GetPolicyName());

        MultiStateToggleButton option = convertView.findViewById(R.id.mdm_add_item_option);
        option.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                isSelected[position] = true;
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public boolean[] GetIsSelected() {
        return isSelected;
    }

    public void AddItem(String title) {
        AddMDMPolicyItem item = new AddMDMPolicyItem();

        item.SetPolicyName(title);

        listViewItemList.add(item);
    }
}