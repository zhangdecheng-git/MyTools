package tools.zhang.com.mytools.crash_getStringExtra;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/3/6.
 */
public class CrashGetStringDetailActivity extends Activity {
    private static final String TAG = "zhang";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_getstring_detail);
        String value="";
        value = getIntent().getStringExtra("zhang");
        try {

        } catch (Exception e) {
            Log.e(TAG, "ee", e);
        }
        Log.e(TAG, "onCreate: value:"+value );
        TextView detail = (TextView) findViewById(R.id.detail_text);
        detail.setText(value);
    }
}
