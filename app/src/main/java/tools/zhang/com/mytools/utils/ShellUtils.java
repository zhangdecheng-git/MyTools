package tools.zhang.com.mytools.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellUtils {

    private static final String _LD_LIBRARY_PATH = "_LD_LIBRARY_PATH";

    public static final String LD_LIBRARY_PATH = "LD_LIBRARY_PATH";

    private static final boolean DEBUG = true;

    private static final String DEFAULT_CHARSET = "utf-8";

    private static final String TAG = ShellUtils.class.getSimpleName();

    public static String findShShellBin() {
        String pathStr = System.getenv("PATH");
        if (pathStr != null && pathStr.length() > 0) {
            String[] paths = pathStr.split(":");
            for (String path : paths) {
                File file = new File(path, "sh");
                if (file.exists()) {
                    return file.getPath();
                }
            }
        }
        return null;
    }

    public static String execSh(File directory, Map<String, String> envs, String... cmds) {
        String sh = findShShellBin();
        if (sh == null) {
            throw new RuntimeException("The devices(" + Build.MODEL + ") has not shell ");
        }
        return exec(sh, directory, envs, cmds);
    }

    private static void setupEnvs(Map<String, String> envs) {
        if (envs == null) {
            envs = new HashMap<String, String>();
        }

        if (!envs.containsKey(LD_LIBRARY_PATH)) {
            String ldLibPath = getLdLibPath(null);
            envs.put(LD_LIBRARY_PATH, ldLibPath);
            envs.put(_LD_LIBRARY_PATH, ldLibPath);
        } else {
            String oldLd = envs.get(LD_LIBRARY_PATH);
            String ldLibPath = getLdLibPath(oldLd);
            envs.put(LD_LIBRARY_PATH, ldLibPath);
            envs.put(_LD_LIBRARY_PATH, ldLibPath);
        }
    }

    private static String getLdLibPath(String ld) {
        String systemLb = System.getenv(LD_LIBRARY_PATH);
        if (TextUtils.isEmpty(systemLb)) {
            systemLb = System.getenv(_LD_LIBRARY_PATH);
        }
        systemLb += "/vendor/lib:/system/lib:/vendor/lib*:/system/lib*";

        if (!TextUtils.isEmpty(ld)) {
            systemLb += ":" + ld;
        }

        systemLb += ":.";

        List<String> lbList = new ArrayList<String>();
        String[] lbs = systemLb.split(":");
        if (lbs.length > 0) {
            for (String string : lbs) {
                if (!lbList.contains(string)) {
                    lbList.add(string);
                }
            }
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String string : lbList) {
                if (!first) {
                    sb.append(":");
                }
                sb.append(string);
                first = false;
            }
            return sb.toString();
        } else {
            return systemLb;
        }
    }

    public static String exec(String shell, File directory, Map<String, String> envs,
                              String... cmds) {
        Process process = null;
        try {
            if (shell == null) {
                throw new RuntimeException("The devices(" + Build.MODEL + ") has not shell ");
            }
            ProcessBuilder builder = new ProcessBuilder().command(shell).redirectErrorStream(true);
            if (directory != null) {
                builder.directory(directory);
            }

            try {// 2.1 hero这里不支持
                builder.environment().putAll(System.getenv());
                setupEnvs(envs);
                if (envs != null && envs.size() > 0) {
                    builder.environment().putAll(envs);
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "error", e);
                }
            }
            process = builder.start();

            OutputStream stdIn = process.getOutputStream();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), DEFAULT_CHARSET));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    process.getErrorStream(), DEFAULT_CHARSET));

            for (String cmd : cmds) {
                if (!cmd.endsWith("\n")) {
                    cmd += "\n";
                }
                stdIn.write(cmd.getBytes());
                stdIn.flush();
            }
            stdIn.write("exit 156\n".getBytes());
            stdIn.flush();
            stdIn.close();

            String str = readAllStrFromBufferedReader(stdOut);
            if (str != null) {
                return str;
            } else {
                return readAllStrFromBufferedReader(stdError);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AndroidRuntimeException(e);
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                    process.waitFor();
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "error", e);
                    }
                }

            }
        }
    }

    public static int execShP(File directory, Map<String, String> envs,
                              boolean waitForProcessFinished,
                              String... cmds) {
        String sh = findShShellBin();
        if (sh == null) {
            throw new RuntimeException("The devices(" + Build.MODEL + ") has no shell sh");
        }
        return execP(sh, directory, envs, waitForProcessFinished, null, cmds);
    }

    public static int execP(String shell, File directory, Map<String, String> envs,
                            boolean waitForProcessFinished, StringBuffer result, String... cmds) {
        Process process = null;
        try {
            if (shell == null) {
                throw new RuntimeException("The devices(" + Build.MODEL + ") has not shell ");
            }
            ProcessBuilder builder = new ProcessBuilder().command(shell).redirectErrorStream(true);
            if (directory != null) {
                builder.directory(directory);
            }

            try {// 2.1 hero这里不支持
                builder.environment().putAll(System.getenv());
                setupEnvs(envs);
                if (envs != null && envs.size() > 0) {
                    builder.environment().putAll(envs);
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "error", e);
                }
            }
            process = builder.start();

            OutputStream stdIn = process.getOutputStream();
            for (String cmd : cmds) {
                if (!cmd.endsWith("\n")) {
                    cmd += "\n";
                }
                stdIn.write(cmd.getBytes());
                stdIn.flush();
            }

            stdIn.write("exit 0\n".getBytes());
            stdIn.flush();

            if (waitForProcessFinished) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                stdIn.write("exit 0\n".getBytes());
                stdIn.flush();
                stdIn.close();
                int waitFor = process.waitFor();
                if (result != null) {
                    BufferedReader successResult = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));
                    BufferedReader errorResult = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()));
                    String s;
                    result.append("GK--result:");
                    while ((s = successResult.readLine()) != null) {
                        result.append(s);
                    }
                    result.append("\n");
                    result.append("GK--error:");
                    while ((s = errorResult.readLine()) != null) {
                        result.append(s);
                    }
                }
                return waitFor;
            } else {
                // stdIn.flush();
                // stdIn.close();
                return 0;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new AndroidRuntimeException(e);
        } finally {
            if (process != null) {
                if (waitForProcessFinished) {
                    try {
                        process.destroy();
                    } catch (Exception e) {
                        if (DEBUG) {
                            Log.e(TAG, "error", e);
                        }
                    }
                }
            }
        }
    }

    private static String readAllStrFromBufferedReader(BufferedReader br) throws IOException {
        final char[] buf = new char[1024];
        int readed;
        final StringBuilder builder = new StringBuilder();
        while ((readed = br.read(buf)) != -1) {
            builder.append(buf, 0, readed);
        }
        return builder.length() > 0 ? builder.toString() : null;
    }
}
