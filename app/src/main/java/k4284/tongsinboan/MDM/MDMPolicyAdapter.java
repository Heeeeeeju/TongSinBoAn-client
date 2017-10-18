package k4284.tongsinboan.MDM;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import k4284.tongsinboan.R;

/**
 * Created by Administrator on 2017-10-17.
 */

public class MDMPolicyAdapter extends BaseAdapter {

    private ArrayList<MDMPolicyItem> listViewItemList = new ArrayList<MDMPolicyItem>();

    public MDMPolicyAdapter()
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
            convertView = inflater.inflate(R.layout.mdm_user_listview_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.mdm_user_item_policy_name) ;
        ImageView iconImageView = convertView.findViewById(R.id.mdm_user_item_policy_value) ;

        MDMPolicyItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.GetPolicyName());
        iconImageView.setImageDrawable(listViewItem.GetPolicyValue());

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

    public void AddItem(String title, Drawable icon)
    {
        MDMPolicyItem item = new MDMPolicyItem();

        item.SetPolicyName(title);
        item.SetPolicyValue(icon);

        listViewItemList.add(item);
    }
}
