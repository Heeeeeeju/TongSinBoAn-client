package k4284.tongsinboan.Admin;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by Administrator on 2017-10-18.
 */

public class MDMPeopleListItem {
    private Uri profileImage;
    private String name;

    public void SetProfileImage(Uri image)
    {
        this.profileImage = image;
    }

    public Uri GetProfileImage()
    {
        return this.profileImage;
    }

    public void SetName(String name)
    {
        this.name = name;
    }

    public String GetName()
    {
        return this.name;
    }
}
