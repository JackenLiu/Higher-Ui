package com.jacken_liu.neteaseanimatorlib;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 属性值管理器
 */
public class MyFloatPropertyValuesHolder {
    /**
     * 属性名
     */
    String mPropertyName;
    /**
     * 属性类型
     */
    Class mValueType;

    /**
     * 反射
     *
     * @param propertyName
     * @param values
     */
    Method mSetter = null;
    /**
     * 关键帧管理类
     */
    MyKeyframeSet keyframeSet;

    public MyFloatPropertyValuesHolder(String propertyName, float... values) {
        mPropertyName = propertyName;
        mValueType = float.class;
        keyframeSet = MyKeyframeSet.ofFloat(values);
    }

    /**
     * 通过反射找到控件的应得方法
     */
    public void setupSetter() {
        // 无论第一个字母是大小写都会转成大写
        char firstLetter = Character.toUpperCase(mPropertyName.charAt(0));
        String theRest = mPropertyName.substring(1);
        String methodName = "set" + firstLetter + theRest;
        try {
            mSetter = View.class.getMethod(methodName, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给控件设置相应的属性值
     *
     * @param view
     * @param fraction
     */
    public void setAnimatedValue(View view, float fraction) {

        Object value = keyframeSet.getValue(fraction);
        try {
            mSetter.invoke(view, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
