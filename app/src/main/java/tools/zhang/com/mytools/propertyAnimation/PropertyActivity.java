package tools.zhang.com.mytools.propertyAnimation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import tools.zhang.com.mytools.R;

/**
 * Created by zhangdecheng on 2016/10/28.
 */
public class PropertyActivity extends Activity {
    View view;
    private TextView mRotateView;
    private TextView mRotateXView;
    private TextView mRotateYView;

    private TextView mAlphaView;
    private TextView mscaleXView;
    private TextView mtranslate_X;
    private TextView mbackgroundColorView;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        mRotateView = (TextView) findViewById(R.id.rotate);
        mRotateXView = (TextView) findViewById(R.id.rotatex);
        mRotateYView = (TextView) findViewById(R.id.rotatey);
        mAlphaView = (TextView) findViewById(R.id.alpha);
        mscaleXView = (TextView) findViewById(R.id.scaleX);
        mtranslate_X = (TextView) findViewById(R.id.translate_X);
        mbackgroundColorView = (TextView) findViewById(R.id.backgroundColor);
        mButton = (Button) findViewById(R.id.btn);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rotationAnimation();
//                scaleAnimation();
                rapperViewAnimation();
            }
        });
    }

    private void rotationAnimation() {
        // 设置旋转的支点  ,设置后旋转中心为控件的左下角,， 默认为view的中心点
        mRotateView.setPivotX(mRotateView.getX());
        mRotateView.setPivotY(mRotateView.getY());
        ObjectAnimator rotationanim = ObjectAnimator.ofFloat(mRotateView, "rotation", 0, 270); //旋转动画（0-360度）
        rotationanim.setDuration(2000); //持续时间
        rotationanim.setRepeatCount(0); //循环次数（-1 为无限循环）
        rotationanim.start();

        ObjectAnimator rotationXanim = ObjectAnimator.ofFloat(mRotateXView, "rotationX", 0, 180); //旋转动画（0-360度）
        rotationXanim.setDuration(2000); //持续时间
        rotationXanim.setRepeatCount(0); //循环次数（-1 为无限循环）
        rotationXanim.start();


        ObjectAnimator rotationYanim = ObjectAnimator.ofFloat(mRotateYView, "rotationY", 0, 180); //旋转动画（0-360度）
        rotationYanim.setDuration(2000); //持续时间
        rotationYanim.setRepeatCount(0); //循环次数（-1 为无限循环）
        rotationYanim.start();

        ObjectAnimator alphaanim = ObjectAnimator.ofFloat(mAlphaView, "alpha", 1.0f, 0.2f); //
        alphaanim.setDuration(2000); //持续时间
        alphaanim.setRepeatCount(0); //循环次数（-1 为无限循环）
        alphaanim.start();

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mscaleXView, "scaleX", 1f, 0.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mscaleXView, "scaleY", 1f, 0.2f);
        animatorSet.setDuration(2000);
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSet.start();


        ObjectAnimator translationUp = ObjectAnimator.ofFloat(mtranslate_X, "X", 0, 110, 0);
        //ObjectAnimator translationUp = ObjectAnimator.ofFloat(mtranslate_X, "translationX", 0, -110);
        translationUp.setDuration(2000);
        translationUp.setRepeatCount(-1);
        translationUp.start();


        ObjectAnimator backgroundAnim = ObjectAnimator.ofInt(mbackgroundColorView,"backgroundColor", Color.RED, Color.BLUE, Color.GRAY, Color.GREEN);
        backgroundAnim.setInterpolator(new DecelerateInterpolator());
        backgroundAnim.setDuration(2000);
        backgroundAnim.setRepeatCount(-1);
        backgroundAnim.setRepeatMode(Animation.REVERSE);
        /*
         * ArgbEvaluator：这种评估者可以用来执行类型之间的插值整数值代表ARGB颜色。
         * FloatEvaluator：这种评估者可以用来执行浮点值之间的插值。
         * IntEvaluator：这种评估者可以用来执行类型int值之间的插值。
         * RectEvaluator：这种评估者可以用来执行类型之间的插值矩形值。
         *
         * 由于本例是改变View的backgroundColor属性的背景颜色所以此处使用ArgbEvaluator
         */
        backgroundAnim.setEvaluator(new ArgbEvaluator());
        backgroundAnim.start();
    }

    private void scaleAnimation() {
        Animation animation = AnimationUtils.loadAnimation(PropertyActivity.this, R.anim.scale_animation);
        mRotateView.startAnimation(animation);
    }

    private void rapperViewAnimation() {
        RapperButton rapperButton = new RapperButton(mButton);
        ObjectAnimator rotationXanim = ObjectAnimator.ofInt(rapperButton, "width", 300, 900);
//        ObjectAnimator rotationXanim = ObjectAnimator.ofInt(rapperButton, "width", 900);
        rotationXanim.setDuration(2000);
        rotationXanim.start();
    }
}
