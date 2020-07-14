package com.jacken_liu.nerippleanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.jacken_liu.nerippleanimator.ui.UIUtils;

import java.util.ArrayList;

public class RippleAnimatorView extends RelativeLayout {
    public Paint paint;
    int rippleColor;
    int radius;
    int strokeWidth;
    private ArrayList<RippleCircle> viewList = new ArrayList<>();
    /**
     * x 扩散、y扩散、透明度变化，多个动画
     */
    private AnimatorSet animatorSet;
    private boolean animationRunning = false;

    public RippleAnimatorView(Context context) {
        this(context, null);
    }

    public RippleAnimatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleAnimatorView);
        int ripple_anim_type = typedArray.getInt(R.styleable.RippleAnimatorView_ripple_anim_type, 0);
        paint.setStyle(ripple_anim_type == 0 ? Paint.Style.FILL : Paint.Style.STROKE);

        radius = typedArray.getInteger(R.styleable.RippleAnimatorView_radius, 54);
        strokeWidth = typedArray.getInteger(R.styleable.RippleAnimatorView_strokeWidth,
                2);
        rippleColor = typedArray.getColor(R.styleable.RippleAnimatorView_ripple_anim_color,
                ContextCompat.getColor(context, R.color.rippleColor));
        paint.setStrokeWidth(strokeWidth);
        Log.e("paint", paint.getStrokeWidth() + "   " + paint.getStyle() + "   " + strokeWidth);
        paint.setColor(rippleColor);
        Log.e("paint", paint.getColor() + "");
        typedArray.recycle();

        // 所有水波排列在中心位置
        LayoutParams layoutParams = new LayoutParams(
                UIUtils.getInstance().getWidth(radius + strokeWidth),
                UIUtils.getInstance().getWidth(radius + strokeWidth)
        );
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        // 屏幕宽 1.5 倍
        float maxScale = UIUtils.displayMetricsWidth * 1.5f / UIUtils.getInstance().getWidth(radius);
        //时间
        int rippleDuration = 2000;
        int singleDelay = rippleDuration / 6;

        ArrayList<Animator> animatorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 添加水波
            RippleCircle rippleCircle = new RippleCircle(this);
            addView(rippleCircle, layoutParams);
            viewList.add(rippleCircle);
            // 添加动画
            //X
//            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleCircle, "ScaleX", maxScale, 1);
//            scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
//            scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
//            scaleXAnimator.setDuration(rippleDuration);
//            scaleXAnimator.setStartDelay(singleDelay);
//            animatorList.add(scaleXAnimator);
//            //Y
//            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleCircle, "ScaleY", maxScale, 1);
//            scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
//            scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
//            scaleXAnimator.setDuration(rippleDuration);
//            scaleXAnimator.setStartDelay(singleDelay);
//            animatorList.add(scaleYAnimator);
//            //Alpha
//            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleCircle, "alpha", maxScale, 1);
//            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
//            alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
//            alphaAnimator.setDuration(rippleDuration);
//            alphaAnimator.setStartDelay(singleDelay);
//            animatorList.add(alphaAnimator);

            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX",
                    maxScale, 1);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY",
                    maxScale, 1);
            PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha",
                    0, 1);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(rippleCircle,
                    holder1, holder2, holder3);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setDuration(rippleDuration);
            animator.setStartDelay(i * singleDelay);
            animatorList.add(animator);
        }

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(animatorList);
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    /**
     * 开启动画
     */
    public void startRippleAnimation() {
        if (!animationRunning) {
            final ArrayList<Animator> childAnimations = animatorSet.getChildAnimations();
            for (Animator childAnimation : childAnimations) {
                ((ObjectAnimator) childAnimation).setRepeatCount(ValueAnimator.INFINITE);
            }
            for (RippleCircle rippleCircle : viewList) {
                rippleCircle.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }
    }

    /**
     * 结束动画
     */
    public void stopRippleAnimation() {
        if (animationRunning) {
            final ArrayList<Animator> childAnimations = animatorSet.getChildAnimations();
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animationRunning = false;
                }
            });

            // 每个水波纹都慢慢褪去
            final int[] count = {0};
            for (Animator childAnimation : childAnimations) {
                ((ObjectAnimator) childAnimation).setRepeatCount(0);
                childAnimation.removeAllListeners();
                childAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        count[0]++;
                        if (count[0] == childAnimations.size()) {
                            animationRunning = false;
                        }
                    }
                });
            }
        }
    }
}
