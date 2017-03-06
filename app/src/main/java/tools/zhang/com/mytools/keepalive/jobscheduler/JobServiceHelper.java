package tools.zhang.com.mytools.keepalive.jobscheduler;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Created by zhangdecheng on 2017/1/3.
 */
public class JobServiceHelper {
    private static final String TAG = "JobServiceHelper";

    private static final long INTERVEL = 10 * 1000;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startJob(int jobId, Context context, String clsName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            // 错误时输出日志
            Log.d(TAG, "JobServiceHelper.schedule() jobScheduler is null");
            return;
        }

        Log.e(TAG, "startJob: jobid:" + jobId);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, new ComponentName(context.getPackageName(), clsName));
        builder.setPeriodic(INTERVEL);
        builder.setPersisted(true);
        int result = jobScheduler.schedule(builder.build());
        if (result <= 0) {
            Log.d(TAG, "JobServiceHelper.schedule() jobScheduler.schedule result <= 0");
        }
    }
}
