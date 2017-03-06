package tools.zhang.com.mytools.process;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import tools.zhang.com.mytools.R;
import tools.zhang.com.mytools.utils.Utils;

/**
 * Created by zhangdecheng on 2016/10/28.
 */
public class ProcessActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_activity);
        findViewById(R.id.process_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private Runnable mRunnable;
    private Handler mThreadHandler;

    private void test() {
        HandlerThread thread = new HandlerThread("test_thread");
        thread.start();
        mThreadHandler = new Handler(thread.getLooper());

        mRunnable = new Runnable() {
            @Override
            public void run() {
                Utils.getPidsByProcessName("tools.zhang.com.mytools");
                mThreadHandler.postDelayed(this, 10*1000);
            }
        };

        mThreadHandler.postDelayed(mRunnable, 1000);
    }
}
