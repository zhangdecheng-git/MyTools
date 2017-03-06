package tools.zhang.com.mytools.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/10/28.
 */
public class MyDrawView extends View {


    // 画笔
    private Paint mPaint;
    // 声明路径对象数组
    Path mPath;
    Bitmap mBitmap;


    public MyDrawView(Context context) {
        super(context);
        initView();
    }

    public MyDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        String testStr="12345六七八九十";

        float[] breakNums=new float[1];

        int breakNum=mPaint.breakText(testStr,true,100,breakNums);

        float measureNum=mPaint.measureText(testStr);

        Log.e("velsharoon",breakNum+" "+breakNums[0]+" "+measureNum);


        Path path9 = new Path();
        path9.addCircle(300, 600, 150, Path.Direction.CW);
        path9.addCircle(380, 700, 150, Path.Direction.CW);
//        path9.setFillType(Path.FillType.WINDING);
        path9.setFillType(Path.FillType.EVEN_ODD);
//        path9.setFillType(Path.FillType.INVERSE_WINDING);
//        path9.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path9, mPaint);
    }

    private void initView() {
        mPath = new Path();
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(15);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);


        String gg = "zgg";
        if ("test".equals(gg)) {
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(10);
        } else {
            mPaint.setColor(Color.YELLOW);
            mPaint.setStrokeWidth(5);
        }
    }
}
