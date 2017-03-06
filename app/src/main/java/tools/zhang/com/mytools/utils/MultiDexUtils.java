package tools.zhang.com.mytools.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 多dex打包，获取dex的路径
 * Created by gaokuo on 2015/8/6.
 */
public class MultiDexUtils {

    private static final String TAG = "MultiDexUtils";
    private static final boolean useMultiDex = false;

    public static synchronized List<String> getDexpath(Context context) {
        List<String> dexs = new ArrayList<String>();
        boolean IS_VM_MULTIDEX_CAPABLE;
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        dexs.add(applicationInfo.sourceDir);
        if (!useMultiDex) {
            return dexs;
        }
        try {
            Class Mclass = Class.forName("android.support.multidex.MultiDex");
            Field field = Mclass.getDeclaredField("SECONDARY_FOLDER_NAME");
            field.setAccessible(true);
            String path = (String) field.get(null);
            Log.d(TAG, "path:" + path);
            File file = new File(context.getApplicationInfo().dataDir, path);

            Class a = Class.forName("android.support.multidex.MultiDex");
            Field field_a = a.getDeclaredField("IS_VM_MULTIDEX_CAPABLE");
            field_a.setAccessible(true);
            IS_VM_MULTIDEX_CAPABLE = (Boolean) field_a.get(null);
            if (IS_VM_MULTIDEX_CAPABLE) {
                //支持，解压
                Log.d(TAG, "support");
                Class b_class = Class.forName("android.support.multidex.MultiDex");
                Method b_method = b_class.getDeclaredMethod("clearOldDexDir", Context.class);
                b_method.setAccessible(true);
                b_method.invoke(null, context);

                Class c_class = Class.forName("android.support.multidex.MultiDexExtractor");
                Method c_method = c_class.getDeclaredMethod("load", Context.class, ApplicationInfo.class, File.class, boolean.class);
                c_method.setAccessible(true);
                c_method.invoke(null, context, applicationInfo, file, false);
            }
            File file_array[] = file.listFiles();
            if (file_array != null) {
                for (File f : file_array) {
                    if (f.isFile() && f.getName().endsWith(".zip")) {
                        Log.d(TAG, "dex:" + f.getAbsolutePath());
                        dexs.add(f.getAbsolutePath());
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return dexs;
    }
}
