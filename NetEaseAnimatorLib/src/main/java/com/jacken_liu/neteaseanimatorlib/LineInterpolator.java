package com.jacken_liu.neteaseanimatorlib;

public class LineInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolator(float input) {
        // 线性插值器，不对值更改
        return input;
    }
}
