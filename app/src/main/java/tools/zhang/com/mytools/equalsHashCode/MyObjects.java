package tools.zhang.com.mytools.equalsHashCode;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created by zhangdecheng on 2017/2/21.
 */
public class MyObjects {

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }
}
