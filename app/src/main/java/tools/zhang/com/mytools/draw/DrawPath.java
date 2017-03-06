package tools.zhang.com.mytools.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import tools.zhang.com.mytools.R;

/**
 * http://www.voidcn.com/blog/mockingbirds/article/p-4866673.html
 * Created by zhangdecheng on 2016/10/28.
 */
public class DrawPath extends View implements Runnable, ViewTreeObserver.OnPreDrawListener {
    private static final String TAG = "DrawPath";

    public DrawPath(Context context) {
        super(context);
        initView();
    }

    public DrawPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPath(canvas);
        postDelayed(this, 200);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mAlterBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mAlterBitmap);
    }

    @Override
    public void run() {
        mStartY += 10;
        if (mStartY <= 600) {
            postInvalidate();
        }
        mStartX  = (int) (Math.random() * 20);
        if (mStartX > 40) {
            mStartX = 10;
        }
    }

    private Canvas mCanvas;
    private Bitmap mAlterBitmap;
    private Bitmap mBitmap;
    // 画笔
    private Paint mPaint;
    private Paint mCirclePaint;
    // 声明路径对象数组
    Path mPath;
    private int mStartX = 10;
    private int mStartY = 300;
    private int mSinMax = 20;

    private void drawPath(Canvas canvas) {
//        mPath.reset();
//        mPath.moveTo(mStartX,100);
//        for (int i = 0; i < 4; i++) {
//            mPath.rQuadTo(20, mSinMax, 40, 0);
//            mPath.rQuadTo(20, -mSinMax, 40, 0);
//        }
//        canvas.drawPath(mPath, mPaint);

//        mPath.reset();
//        mPath.moveTo(450,100); //矩形右上角
//        mPath.lineTo(450, 300); //矩形右下角
//        mPath.lineTo(mStartY,300); //矩形左下角
//        mPath.lineTo(mStartY, 100); //矩形左上角
//        for (int i = 0; i < 5; i++) {
//            mPath.rQuadTo(20, mSinMax, 40, 0);
//            mPath.rQuadTo(20, -mSinMax, 40, 0);
//        }
//        canvas.drawPath(mPath, mPaint);


        mCanvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,mCirclePaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 只显示src，且是src和dst相交的部分
        mPath.reset();
        mPath.moveTo(getWidth(), getWidth() - mStartY); //矩形右上角
        mPath.lineTo(getWidth(), getWidth()); //矩形右下角
        mPath.lineTo(0,getWidth()); //矩形左下角
        mPath.lineTo(0, getWidth() - mStartY); //矩形左上角
        for (int i = 0; i < 6; i++) {
            mPath.rQuadTo(mStartX, mSinMax, 40, 0);
            mPath.rQuadTo(mStartX, -mSinMax, 40, 0);
        }
        mCanvas.drawPath(mPath, mPaint);

//        mCanvas.drawRect(0, 500, getWidth(), getWidth() , mPaint);

//        mPaint.setXfermode(null);
        canvas.drawBitmap(mAlterBitmap, 0, 0, null);

    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hugh);
        mPath = new Path();
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setColor(Color.YELLOW);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onPreDraw() {
        return false;
    }
}
