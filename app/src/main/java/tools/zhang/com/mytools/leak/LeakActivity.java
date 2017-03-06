package tools.zhang.com.mytools.leak;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/3.
 */
public class LeakActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

//        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
//
//        getWindow().setAttributes(params);

        new MyLeak().testLeak(LeakActivity.this);
    }

    private static class MyLeak {
        private static List<Activity> mActivity = new ArrayList<>();
        private void testLeak(Activity activity) {
            mActivity.add(activity);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Log.d("leak", "test");
                    }
                }
            }).start();
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

}
