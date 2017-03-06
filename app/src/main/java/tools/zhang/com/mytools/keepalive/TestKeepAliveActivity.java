package tools.zhang.com.mytools.keepalive;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.keepalive.jobscheduler.JobServiceHelper;
import tools.zhang.com.mytools.keepalive.jobscheduler.MyJobService;
import tools.zhang.com.mytools.utils.process.ProcessUtils;

/**
 * Created by zhangdecheng on 2017/1/3.
 */
public class TestKeepAliveActivity extends Activity {

    private static final String TAG = "TestKeepAliveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: onCreate,processname:" + ProcessUtils.getCurrentProcessName());
        setContentView(R.layout.activity_keepalive);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobServiceHelper.startJob(111, TestKeepAliveActivity.this, MyJobService.class.getName());
            }
        });
    }
}
