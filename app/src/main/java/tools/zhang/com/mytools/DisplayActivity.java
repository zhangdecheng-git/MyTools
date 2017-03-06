package tools.zhang.com.mytools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by zhangdecheng on 2016/10/28.
 */
public class DisplayActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        textView = (TextView) findViewById(R.id.text);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private void test() {
        StringBuilder sb = new StringBuilder();
        sb.append("density:").append(getScreenDensity(DisplayActivity.this))
                .append("\nwidth:").append(getScreenDisplaySize(DisplayActivity.this).getWidth())
                .append("\nheight:").append(getScreenDisplaySize(DisplayActivity.this).getHeight());
        textView.setText(sb.toString());
    }

    public float getScreenDensity(Context ctx) {
        DisplayMetrics dsm = new DisplayMetrics();
        ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dsm);
        return dsm.density;
    }

    public Display getScreenDisplaySize(Context ctx) {
        return ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

}
