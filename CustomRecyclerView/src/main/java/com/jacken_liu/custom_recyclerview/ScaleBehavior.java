package com.jacken_liu.custom_recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

public class ScaleBehavior<V extends View> extends CoordinatorLayout.Behavior {

    private Interpolator interplator;
    private boolean isRunning;

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        interplator = new AccelerateDecelerateInterpolator();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {

        // 只有返回 true 即垂直运动，后续的动作才会触发
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

        if (dyConsumed > 0 && !isRunning && child.getVisibility() == View.VISIBLE) {
            // 上滑 缩小隐藏动画
            scaleHide(child);
        } else if (dyConsumed < 0 && !isRunning && child.getVisibility() == View.INVISIBLE) {
            // 下滑 放大显示
            scaleShow(child);
        }
    }

    private void scaleHide(final View child) {
        ViewCompat.animate(child).alpha(0).scaleX(0).scaleY(0).setInterpolator(interplator)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isRunning = false;
                        child.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isRunning = false;
                    }
                }).setDuration(500).start();
    }

    private void scaleShow(final View child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child).alpha(1).scaleX(1).scaleY(1).setInterpolator(interplator)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        isRunning = false;
                        child.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        isRunning = false;
                    }
                }).setDuration(500).start();
    }
}
