package tools.zhang.com.mytools.crash_getStringExtra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/3/6.
 */
public class CrashGetStringMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_getstring_main);
        findViewById(R.id.open_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrashGetStringMainActivity.this, CrashGetStringDetailActivity.class);
                // 接收到使用getStringExtra正常
                intent.putExtra("zhang", "hello world");
                // 传一个对象，在接收端getStringExtra崩溃
                intent.putExtra("zhang", 123);
                CrashGetStringMainActivity.this.startActivity(intent);
            }
        });
    }
}
