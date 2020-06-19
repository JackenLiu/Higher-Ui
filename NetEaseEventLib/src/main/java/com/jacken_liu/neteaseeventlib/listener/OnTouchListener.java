package com.jacken_liu.neteaseeventlib.listener;

import com.jacken_liu.neteaseeventlib.MotionEvent;
import com.jacken_liu.neteaseeventlib.View;

public interface OnTouchListener {
    boolean onTouch(View view, MotionEvent event);
}
