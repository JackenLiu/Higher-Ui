package com.jacken_liu.neteaseanimatorlib;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;

import java.util.Arrays;
import java.util.List;

/**
 * 关键帧集合
 */
public class MyKeyframeSet {
    /**
     * 类型估值器
     */
    TypeEvaluator mEvaluator;
    List<MyFloatKeyframe> mKeyframes;

    public MyKeyframeSet(MyFloatKeyframe... keyframes) {
        mKeyframes = Arrays.asList(keyframes);
        mEvaluator = new FloatEvaluator();
    }

    public static MyKeyframeSet ofFloat(float... values) {
        if (values.length <= 0) {
            return null;
        }

        // 初始化关键帧
        int numKeyframes = values.length;
        // 数组保存所有关键帧
        MyFloatKeyframe keyframe[] = new MyFloatKeyframe[numKeyframes];

        // 第一帧：fraction=0 ==> 动画在当第一帧出现的时候为 0%
        keyframe[0] = new MyFloatKeyframe(0, values[0]);

        for (int i = 0; i < numKeyframes; i++) {
            // 计算其他帧
            keyframe[i] = new MyFloatKeyframe(
                    // 需要转成 float ，否则 int 类型容易为 0
                    (float) i / (numKeyframes - 1),
                    values[i]
            );
        }

        return new MyKeyframeSet(keyframe);
    }

    /**
     * 获取指定百分比对应的具体帧数属性值
     *
     * @param fraction 指定百分比
     * @return
     */
    public Object getValue(float fraction) {
        // 从第一帧开始循环
        MyFloatKeyframe prevKeyframe = mKeyframes.get(0);

        for (int i = 0; i < mKeyframes.size(); i++) {
            MyFloatKeyframe nextKeyframe = mKeyframes.get(i);
            // 获取到指定百分比最近的下一帧
            if (fraction < nextKeyframe.getFraction()) {
                // 指定百分比在两帧的间隔百分比
                float intervalFraction = (fraction - prevKeyframe.getFraction()) /
                        (nextKeyframe.getFraction() - prevKeyframe.getFraction());

                return mEvaluator == null ?
                        prevKeyframe.getValue() + intervalFraction * (nextKeyframe.getValue() - prevKeyframe.getValue()) :
                        ((Number) mEvaluator.evaluate(intervalFraction, prevKeyframe.getValue(), nextKeyframe.getValue())).floatValue();
            }
            // 第一帧没找到则找下一帧
            prevKeyframe = nextKeyframe;
        }

        // 没有找到合适的关键字信息（如 200%）
        return mKeyframes.get(mKeyframes.size() - 1).getValue();
    }
}
