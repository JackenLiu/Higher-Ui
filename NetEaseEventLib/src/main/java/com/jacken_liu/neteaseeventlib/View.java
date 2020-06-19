package com.jacken_liu.neteaseeventlib;

import com.jacken_liu.neteaseeventlib.listener.OnClickListener;
import com.jacken_liu.neteaseeventlib.listener.OnTouchListener;

public class View {

    // 位置相关
    private int left;
    private int right;
    private int bottom;
    private int top;

    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    private OnClickListener onClickListener;
    private OnTouchListener onTouchListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    /**
     * 是否在 View 的范围内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isContainer(int x, int y) {
        if (x > left && x < right && y > top && y < bottom) {
            return true;
        }
        return false;
    }

    /**
     * View 里的分发方法只接收事件分发，本身不具备事件分发能力
     *
     * @param event
     * @return 是否消费了事件
     */
    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println("------------- " + name + "  View dispatchTouchEvent");
        // 消费
        boolean result = false;

        // 如果有 onTouchListener 就先执行 onTouchListener.onTouch()
        if (onTouchListener != null && onTouchListener.onTouch(this, event)) {
            result = true;
        }

        // 当前事件是否被消费，没消费则执行 onTouchEvent（event）
        if (!result && onTouchEvent(event)) {
            result = true;
        }

        // 默认不消费
        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        // 判断有没有设置点击事件（或者长按事件）
        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }

    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "View{" +
                "name='" + name + '\'' +
                '}';
    }
}
