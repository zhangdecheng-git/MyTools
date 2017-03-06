package tools.zhang.com.mytools.MotionEvent;

import android.app.Activity;
import android.os.SystemClock;
import android.view.MotionEvent;

/**
 * 模拟点击
 */
public class MotionEventUtils {
	public void myClickEvent(Activity context, float x, float y) {
        long firstTime = SystemClock.uptimeMillis();
        final MotionEvent firstEvent = MotionEvent.obtain(firstTime, firstTime, MotionEvent.ACTION_DOWN, x, y, 0);

        long secondTime = firstTime + 30;
        final MotionEvent secondEvent = MotionEvent.obtain(secondTime, secondTime, MotionEvent.ACTION_UP, x, y, 0);

        context.dispatchTouchEvent(firstEvent);
        context.dispatchTouchEvent(secondEvent);
    }
}
