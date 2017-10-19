package k4284.tongsinboan.Scanner;

/**
 * Created by Administrator on 2017-10-19.
 */

public class ScannerListItem {
    private String policyName;
    private String policyDetail;
    private String policyIdx;

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

    public void SetPolicyIdx(String idx)
    {
        this.policyIdx = idx;
    }

    public String GetPolicyIdx()
    {
        return this.policyIdx;
    }
}
