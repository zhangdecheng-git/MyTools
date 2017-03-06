package tools.zhang.com.mytools.utils.keyboard;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘的显示、隐藏.
 *
 * Created by zhangdecheng on 2015/8/25.
 */
public class SoftInputUtils {
    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
