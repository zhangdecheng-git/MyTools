package tools.zhang.com.mytools.screen;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/11/8.
 */
public class ScreenActivity extends Activity {
    private Context context;
    private ImageView mImgView;
    private Button mBtn;
    private boolean portrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        context = this;
        mImgView = (ImageView) findViewById(R.id.imageView);
        mBtn = (Button) findViewById(R.id.screen_btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ScreenHelper.getScreenOrientation((Activity) context) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {// 转小屏
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    portrait = true;
                } else {
                    // 全屏
                    ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    portrait = false;
                }
            }
        });
    }


}
