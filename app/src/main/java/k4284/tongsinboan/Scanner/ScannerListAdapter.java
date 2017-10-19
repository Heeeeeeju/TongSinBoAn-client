package k4284.tongsinboan.Scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import k4284.tongsinboan.R;

/**
 * Created by Administrator on 2017-10-19.
 */

public class ScannerListAdapter extends BaseAdapter {

    private ArrayList<ScannerListItem> listViewItemList = new ArrayList<ScannerListItem>();

    public ScannerListAdapter()
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
            convertView = inflater.inflate(R.layout.fragment_scanner_list, parent, false);
        }

        ScannerListItem listViewItem = listViewItemList.get(position);

        TextView policyNameView = convertView.findViewById(R.id.scanner_list_item_name);
        policyNameView.setText(listViewItem.GetPolicyName());

        TextView policyDetailView = convertView.findViewById(R.id.scanner_list_item_detail);
        policyDetailView.setText(listViewItem.GetPolicyDetail());

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

    public void AddItem(String name, String detail, String idx)
    {
        ScannerListItem item = new ScannerListItem();

        item.SetPolicyName(name);
        item.SetPolicyDetail(detail);
        item.SetPolicyIdx(idx);

        listViewItemList.add(item);
    }
}
