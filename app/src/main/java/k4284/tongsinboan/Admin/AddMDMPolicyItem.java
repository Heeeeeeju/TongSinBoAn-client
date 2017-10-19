package k4284.tongsinboan.Admin;

/**
 * Created by Administrator on 2017-10-18.
 */

public class AddMDMPolicyItem {

    private String policyName;
    private int selectedValue;

    public void SetPolicyName(String name)
    {
        this.policyName = name;
    }

    public String GetPolicyName()
    {
        return this.policyName;
    }

    public void SetSelectedValue(int value)
    {
        this.selectedValue = value;
    }

    public int GetSelectedValue()
    {
        if (1 == selectedValue) {
            return 0;
        } else if (0 == selectedValue) {
            return 1;
        }
        return this.selectedValue;
    }
}
