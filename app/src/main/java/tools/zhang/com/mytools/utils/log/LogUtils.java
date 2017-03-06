package tools.zhang.com.mytools.utils.log;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import tools.zhang.com.mytools.utils.DeviceUtils;
import tools.zhang.com.mytools.utils.Thread.PriorityThreadFactory;
import tools.zhang.com.mytools.utils.Thread.ThreadUtils;
import tools.zhang.com.mytools.utils.config.ConfigUtils;
import tools.zhang.com.mytools.utils.file.FileUtils;
import tools.zhang.com.mytools.utils.process.ProcessUtils;
import tools.zhang.com.mytools.utils.sdcard.SDCardUtils;

public class LogUtils {
    public static final String TAG = "LogUtils";

    private static final long LOGABLE_DELAY = 2000;

    public static File LogPath;
    private static final String FORMAT_STR = "%S[%s] %s\n";

    private static AtomicBoolean isDebug = new AtomicBoolean(true);
    private static AtomicBoolean isWriteLogFile = new AtomicBoolean(false);
    private static AtomicBoolean initilized = new AtomicBoolean(false);

    public static AtomicBoolean canLog = new AtomicBoolean(false);//是不是可以显示日志，如果不显示，就不要构造log的message了，浪费内存

    private static ThreadPoolExecutor mExecutor;

    public static long START_TIME = SystemClock.elapsedRealtime(); // 返回系统启动到现在的毫秒数，包含休眠时间

//    public static boolean isDebug() {
//        return isDebug.get();
//    }

    public static boolean isWriteLogFile() {
        return isWriteLogFile.get();
    }

    public static String getLogPath() {
        return Environment.getExternalStorageDirectory() + "/360Log";
    }

    // debug 表示是否输出ddms日志。
    // loable 表示是否写日志。
    public static void initInAppProcess(boolean debug, boolean logable, String logFile) {
        isDebug.set(debug);
        isWriteLogFile.set(logable);
    }

    public static void init(final Context context) {
        /*延迟初始化*/
        LogPath = new File(getLogPath() + "/appstore_log.txt");

        if (getDynamicLogable(context)) {
            isWriteLogFile.set(true);//这个快关特别耗内存  手动打开，不再自动打开
            canLog.set(isWriteLogFile.get() || isDebug.get());
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isWriteLogFile.set(ConfigUtils.isLogable(context));
                    canLog.set(isWriteLogFile.get() || isDebug.get());
                    initilized.set(true);
                }
            }, LOGABLE_DELAY);
        }

        isDebug.set(DeviceUtils.isDebuggable());
        canLog.set(isWriteLogFile.get() || isDebug.get());
        if (FileUtils.getFileLen(LogPath.getAbsolutePath()) > 50 * 1024 * 1024) {
            FileUtils.deleteFile(LogPath.getAbsolutePath());
        }
        
//        initilized.set(true);
//        isDebug.set(true);
//        canLog.set(true);
//        isWriteLogFile.set(true);
    }

    public static void delOldLogfiles() {
        String sdCard = SDCardUtils.getSDCardPath();
        if (!TextUtils.isEmpty(sdCard)) {
            FileUtils.deleteDirectory(sdCard + "/" + "360log");
        }
    }

    public static boolean isDebug() {
        return canLog.get();
    }

    /**
     * 动态设置是否输出日志
     */
    public static void setDynamicLogable(Context context, boolean logable) {
        isWriteLogFile.set(logable);
        canLog.set(isWriteLogFile.get() || isDebug.get());
        File logEnableFile = new File(context.getCacheDir(), "log.enable"); // 支持多进程判断；
        if (logable) {
            if (!logEnableFile.exists()) {
                try {
                    logEnableFile.createNewFile();
                } catch (IOException e) {
                }
            }
        } else {
            logEnableFile.delete();
        }

    }

    public static boolean getDynamicLogable(Context context) {
        File logEnableFile = new File(context.getCacheDir(), "log.enable");
        return logEnableFile.exists();
    }

    public static int v(String tag, String msgString) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, null);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.v(tag, "" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int v(String tag, String msgString, Throwable tr) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, tr);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }
            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.v(tag, "" + msg, tr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int d(String tag, String msgString) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, null);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.d(tag, "" + msg);
            }


        } catch (Exception e) {
        }
        return 0;
    }

    public static int d(String tag, String msgString, Throwable tr) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, tr);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }
            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.d(tag, "" + msg, tr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int i(String tag, String msgString) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, null);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.i(tag, msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int i(String tag, String msgString, Throwable tr) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, tr);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.i(tag, "" + msg, tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int w(String tag, String msgString) {

        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, null);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.w(tag, "" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int w(String tag, String msgString, Throwable tr) {

        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, tr);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.w(tag, "" + msg, tr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int e(String tag, String msgString) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, null);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.e(tag, "" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int e(String tag, String msgString, Throwable tr) {
        try {
            String msg = null;
            if (isWriteLogFile.get()) {
                LogData data = writeToFile(tag, msgString, Log.INFO, tr);
                if (data.hasLogout) {
                    return data.logResult;
                } else {
                    msg = data.msg;
                }
            }

            if (isDebug.get()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = buildMessage(msgString);
                }
                return Log.e(tag, "" + msg, tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static LogData writeToFile(String tag, String msgString, int priority, Throwable tr) {
        String msg;
        LogData data = new LogData();
        if (initilized.get()) {
            if (isWriteLogFile.get()) {
                msg = buildMessage(msgString);
                if (tr != null) {
                    writeFile(tag, msg, tr);
                    data.logResult = Log.v(tag, "" + msg, tr);
                } else {
                    writeFile(tag, msg);
                    data.logResult = Log.v(tag, "" + msg);
                }
                data.hasLogout = true;
                data.msg = msg;
            }
        } else {
            msg = buildMessage(msgString);
            data.msg = msg;
        }
        return data;
    }

    public static void writeFile(final String tag, final String msg) {
        writeFile(LogPath, tag, msg, null);
    }

    private static void writeFile(final String tag, final String msg, final Throwable tr) {
        writeFile(LogPath, tag, msg, tr);
    }

    public synchronized static void writeFile(final File file, final String tag, final String msg, final Throwable tr) {
        if (mExecutor == null) {
            synchronized (TAG) {
                if (mExecutor == null) {
                    mExecutor = new ThreadPoolExecutor(1, 1,
                            5 * 1000L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new PriorityThreadFactory(TAG, Process.THREAD_PRIORITY_LOWEST));
                    mExecutor.allowCoreThreadTimeOut(true);
                }
            }
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                writeFileSync(file, tag, msg, tr);
            }
        });
    }

    private static void writeFileSync(File file, String tag, String msg, Throwable tr) {
        PrintStream outputStream = null;
        try {
            FileUtils.makeDir(file.getParentFile().getPath());

            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new PrintStream(new FileOutputStream(file, true));
            outputStream.printf(FORMAT_STR, getSystemTime(), tag, msg);
            if (tr != null)
                tr.printStackTrace(outputStream);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String msg) {
//        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
//        String caller = "<unknown>";
//        // Walk up the stack looking for the first caller outside of LogUtils.
//        // It will be at least two frames up, so start there.
//        for (int i = 2; i < trace.length; i++) {
//            Class<?> clazz = trace[i].getClass();
//            if (!clazz.equals(LogUtils.class)) {
//                String callingClass = trace[i].getClassName();
//                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
//                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
//
//                caller = callingClass + "." + trace[i].getMethodName();
//                break;
//            }
//        }
        return Process.myPid() + " " + Thread.currentThread().getId() + ": " + msg;
    }

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);

    private static String getSystemTime() {
        String str = null;
        try {

            Date curDate = new Date(System.currentTimeMillis());
            str = formatter.format(curDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 显示Intent中的所有数据
     */
    public static String intentToString(Intent intent) {
        if (intent != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Intent { ");
            String action = intent.getAction();
            if (action != null) {
                sb.append("act = ").append(action).append(",");
            }
            Set<String> categories = intent.getCategories();
            if (categories != null) {
                sb.append(" cat = [");
                Iterator<String> i = categories.iterator();
                boolean done = false;
                while (i.hasNext()) {
                    if (done) {
                        sb.append(", ");
                    }
                    done = true;
                    sb.append(i.next());
                }
                sb.append("]");
            }
            Uri uri = intent.getData();
            if (uri != null) {
                sb.append(" dat = ").append(uri).append(",");
            }
            String type = intent.getType();
            if (type != null) {
                sb.append(" typ = ").append(type).append(",");
            }
            int flags = intent.getFlags();
            if (flags != 0) {
                sb.append(" flg = 0x").append(Integer.toHexString(flags)).append(",");
            }
            String packageStr = intent.getPackage();
            if (packageStr != null) {
                sb.append(" pkg = ").append(packageStr).append(",");
            }
            ComponentName component = intent.getComponent();
            if (component != null) {
                sb.append(" cmp = ").append(component.flattenToShortString()).append(",");
            }
            Rect rect = intent.getSourceBounds();
            if (rect != null) {
                sb.append(" bnds = ").append(rect.toShortString()).append(",");
            }
            Bundle extras = intent.getExtras();
            if (extras != null) {
                sb.append(" extras = [");
                int i = 0;
                try {
                    for (String key : extras.keySet()) {
                        sb.append(key).append(" = ");
                        Object obj = extras.get(key);
                        if (obj != null && obj instanceof Bundle) {
                            sb.append(" [extras2 = [");
                            int j = 0;
                            Bundle extras2 = (Bundle) obj;
                            for (String key2 : extras2.keySet()) {
                                Object obj2 = extras2.get(key2);
                                sb.append(key2).append(" = ").append(obj2 instanceof byte[] ? new String((byte[]) obj2) : obj2);
                                if (++j <= extras2.size() - 1) {
                                    sb.append(", ");
                                }
                            }
                            sb.append("] ]");
                        } else {
                            sb.append(obj instanceof byte[] ? new String((byte[]) obj) : obj);
                        }
                        if (++i <= extras.size() - 1) {
                            sb.append(", ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append("]");
            }
            sb.append(" }");
            return sb.toString();
        }
        return null;
    }

    public static String getThreadInfo() {
        return ThreadUtils.getCurThreadId() + " " + ThreadUtils.getCurThreadName() + " " + Process.myPid() + " " + ProcessUtils.getCurrentProcessName();
    }

    public static void safeCheck(boolean bval) {
        if (!bval && LogUtils.isDebug()) {
            LogUtils.e("appstore error", "safeCheck ", new RuntimeException());
//            throw new RuntimeException("com.qihoo.appstore assert " + getThreadInfo());
        }
    }

    public static void safeCheck(boolean bval, String log) {
        if (!bval && LogUtils.isDebug()) {
            LogUtils.e("appstore error", "safeCheck ", new RuntimeException(log));
//            throw new RuntimeException("com.qihoo.appstore assert " + getThreadInfo() + " " + log);
        }
    }

    public static void safeCheckUIThread(String log) {
        safeCheckThread(ThreadUtils.getMainThreadId(), log);
    }

    public static void safeCheckNotUIThread(String log) {
        if (LogUtils.isDebug() && ThreadUtils.getCurThreadId() == ThreadUtils.getMainThreadId()) {
            LogUtils.e("appstore error", "safeCheckNotUIThread ", new RuntimeException(log));
            throw new RuntimeException("com.qihoo.appstore assert " + getThreadInfo() + " " + log);
        }
    }

    public static void safeCheckStatParam(boolean bval, String url) {
        if (!bval && LogUtils.isDebug()) {
            throw new RuntimeException("com.qihoo.appstore stat url curpage == null, url =" + url);
        }
    }


    public static void safeCheckThread(long targetThread, String log) {
        if (LogUtils.isDebug() && ThreadUtils.getCurThreadId() != targetThread) {
            LogUtils.e("appstore error", "safeCheckThread ", new RuntimeException(log));
            throw new RuntimeException("com.qihoo.appstore assert " + getThreadInfo() + " " + log);
        }
    }

    private static class LogData {
        boolean hasLogout;
        int logResult;
        String msg;
    }

    private static int isTestCrashFlag = -1;

    public static boolean isTestCrash() {
        if (!LogUtils.isDebug()) {
            return false;
        }
        if (isTestCrashFlag == -1) {
            isTestCrashFlag = 0;
            if (FileUtils.pathFileExist(SDCardUtils.getSDCardPath() + "/test_crash.txt")) {
                isTestCrashFlag = 1;
            }
        }

        boolean bRet = isTestCrashFlag == 1;

        LogUtils.d(TAG, "isTestCrash " + bRet);

        return bRet;
    }

    public static String getStackTrace(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String caller = "<unknown>";
        for (int i = 0; i < trace.length; i++) {
            String callingClass = trace[i].getClassName();
            callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
            callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

            caller += ((callingClass + "." + trace[i].getMethodName()) + "\n");
        }
        return String.format("[%d] %s(%s): %s",
                Thread.currentThread().getId(), caller, System.currentTimeMillis(), msg);
    }

    public static long getRunningTime() {
        return SystemClock.elapsedRealtime() - START_TIME;
    }

}
