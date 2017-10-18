package k4284.tongsinboan.Admin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

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
        Picasso.with(context).load(listViewItem.GetProfileImage()).into(profileImageView);

        Button remove = convertView.findViewById(R.id.mdm_people_item_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 서버 통신 후 해당 정책에서 삭제
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

    public void AddItem(String name, Uri image) {
        MDMPeopleListItem item = new MDMPeopleListItem();

        item.SetName(name);
        item.SetProfileImage(image);

        listViewItemList.add(item);
    }
}