package k4284.tongsinboan;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-10-18.
 */

public class CameraDisableReceiver extends DeviceAdminReceiver {

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("CameraReceiver", "onDisabled");
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("CameraReceiver", "onEnabled");
    }
}
