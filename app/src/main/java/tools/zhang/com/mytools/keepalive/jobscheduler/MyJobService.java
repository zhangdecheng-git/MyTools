package tools.zhang.com.mytools.keepalive.jobscheduler;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

/**
 * Created by zhangdecheng on 2017/1/3.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "MyJobService onCreate: ");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "MyJobService onStartJob: ");
//        JobServiceHelper.startJob(params.getJobId(), this, MyJobService.class.getName());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "MyJobService onStopJob: ");
        return false;
    }
}
