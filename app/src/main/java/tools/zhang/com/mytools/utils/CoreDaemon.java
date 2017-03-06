package tools.zhang.com.mytools.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 这是一个系统进程的api，运行时不需要root权限，此进程为守护进程。
 * 此进程同时用于弹卸载调查
 * <p>
 *
 */
public class CoreDaemon {

    public static final String CLASSPATH_PKGNAME_APPSTORE = "PKGNAME_APPSTORE_CLASSPATH";
    private static final String ACTION_DAEMON_CORE_SERVICE = "com.qihoo.appstore.ACTION_DAEMON_CORE_SERVICE";
    private static final String ACTION_RT_EXTRA_PID = "com.qihoo.appstore.rtservice.EXTRA_PID";
    private static final String SOCKET_NAME = "LocalSocketName";
    private static final String MY_DATA_DIR = "MY_DATA_DIR";
    private static final String INTERVAL_TIMER = "IntervalTimer";

    private Handler mThreadHandler;
    private HandlerThread mHandlerThread;
    private int mIntervalMillis = 1000 * 10;

    private final static boolean sDebug = true;


    private final static String TAG = "MyTools";

    public static final String START_TYPE = "startType";

    public static final int START_TYPE_FROM_CORE_DAEMON = 20;

    private CoreDaemon() {
    }

    public void setInterval(int interval){
        mIntervalMillis = interval;
        mIntervalMillis = 10 * 1000;
    }

    private static boolean isAppStoreInstalled() {
        String pkgNname = System.getenv(CLASSPATH_PKGNAME_APPSTORE);
        if (!TextUtils.isEmpty(pkgNname)) {
            File path = new File(Environment.getDataDirectory(), "data/" + pkgNname);
            return !path.exists();
        }
        return false;
    }


    private final Runnable mKeepDaemonRunning = new Runnable() {
        @Override
        public void run() {
            if (mThreadHandler != null) {
                mThreadHandler.postDelayed(this, mIntervalMillis);
            }
            checkAndRestartPushService();
        }

        private void checkAndRestartPushService() {
            boolean  isDaemonRunning = isDaemonRunning();
//            if (enable && !isDaemonRunning) {
//                final String cmd, cmd2, cmd3;
//                if (Build.VERSION.SDK_INT >= 17) {
//                    cmd = String.format("am broadcast --user 0 -a %s --ei %s %s --ei %s %s &", ACTION_DAEMON_CORE_SERVICE,
//                            ACTION_RT_EXTRA_PID, Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                    cmd2 = String.format("am startservice --user 0 -a %s --ei %s %s --ei %s %s &", ACTION_DAEMON_CORE_SERVICE,
//                            ACTION_RT_EXTRA_PID, Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                    cmd3 = String.format("am startservice --user 0 -n %s --ei %s %s --ei %s %s &", "com.qihoo.appstore/com.qihoo.core.CoreService",
//                            ACTION_RT_EXTRA_PID, Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                } else {
//                    cmd = String.format("am broadcast -a %s --ei %s %s --ei %s %s &", ACTION_DAEMON_CORE_SERVICE, ACTION_RT_EXTRA_PID,
//                            Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                    cmd2 = String.format("am startservice -a %s --ei %s %s --ei %s %s &", ACTION_DAEMON_CORE_SERVICE, ACTION_RT_EXTRA_PID,
//                            Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                    cmd3 = String.format("am startservice -n %s --ei %s %s --ei %s %s &", "com.qihoo.appstore/com.qihoo.core.CoreService",
//                            ACTION_RT_EXTRA_PID, Process.myPid(), START_TYPE, START_TYPE_FROM_CORE_DAEMON);
//                }
//
//                try {
//                    ShellUtils.execShP(new File("/"), null, false, cmd);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    ShellUtils.execShP(new File("/"), null, false, cmd2);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    ShellUtils.execShP(new File("/"), null, false, cmd3);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (sDebug) {
//                    Log.e(TAG, "Start Daemon in coredaemon");
//                }
//            } else {
//                if (sDebug) {
//                    Log.e(TAG, "not Start Daemon in coredaemon");
//                }
//            }
        }

        // 判断CoreService是否在运行状态；
        private boolean isDaemonRunning() {
            List<String> pids = Utils.getPidsByProcessName("com.qihoo.daemon");
            Log.e(TAG, "isDaemonRunning  pids size:" + pids.size());
            boolean isDeamonRunning = pids.size()>0;
            pids.clear();
            System.gc();
            return isDeamonRunning;
        }
    };


    private void systemReady() {
        String pkg = System.getenv(CLASSPATH_PKGNAME_APPSTORE);
        setProcessName(getProcessName());
        mHandlerThread = new HandlerThread("CoreDaemon");
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper());
        mThreadHandler.postDelayed(mKeepDaemonRunning, mIntervalMillis);
        Looper.prepare();
        Looper.loop();
    }

    private static void setProcessName(String name) {
        try {
            Class<Process> clazz = Process.class;
            Method method = clazz.getMethod("setArgV0", String.class);
            method.invoke(null, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Log.e(TAG, "main: args:" + args.length +" , member:" + args.toString() );
            CoreDaemon coreDaemon = new CoreDaemon();
            coreDaemon.setInterval(10 * 1000);
            coreDaemon.systemReady();
        } catch (Exception e) {
            if (sDebug) {
                Log.e(TAG, "CoreDaemon Error", e);
            }
        } finally {
        }
    }

    private static String getMyPackageName() {
        return System.getenv(CLASSPATH_PKGNAME_APPSTORE);
    }

    private static String getProcessName() {
        String pkg = getMyPackageName();
        Log.e(TAG, "getProcessName " + String.format("%s_CoreDaemon", pkg));
        return String.format("%s_CoreDaemon", pkg);
    }

    private static String getProcessName(Context context) {
        return String.format("%s_CoreDaemon", context.getPackageName());
    }


    public static boolean enableStartDaemonProcess() {
        return isKeepPushInRTService() && CoreDaemon.isEnable();
    }

    private static boolean isKeepPushInRTService() {
        return !new File(Environment.getDataDirectory(), "/data/com.qihoo.appstore/config/unrsh.lock").exists();
    }

    public static final boolean isEnable() {
        return !new File(Environment.getDataDirectory(), "/data/com.qihoo.appstore/config/usr_unrsh.lock").exists();
    }

}