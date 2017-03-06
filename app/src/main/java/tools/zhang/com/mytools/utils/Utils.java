package tools.zhang.com.mytools.utils;

import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdecheng on 2016/9/7.
 */
public class Utils {
    private static final String TAG = "MyTools";

    public static List<String> getPidsByProcessName(String processName) {
        Log.e(TAG, "n:" + processName +",p:" + Process.myPid() +",t:" + Thread.currentThread().getName());
        File[] files = new File("/proc").listFiles(sFilter);
        ArrayList<String> pids = new ArrayList<String>();
        if (files != null && files.length >= 1) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    continue;
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(new File(file.getPath(),
                            "cmdline")));
                    Log.e(TAG, "getPidsByProcessName: reader:" + reader);
                    String cmdline = reader.readLine();
                    if (cmdline != null) {
                        cmdline = cmdline.trim();
                    }
                    if (cmdline != null && cmdline.startsWith(processName)) {
                        Log.e(TAG, "cmdline :" + cmdline);
                        pids.add(file.getName());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getPidsByProcessName: erroe", e );
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                            Log.e(TAG, "getPidsByProcessName: reader.close");
                        } catch (Throwable e) {
                            Log.e(TAG, "getPidsByProcessName: reader.close error", e);
                        }
                    }
                }
            }
        }
        if (files != null) {
            Log.e(TAG, "files != null then set files = null");
        }
        Log.e(TAG, "getPidsByProcessName: pids.size:" + pids.size() );
        return pids;
    }

    private static final FilenameFilter sFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return filename.matches("\\d+");
        }
    };
}
