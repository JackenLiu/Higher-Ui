package com.jacken_liu.myrecyclerview;

import android.view.View;

import java.util.Stack;

/**
 * 回收池
 */
public class Recycler {

    private Stack<View>[] views;

    /**
     * @param typeNumber view 的类型数
     */
    public Recycler(int typeNumber) {
        // 栈初始化
        views = new Stack[typeNumber];
        for (int i = 0; i < typeNumber; i++) {
            views[i] = new Stack<>();
        }
    }

    /**
     * 回收到回收池（保存）
     * @param view
     * @param type
     */
    public void put(View view, int type) {
        views[type].push(view);
    }

    /**
     * 填充（获取）
     * @param type
     * @return 指定 type 的栈的要出栈的 view
     */
    public View get(int type) {
        try {
            return views[type].pop();
        } catch (Exception e) {
            return null;
        }
    }
}
