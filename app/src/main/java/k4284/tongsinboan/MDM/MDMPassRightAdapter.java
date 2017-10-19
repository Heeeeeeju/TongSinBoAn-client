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
 * Created by Administrator on 2017-10-19.
 */

public class MDMPassRightAdapter extends BaseAdapter {

    private ArrayList<MDMPassRightItem> listViewItemList = new ArrayList<MDMPassRightItem>();

    public MDMPassRightAdapter()
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
            convertView = inflater.inflate(R.layout.mdm_pass_right_listview_item, parent, false);
        }

        TextView passNameView = convertView.findViewById(R.id.mdm_pass_right_name) ;
        TextView passDetailView = convertView.findViewById(R.id.mdm_pass_right_detail) ;

        MDMPassRightItem listViewItem = listViewItemList.get(position);

        passNameView.setText(listViewItem.GetPassRightName());
        passDetailView.setText(listViewItem.GetPassRightDetail());

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

    public void AddItem(String name, String detail)
    {
        MDMPassRightItem item = new MDMPassRightItem();

        item.SetPassRightName(name);
        item.SetPassRightDetail(detail);

        listViewItemList.add(item);
    }
}
