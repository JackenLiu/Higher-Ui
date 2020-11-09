package com.jacken_liu.neteaseeventlib;

import java.util.ArrayList;
import java.util.List;

/**
 * 容器
 */
public class ViewGroup extends View {
    /**
     * 存放子控件
     */
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];

    private TouchTarget mFirstTouchTarget;

    public ViewGroup(int left, int right, int bottom, int top) {
        super(left, right, bottom, top);
    }

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    /**
     * 事件分发的入口
     *
     * @param event
     * @return 事件被子 view 处理了
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println("------------- " + name + "  ViewGroup dispatchTouchEvent");

        TouchTarget newTouchTarget = null;
        boolean handled = false;
        // 判断是否拦截
        boolean intercepted = onInterceptTouchEvent();

        int actionMasked = event.getActionMasked();
        // 不拦截则开始分发到子控件
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted) {
            // DOWN 确认事件传递路线
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                final View[] children = mChildren;
                // 倒序遍历子控件
                for (int i = children.length-1 ; i >= 0; i--) {
                    View child = children[i];
                    // child 能否接收到事件
                    if (!child.isContainer(event.getX(), event.getY())) {
                        continue;
                    }
                    // child 能接收事件，分发给它
                    if (dispatchTransformedTouchEvent(event, child)) {
                        // 消费事件，责任链
                        handled = true;

                        newTouchTarget = addTouchTarget(child);
                        // 当前 child 已经具备消费能力了，后面的 child 则不再遍历了
                        break;
                    }
                }
            }

            /*
            · DOWN 事件后面的事件
            · 有没有事件传递链表
            · 有就顺着链去分发，没有则代表没有具备事件消费能力的子控件
             */
            // 没有链表
            if (mFirstTouchTarget == null) {
                // child 为 null 代表会直接进入 super.dispatchTouchEvent()，没有子控件消费则只有让自己去消费
                handled = dispatchTransformedTouchEvent(event, null);
            } else {
                // 按照链表结构传递事件
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    TouchTarget next = target.next;
                    // 如果当前遍历的 target 等于最近加的那个 newTouchTarget
                    if (target == newTouchTarget) {
                        // 只有 DOWN 事件才会进入
                        handled = true;
                    } else {
                        dispatchTransformedTouchEvent(event, target.child);
                    }
                    // 顺着链表下一个
                    target = next;
                }
            }
            return handled;
        }

        return super.dispatchTouchEvent(event);
    }


    /**
     * 子 view 分发处理
     *
     * @param event
     * @param child
     * @return 子 view 是否能够消费事件
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        boolean handled;
        // 当前 view 消费了
        if (child != null) {
            /*
            · 如果 child 是 view 则自己消费，不具备分发事件
            · 如果 child 是 ViewGroup 则进行递归分发
             */
            handled = child.dispatchTouchEvent(event);
        } else {
            // 没有子控件可以消费，返回给父容器消费
            handled = super.dispatchTouchEvent(event);
        }
        return handled;
    }

    private TouchTarget addTouchTarget(View child) {
        final TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    private static final class TouchTarget {
        /**
         * 能够消费事件的子控件
         */
        private View child;

        /**
         * 回收池
         */
        private static TouchTarget sRecycleBin;

        /**
         * 大小
         */
        private static int sRecycleCount;
        private static final Object sRecycleLock = new Object[0];

        public TouchTarget next;

        public static TouchTarget obtain(View child) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycleCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("已经被回收过了");
            }
            synchronized (sRecycleLock) {
                if (sRecycleCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycleCount++;
                }
            }
        }
    }


    private boolean onInterceptTouchEvent() {
        System.out.println("------------- " + name + "  ViewGroup onInterceptTouchEvent");
        return false;
    }
}
