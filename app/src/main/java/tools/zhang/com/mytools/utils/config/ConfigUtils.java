package tools.zhang.com.mytools.utils.config;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    private volatile static Properties sProperties;
    private static boolean logable = false;
    private static String mOriginChannel = "-1";

    public static boolean isLogable(Context context) {
        return logable ? logable : "t".equalsIgnoreCase(getValueFromAssetsConf(context, "l"));
    }

    public static boolean isUseTestHost(Context context) {
        return "t".equalsIgnoreCase(getValueFromAssetsConf(context, "server"));
    }

    public static boolean isBetaVersion(Context context) {
        return "beta".equalsIgnoreCase(getValueFromAssetsConf(context, "ver"));
    }

    public static String getBuildId(Context context) {
        return getValueFromAssetsConf(context, "build_id");
    }

    public static String getRevision(Context context) {
        return getValueFromAssetsConf(context, "svn");
    }

    private static String getValueFromAssetsConf(Context context, String key) {
        if (sProperties == null) {
            synchronized (ConfigUtils.class) {
                if (sProperties == null) {
                    sProperties = getProperties(context, "conf");
                }
            }
        }
        return sProperties.getProperty(key, "");
    }

    private static Properties getProperties(Context context, String name) {
        InputStream in = null;
        Properties properties = new Properties();
        try {
            in = context.getResources().getAssets().open(name);
            properties.load(in);
        } catch (Exception | Error e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignore) {
            }
        }
        return properties;
    }

}
