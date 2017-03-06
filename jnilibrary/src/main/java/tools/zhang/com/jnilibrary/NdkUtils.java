package tools.zhang.com.jnilibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;

import com.facebook.soloader.SoLoader;

/**
 * Created by zhangdecheng on 2016/9/6.
 */
public class NdkUtils {
    private static final String TAG = "NdkUtils";

    // 1. 加载so
//    static {
//        System.loadLibrary("myjni");
//
//    }

    // 2. load加载
    public static void loadSo(Context context) {
        String systemPath = System.getProperty("java.library.path");
        Log.e(TAG, "onCreate: path :" + systemPath);//  /vendor/lib:/system/lib


        ApplicationInfo var11 = context.getApplicationInfo();
        String lib = var11.nativeLibraryDir;
        Log.e(TAG, "onCreate: lib:" + lib); // /data/app/tools.zhang.com.mytools-2/lib/arm
        String soPath = lib +"/libmyjni.so";
        System.load(soPath);
        //System.load("/data/app/tools.zhang.com.mytools-1/lib/arm/libmyjni.so");


        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/360Download/libmyjni.so";
        System.load(path);
    }

    // 3. 通过facebook的soloader加载so（里面通过System.load加载所以不能加载64位so，否则崩溃）
    public static void initSoLoader(Context context) {
        SoLoader.init(context, false);
        SoLoader.loadLibrary("myjni");
    }




    /*
    java.lang.UnsatisfiedLinkError: dlopen failed: "/data/data/tools.zhang.com.mytools/lib-main/libmyjni.so" is 32-bit instead of 64-bit
        at java.lang.Runtime.load(Runtime.java:332)
        at java.lang.System.load(System.java:1069)
        at com.facebook.soloader.DirectorySoSource.loadLibraryFrom(DirectorySoSource.java:71)
        at com.facebook.soloader.DirectorySoSource.loadLibrary(DirectorySoSource.java:42)
        at com.facebook.soloader.SoLoader.loadLibraryBySoName(SoLoader.java:299)
        at com.facebook.soloader.SoLoader.loadLibrary(SoLoader.java:247)
        at tools.zhang.com.jnilibrary.NdkUtils.initSoLoader(NdkUtils.java:16)
     */

    public static native String fromJni();

    public native String getNormalJni();
}