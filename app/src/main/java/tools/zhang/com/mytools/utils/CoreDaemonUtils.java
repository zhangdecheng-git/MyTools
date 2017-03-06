package tools.zhang.com.mytools.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

/**
 * Created by gaokuo on 2016/5/25.
 */
public class CoreDaemonUtils {

    public static final String CLASSPATH_PKGNAME_APPSTORE = "PKGNAME_APPSTORE_CLASSPATH";
    private static final String SOCKET_NAME = "LocalSocketName";
    private static final String MY_DATA_DIR = "MY_DATA_DIR";
    private static final String INTERVAL_TIMER = "IntervalTimer";


    private final static String TAG = "MyTools";


    public static final boolean tryStart(Context context) {
        try {
            String processName = getProcessName(context);

            String cmd = String.format(getFileName() + " / %s --nice-name=%s --daemon &", "tools.zhang.com.mytools.CoreDaemon", processName);
            List<String> paths = RootCommandUtils.getAppProcessCommandPaths(context);
            String file = "";
            for (String path : paths) {
                file += (":" + path);
                Log.d(TAG, "path:" + path);
            }
            String libDir = getLibDir(context);
            String cmd1 = "export CLASSPATH=$CLASSPATH" + file;
            String cmd2 = "cd " + context.getFilesDir().getParent();
            String cmd3 = String.format("export %s=%s", CLASSPATH_PKGNAME_APPSTORE, context.getPackageName());
            String cmd4 = String.format("export %s=%s", SOCKET_NAME, processName);
            String cmd5 = String.format("export %s=%s", MY_DATA_DIR, context.getApplicationInfo().dataDir);
            String cmd6 = String.format("export _LD_LIBRARY_PATH=$LD_LIBRARY_PATH:%s", libDir);
            String cmd7 = String.format("export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:%s", libDir);
            String cmd8 = String.format("export %s=%d", INTERVAL_TIMER, 3 * 1000);
            ShellUtils.execShP(new File("/"), null, false, cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7, cmd8, cmd);
            Log.d(TAG, "CoreDaemon run ok，ProcessName：" + processName);
        } catch (Throwable e) {
            Log.e(TAG, "CoreDaemon Error", e);
            return false;
        }
        return true;
    }


    private static String getProcessName(Context context) {
        return String.format("%s_CoreDaemon", context.getPackageName());
    }


    private static final FilenameFilter sFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return filename.matches("\\d+");
        }
    };

    private static final boolean hasRunning(Context context) {
        File[] files = new File("/proc").listFiles(sFilter);
        if (files != null && files.length >= 1) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    continue;
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(new File(file.getPath(), "cmdline")));
                    String cmdline = reader.readLine();
                    if (cmdline != null) {
                        cmdline = cmdline.trim();
                    }
                    if (cmdline != null && cmdline.startsWith(getProcessName(context))) {
                        return true;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable e) {
                        }
                    }
                }
            }
        }

        return false;
    }

    private static String getFileName() {
        String path = "/system/bin/app_process32";
        return new File(path).exists() ? "app_process32" : "app_process";
    }


    private static String getLibDir(Context context) {
        String libDir = "";
        try {
            ApplicationInfo info = context.getApplicationInfo();
            libDir = info.nativeLibraryDir;
//            libDir += (":" +  context.getFilesDir().getParentFile().getAbsolutePath() + "/lib");
        } catch (Exception e) {
            Log.e(TAG, "getLibDir" + libDir);
        }
        Log.e(TAG, "getLibDir" + libDir);
        return libDir;
    }

    public static final void setEnable(boolean enable) {
        File file = new File(Environment.getDataDirectory(), "/data/com.qihoo.appstore/config/usr_unrsh.lock");
        if (enable) {
            if (file.exists()) {
                file.delete();
            }

        } else {
            if (!file.exists()) {
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static final boolean isEnable() {
        return !new File(Environment.getDataDirectory(), "/data/com.qihoo.appstore/config/usr_unrsh.lock").exists();
    }
}
