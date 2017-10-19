package k4284.tongsinboan.Admin;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import k4284.tongsinboan.App;
import k4284.tongsinboan.R;

/**
 * Created by Administrator on 2017-10-18.
 */

public class MDMPeopleListAdapter extends BaseAdapter {

    private ArrayList<MDMPeopleListItem> listViewItemList = new ArrayList<MDMPeopleListItem>();

    public MDMPeopleListAdapter() {

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
            convertView = inflater.inflate(R.layout.mdm_people_listview_item, parent, false);
        }

        MDMPeopleListItem listViewItem = listViewItemList.get(position);

        TextView nameView = convertView.findViewById(R.id.mdm_people_item_name);
        nameView.setText(listViewItem.GetName());

        ImageView profileImageView = convertView.findViewById(R.id.mdm_people_item_profile_image);
        Picasso.with(context)
                .load(listViewItem.GetProfileImage())
                .placeholder(R.drawable.default_profile)
                .into(profileImageView);

        TextView belongView = convertView.findViewById(R.id.mdm_people_item_belong);
        belongView.setText(listViewItem.GetBelong());

        ImageView checkView = convertView.findViewById(R.id.mdm_people_check_state);
        Button remove = convertView.findViewById(R.id.mdm_people_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveFromPolicy(position);
                listViewItemList.remove(position);
                notifyDataSetChanged();
            }
        });
        if (listViewItem.GetShowCheck()) {
            checkView.setVisibility(View.VISIBLE);
            remove.setVisibility(View.INVISIBLE);
        } else {
            checkView.setVisibility(View.INVISIBLE);
            remove.setVisibility(View.VISIBLE);
        }

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

    public void AddItem(String name, Uri image, String belong) {
        AddItem(name, image, belong, true);
    }

    public void AddItem(String name, Uri image, String belong, boolean showCheck)
    {
        AddItem(name, image, belong, showCheck, -1, -1, false);
    }

    public void AddItem(String name, Uri image, String belong, boolean showCheck, int policyIdx, int userIdx, boolean isAdmin)
    {
        MDMPeopleListItem item = new MDMPeopleListItem();

        item.SetName(name);
        item.SetProfileImage(image);
        item.SetBelong(belong);
        item.SetShowCheck(showCheck);
        item.SetPolicyIdx(policyIdx);
        item.SetUserIdx(userIdx);
        item.SetIsAdmin(isAdmin);

        listViewItemList.add(item);
    }

    private void RemoveFromPolicy(int position)
    {
        int policyIdx = listViewItemList.get(position).GetPolicyIdx();
        int userIdx = listViewItemList.get(position).GetUserIdx();
        String userType = "/user/";
        if (listViewItemList.get(position).GetIsAdmin()) {
            userType = "/admin/";
        }
        final String requestName = "/policy/" + policyIdx + userType + userIdx;
        new Thread() {
            public void run() {
                JSONObject response = App.ServerRequest(App.REQUEST_DELETE, requestName);
                Log.d("RemoveFromServer", response.toString());
            }
        }.start();
    }
}