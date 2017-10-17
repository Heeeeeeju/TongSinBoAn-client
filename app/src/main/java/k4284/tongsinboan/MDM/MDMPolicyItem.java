package k4284.tongsinboan.MDM;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017-10-17.
 */

public class MDMPolicyItem {

    private String policyName;
    private Drawable policyValue;

    public void SetPolicyName(String name)
    {
        policyName = name;
    }

    public void SetPolicyValue(Drawable value)
    {
        policyValue = value;
    }

    public String GetPolicyName()
    {
        return this.policyName;
    }

    public Drawable GetPolicyValue()
    {
        return this.policyValue;
    }
}
