package tools.zhang.com.mytools.aaa;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;

import tools.zhang.com.jnilibrary.NdkUtils;
import tools.zhang.com.mytools.crash.CrashHandler;
import tools.zhang.com.mytools.utils.context.ContextUtils;

/**
 * Created by zhangdecheng on 2016/11/3.
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ContextUtils.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NdkUtils.initSoLoader(this);
        LeakCanary.install(this);//内存泄漏
//        JobServiceHelper.startJob(111, this, MyJobService.class.getName());
        checkAnr();
        // 捕获崩溃初始化（在Activity或者Application中注册一下即可）
        CrashHandler.getInstance().init(getApplicationContext());
    }

    private void checkAnr() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyDialog() //弹出违规提示对话框
                .penaltyLog() //在Logcat 中打印违规异常信息
                .penaltyFlashScreen() //API等级11
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects() //API等级11
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
