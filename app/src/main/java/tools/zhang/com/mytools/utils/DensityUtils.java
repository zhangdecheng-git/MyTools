package tools.zhang.com.mytools.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zhangdecheng on 2016/11/9.
 */
public class DensityUtils {
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
