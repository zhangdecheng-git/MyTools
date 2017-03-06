package tools.zhang.com.mytools.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaokuo on 2016/4/19.
 */
public class RootCommandUtils {
    /**
     * 使用了加固宝和未使用加固宝的获取方法不一样
     *
     * @return
     */
    public static List<String> getAppProcessCommandPaths(Context context) {
        List<String> paths = new ArrayList<>();
        paths.addAll(MultiDexUtils.getDexpath(context.getApplicationContext()));
        return paths;
    }
}
