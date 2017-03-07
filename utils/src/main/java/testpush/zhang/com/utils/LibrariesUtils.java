package testpush.zhang.com.utils;

/**
 * Created by zhangdecheng on 2016/1/13.
 */
public class LibrariesUtils {

    // 可以在app中直接用；但不能在volley中直接用，需要反射调用
    public static String getLibraryString(String param) {
        return "Utils:getLibraryString:::::param->" + param;
    }
}
