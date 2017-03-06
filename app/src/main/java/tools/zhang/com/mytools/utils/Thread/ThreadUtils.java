package tools.zhang.com.mytools.utils.Thread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by fengda on 2015/8/10.
 * 使用Asynctask的线程池进行后台操作，特别耗时操作如下载请自己开线程管理，以防止多个特别耗时操作把Asynctask的线程阻塞掉
 */
public class ThreadUtils {

    public static long getCurThreadId() {
        return Thread.currentThread().getId();
    }

    public static long getMainThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }

    public static String getCurThreadName() {
        String strThreadName;
        strThreadName = Thread.currentThread().getName();
        if (strThreadName == null) {
            strThreadName = "";
        }
        return strThreadName;
    }

    public static class RunnableCallback implements Cancelable{
        private AsyncTask mTask;

        private RunnableCallback(AsyncTask task) {
            mTask = task;
        }

        @Override
        public void cancel() {
            if (mTask != null) {
                mTask.cancel(true);
            }
        }
    }

    public static RunnableCallback postRunnable(final Runnable runnable) {
        if (runnable != null) {
            AsyncTask<?, ?, ?> task = new AsyncTask<Void, Void, Void>() {
                //用于Asynctask通过该引用获取调用类
                public Runnable mPostRunnable = runnable;

                @Override
                protected Void doInBackground(Void... params) {
                    runnable.run();
                    return null;
                }
            }.execute();
            return new RunnableCallback(task);
        }
        return null;
    }

    public static void setThreadName(String threadName) {
        Thread.currentThread().setName(threadName);
    }

    // 参数 timerOut： 单位是秒
    // 参看 TransitService 用法
    // countDownLatch 的锁技术必须是1.即使，mCountDownLatch = new CountDownLatch(1);
    public static void execRunnableOnThread(Handler handler, final CountDownLatch countDownLatch, final Runnable runnable) {
        execRunnableOnThread(handler, countDownLatch, false, 0, runnable);
    }

    public static void execRunnableOnThread(Handler handler, final CountDownLatch countDownLatch, long timerOutSecond, final Runnable runnable) {
        execRunnableOnThread(handler, countDownLatch, true, timerOutSecond, runnable);
    }

    private static void execRunnableOnThread(Handler handler, final CountDownLatch countDownLatch, boolean needTimerOut, long timerOutSecond, final Runnable runnable) {
        if (getCurThreadId() == handler.getLooper().getThread().getId()) {
            runnable.run();
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    countDownLatch.countDown();
                }
            }
        });
        try {
            if (needTimerOut) {
                countDownLatch.await(timerOutSecond, TimeUnit.SECONDS);
            } else {
                countDownLatch.await();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在UI线程执行Runnable
     *
     * @param action
     */
    public static void runOnUiThread(Runnable action) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            action.run();
        } else {
            new Handler(Looper.getMainLooper()).post(action);
        }
    }

    /**
     * 在UI线程执行Runnable
     *
     * @param action
     */
    public static void postOnUiThread(Runnable action, long delayTime) {
        new Handler(Looper.getMainLooper()).postDelayed(action, delayTime);
    }


    public static int getThreadCount() {
        return Thread.getAllStackTraces().size();
    }

    public static boolean isMainThread() {
        return getCurThreadId() == getMainThreadId();
    }

    public interface Cancelable {
        void  cancel();
    }

}