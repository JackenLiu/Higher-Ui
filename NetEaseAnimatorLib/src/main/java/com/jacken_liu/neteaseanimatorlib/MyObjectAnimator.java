package com.jacken_liu.neteaseanimatorlib;


import android.view.View;

import java.lang.ref.WeakReference;

public class MyObjectAnimator implements VsyncManager.AnimationFrameCallback {
    private long mDuration = 0;
    private WeakReference<View> mTarget;
    /**
     * 属性值管理类
     */
    MyFloatPropertyValuesHolder myFloatPropertyValuesHolder;
    /**
     * 目前显示第几帧
     */
    private int index = 0;
    private TimeInterpolator interpolator;

    public MyObjectAnimator(View mTarget, String propertyName, float... values) {
        this.mTarget = new WeakReference<>(mTarget);
        this.myFloatPropertyValuesHolder = new MyFloatPropertyValuesHolder(propertyName, values);
    }

    public static MyObjectAnimator ofFloat(View target, String propertyName, float... values) {
        MyObjectAnimator anim = new MyObjectAnimator(target, propertyName, values);
        return anim;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    // 每隔 16 ms
    @Override
    public boolean doAnimationFrame(long currentTime) {
        // 动画总帧数
        float total = mDuration / 16;

        // 执行百分比(目前显示帧数/总帧数)
        float fraction = index++ / total;

        // 插值器可以修改对应的百分比
        if (interpolator != null) {
            fraction = interpolator.getInterpolator(fraction);
        }

        // 动画循环
        if (index >= total) {
            index = 0;
        }

        // 给 view 设置相应的属性值
        myFloatPropertyValuesHolder.setAnimatedValue(mTarget.get(), fraction);

        return false;
    }

    public void start() {
        myFloatPropertyValuesHolder.setupSetter();
        VsyncManager.getInstance().add(this);
    }
}
