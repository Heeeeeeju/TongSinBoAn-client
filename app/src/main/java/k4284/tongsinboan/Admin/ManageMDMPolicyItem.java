package k4284.tongsinboan.Admin;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017-10-18.
 */

public class ManageMDMPolicyItem {
    private String policyName;
    private String policyDetail;
    private JSONObject policyData;

    public void SetPolicyName(String name)
    {
        this.policyName = name;
    }

    public String GetPolicyName()
    {
        return this.policyName;
    }

    public void SetPolicyDetail(String detail)
    {
        this.policyDetail = detail;
    }

    public String GetPolicyDetail()
    {
        return this.policyDetail;
    }

    public void SetPolicyData(JSONObject data)
    {
        this.policyData = data;
    }

    public JSONObject GetPolicyData()
    {
        return this.policyData;
    }
}
