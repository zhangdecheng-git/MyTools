package tools.zhang.com.mytools.video;

import android.content.Context;
import android.widget.TextView;

import java.util.Locale;

/**
 * 工具类
 * Created by CZY on 2017/1/31.
 */
public class Utils {

    /**
     * 根据手机的分辨率从dp单位转成为px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px单位转成为dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置时间格式
     *
     * @param tv          TextView
     * @param millisecond 毫秒数
     */
    public static void updateTimeFormat(TextView tv, int millisecond) {
        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        tv.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", hh, mm, ss));
    }

}
