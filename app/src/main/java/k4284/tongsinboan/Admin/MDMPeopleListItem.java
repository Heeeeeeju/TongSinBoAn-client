package k4284.tongsinboan.Admin;

import android.net.Uri;

/**
 * Created by Administrator on 2017-10-18.
 */

public class MDMPeopleListItem {
    private Uri profileImage;
    private String name;
    private String belong;
    private boolean checked;
    private boolean showCheck;
    private int policyIdx;
    private int userIdx;
    private boolean isAdmin;

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

    public void SetBelong(String belong)
    {
        this.belong = belong;
    }

    public String GetBelong()
    {
        return this.belong;
    }

    public void SetChecked(boolean chekced)
    {
        this.checked = chekced;
    }

    public boolean GetChecked()
    {
        return this.checked;
    }

    public void SetShowCheck(boolean show)
    {
        this.showCheck = show;
    }

    public boolean GetShowCheck()
    {
        return this.showCheck;
    }

    public void SetPolicyIdx(int idx)
    {
        this.policyIdx = idx;
    }

    public int GetPolicyIdx()
    {
        return this.policyIdx;
    }

    public void SetUserIdx(int idx)
    {
        this.userIdx = idx;
    }

    public int GetUserIdx()
    {
        return this.userIdx;
    }

    public void SetIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public boolean GetIsAdmin()
    {
        return this.isAdmin;
    }
}
