package tools.zhang.com.mytools.baseadapter.recycleview.common;

import android.app.Activity;
import android.os.Bundle;

import tools.zhang.com.mytools.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/51118836
 *
 * Created by zhangdecheng on 2016/11/11.
 */
public class CommonAdapterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_common);
    }
}
