package tools.zhang.com.mytools.setXfermode;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/2/3.
 */
public class MyView extends View {
    private Paint mPaint;
    private Bitmap mBmp;
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mBmp = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width  = 500;
        int height = width * mBmp.getHeight()/mBmp.getWidth();
        mPaint.setColor(Color.RED);
        int layerID = canvas.saveLayer(0,0,width,height,mPaint,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBmp,null,new Rect(0,0,width,height),mPaint);
        mPaint.setXfermode(new AvoidXfermode(Color.WHITE,100, AvoidXfermode.Mode.TARGET));
        // 方式一：
        canvas.drawRect(0,0,width,height,mPaint);
        // 方式二：
//        mPaint.setARGB(0x00,0xff,0xff,0xff); // 设置透明
//        canvas.drawRect(0,0,width,height,mPaint);
        // 方式三：
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dog_dot_small),null,new Rect(0,0,width,height),mPaint);
        canvas.restoreToCount(layerID);
    }
}
