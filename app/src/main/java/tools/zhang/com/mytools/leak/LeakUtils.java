package tools.zhang.com.mytools.leak;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

import tools.zhang.com.mytools.utils.ReflectUtils;

/**
 * @author Chenyichang
 */
public class LeakUtils {
    private static final String TAG = "LeakUtils";

    /**
     * 通过反射方法将InputMethodManager中持有Activity context的变量置为空
     * 请在activity的ondestroy中调用，内存泄漏文件详见/doc/内存泄漏/inputmethodleak.hprof
     *
     * @param destContext
     */
    public static void fixLeak(Context destContext) {
        fixInputMethodManagerLeak(destContext);
        fixSystemSensorMgr(destContext);
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                f.setAccessible(true);
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null);
                    } else {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /*酷派SystemSensorManager*/
    public static void fixSystemSensorMgr(Context destContext) {
        if (destContext == null) {
            return;
        }

        try {
            SensorManager sensorManager = (SensorManager) destContext.getSystemService(Context.SENSOR_SERVICE);
            Object context = ReflectUtils.getFieldValue(sensorManager, "mAppContextImpl");
            if (context.equals(destContext)) {
                ReflectUtils.setFieldValue(sensorManager, "mAppContextImpl", null);
            }
        } catch (Throwable e) {
        }

    }

    /*
    * 奇酷手机的内存泄漏，和对方聊过，5.1 以后rom 已经修复
    * 应用本身没法解
    * */
    public static void fixLargeViewBackGroundUtil(Context destContext) {
        if (destContext == null) {
            return;
        }
    }
}
