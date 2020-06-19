package com.jacken_liu.neteaseanimatorlib;

/**
 * 关键帧
 */
public class MyFloatKeyframe {

    /**
     * 当前百分比 0~1
     */
    float mFraction;
    Class mValueType;
    float mValue;

    /**
     * @param fraction 帧数占所有帧数的百分比
     * @param value    传的帧数值
     */
    public MyFloatKeyframe(float fraction, float value) {
        mFraction = fraction;
        mValue = value;
        mValueType = float.class;
    }

    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float mFraction) {
        this.mFraction = mFraction;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float mValue) {
        this.mValue = mValue;
    }
}
