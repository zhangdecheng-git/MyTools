package tools.zhang.com.mytools.threadpool;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by zhangdecheng on 2017/3/4.
 */
public class ScheduledUtils {
    private static final String TAG = "ScheduledUtils";

    public static void testScheduled() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        // 没隔2s执行一次（如果上一次没有执行完，需等待执行完，再立马执行下次）
        // scheduledThreadPool.scheduleAtFixedRate(new MyRunnable(), 0, 2*1000, TimeUnit.MILLISECONDS);

        // 是线程停止执行到下一次开始执行之间的延迟时间为2s
        // scheduledThreadPool.scheduleWithFixedDelay(new MyRunnable(), 0, 2*1000, TimeUnit.MILLISECONDS);
    }

    private static class MyRunnable implements Runnable {

        @Override
        public void run() {
            SystemClock.sleep(5*1000);
            Log.e(TAG, "thread:"+Thread.currentThread().getName()+" , runnable end");
        }
    }
}
