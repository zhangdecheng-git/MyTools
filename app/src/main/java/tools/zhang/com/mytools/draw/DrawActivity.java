package tools.zhang.com.mytools.draw;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/10/28.
 */
public class DrawActivity extends Activity {
    ImageView mShapeImg;
    ImageView mCustomDrawableImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        mShapeImg = (ImageView) findViewById(R.id.shape_circle);
        mCustomDrawableImg = (ImageView) findViewById(R.id.shape_drawable_custom);

        mShapeImg.setBackground(getResources().getDrawable(R.drawable.install_circle));

        MyDrawable myDrawable = new MyDrawable();
        myDrawable.setBorderColor(getResources().getColor(R.color.colorAccent));
        mCustomDrawableImg.setBackground(myDrawable);
    }
}
