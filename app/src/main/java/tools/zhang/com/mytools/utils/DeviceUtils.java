package tools.zhang.com.mytools.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

import tools.zhang.com.mytools.utils.context.ContextUtils;
import tools.zhang.com.mytools.utils.log.LogUtils;
import tools.zhang.com.mytools.utils.md5.Md5Utils;
import tools.zhang.com.mytools.utils.sp.SPUtils;

public class DeviceUtils {

    private static final String DEFAULT_IMEI = "360_DEFAULT_IMEI";
    private static final String APP_STORE_IMEI0 = "hot_vedio_imei0_new";
    private static final String APP_STORE_IMEI = "hot_vedio_imei";
    public static int screen_width = 0;
    public static int screen_height = 0;
    private static String sImei_md5;

    public static boolean isDebuggable() {
        try {
            PackageManager pm = ContextUtils.getApplicationContext().getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(ContextUtils.getApplicationContext().getPackageName(), 0);
            return (0 != (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context ctx) {
        if (screen_width == 0) {
            Display screenSize = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            screen_width = screenSize.getWidth();
        }

        return screen_width;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context ctx) {
        if (screen_height == 0) {
            Display screenSize = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            screen_height = screenSize.getHeight();
        }
        return screen_height;
    }

    /**
     * 获取设备ID号(国际移动设备身份码，储存在移动设备中，全球唯一的一组号码)
     *
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            String sImei = (String) SPUtils.get(context, APP_STORE_IMEI0, "");
            if (!TextUtils.isEmpty(sImei)) {
                return new String(Base64.decode(sImei, Base64.DEFAULT));
            }
            sImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (!TextUtils.isEmpty(sImei)) {
                String sImeiBase64 = Base64.encodeToString(sImei.getBytes(), Base64.DEFAULT);
                SPUtils.put(context, APP_STORE_IMEI0, sImeiBase64);
                return sImei;
            }
        } catch (Exception e) {
        }
        return DEFAULT_IMEI;
    }

    public static String getIMEIMd5(Context c) {
        if (!TextUtils.isEmpty(sImei_md5)) {
            return sImei_md5;
        }

        try {
            sImei_md5 = Md5Utils.md5LowerCase(getIMEI(c));
            return sImei_md5;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getIMEI2(Context context) {
        String sImei2 = (String) SPUtils.get(context, APP_STORE_IMEI, "");

        if (!TextUtils.isEmpty(sImei2)) {
            return sImei2;
        }

        String imei = getIMEI(context);
        String androidId = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String serialNo = getDeviceSerial();
        sImei2 = Md5Utils.md5LowerCase("" + imei + androidId + serialNo);
        LogUtils.d("getIMEI2", "imei = " + imei + ", androidId = " + androidId + ", serialNo = " + serialNo + ", sImei2 = " + sImei2);
        SPUtils.put(context, APP_STORE_IMEI, sImei2);
        return sImei2;
    }

    private static String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    private static int statusBarHeight;
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0)
            return statusBarHeight;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}