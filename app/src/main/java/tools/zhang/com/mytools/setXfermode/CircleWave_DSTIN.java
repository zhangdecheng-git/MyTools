package tools.zhang.com.mytools.setXfermode;

import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2017/2/4.
 */
public class CircleWave_DSTIN extends View {
    private static final int ItemWaveLength = 1000;

    private Paint mPaint;
    private Path mPath;
    private int dx;
    private Bitmap BmpSRC, BmpDST;

    public CircleWave_DSTIN(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        BmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.pressed, null);
        // BmpDST是要显示的波浪线，所以我们新建一个空白图像，用以画即将生成的波浪线
        BmpDST = Bitmap.createBitmap(BmpSRC.getWidth(), BmpSRC.getHeight(), Bitmap.Config.ARGB_8888);

        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        generageWavePath();
        // 先清空bitmap上的图像,然后再画上Path
        Canvas c = new Canvas(BmpDST);
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        c.drawPath(mPath, mPaint);
        //先画上SRC图像来显示完整的文字,因为我们在使用Mode.DST_IN时，除了相交区域以外，其它区域都会因为有空白像素而消失不见了
        canvas.drawBitmap(BmpSRC, 0, 0, mPaint);
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(BmpDST, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(BmpSRC, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    /**
     * 生成此时的Path
     */
    private void generageWavePath() {
        mPath.reset();
        int originY = BmpSRC.getHeight() / 2;
        int halfWaveLen = ItemWaveLength / 2;
        mPath.moveTo(-ItemWaveLength + dx, originY);
        for (int i = -ItemWaveLength; i <= getWidth() + ItemWaveLength; i += ItemWaveLength) {
            mPath.rQuadTo(halfWaveLen / 2, -50, halfWaveLen, 0);
            mPath.rQuadTo(halfWaveLen / 2, 50, halfWaveLen, 0);
        }
        mPath.lineTo(BmpSRC.getWidth(), BmpSRC.getHeight());
        mPath.lineTo(0, BmpSRC.getHeight());
        mPath.close();
    }

    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, ItemWaveLength);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
