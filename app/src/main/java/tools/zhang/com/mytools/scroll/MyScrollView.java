package tools.zhang.com.mytools.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by zhangdecheng on 2016/12/6.
 */
public class MyScrollView extends View {
    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    int lastX = 0;
    int lastY = 0;
    Scroller scroller;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                moveView(offsetX, offsetY);
                break;

            case MotionEvent.ACTION_UP:
                //因为下面要使用父视图的引用来得到偏移量 所以要获得一个父视图引用
                View viewGroup = (View) getParent();

                //调用 startScroll 方法，参数为 起始X坐标，起始Y坐标，目的X坐标，目的Y坐标，过度动画持续时间
                //这里使用了 viewGroup.getScrollX() 和 viewGroup.getScrollY() 作为起始坐标，ScrollY 和 ScrollX 记录了使用 scrollBy 进行偏移的量
                //所以使用他们就等于是使用了现在的坐标作为起始坐标，目的坐标为他们的负数，就是偏移量为0的位置，也是view在没有移动之前的位置
                scroller.startScroll(viewGroup.getScrollX(),
                        viewGroup.getScrollY(),
                        -viewGroup.getScrollX(),
                        -viewGroup.getScrollY(),
                        800);

                //刷新view，这里很重要，如果不执行，下面的 computeScroll 方法就不会执行 computeScroll 方法是由 onDraw 方法调用的，而刷新 View 会调用 onDraw。
                invalidate();
                break;
        }
        return true;
    }

    private void moveView(int offsetX, int offsetY) {
        // 1
//        ((View) getParent()).scrollBy(-offsetX, -offsetY);
        // 2
//        layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX,    getBottom() + offsetY);
        // 3
//        offsetLeftAndRight(offsetX);
//        offsetTopAndBottom(offsetY);
        // 4
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.leftMargin =getLeft()+offsetX;
        layoutParams.topMargin = getTop()+ offsetY;
        setLayoutParams(layoutParams);
    }

    @Override
    public void computeScroll() {
        //在上面尝试刷新视图之后被调用，并且执行了 computeScrollOffset 方法，
        //此方法根据上面传进来的起始坐标和目的坐标还有动画时间，进行计算每次移动的偏移量
        //如果到达目的坐标 false ，如果不为零 说明没有到达目的坐标
        if (scroller.computeScrollOffset()) {
            //使用 scrollTo 方法进行移动，参数是从 scroller 的 getCurrX 以及 getCurrY 方法得到的，
            // 这两个参数每次在执行 computeScrollOffset 之后都会改变，会越来越接近目的坐标。
            ((View) getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());
            // 再次刷新 view 也等于是在循环执行此方法 直到 computeScrollOffset 判断到达目的坐标为止，
            // 循环次数和每次移动的坐标距离相关，每次移动的坐标距离又跟目的坐标的距离和动画时长有关
            //通常距离越长，动画时间越长，循环次数越多
            invalidate();
        }
    }
}
