package com.jacken_liu.custom_recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class NestedListView extends ListView implements NestedScrollingChild {

    /**
     * 初始化获取 ChildHelper
     *
     * @param context
     */
    private NestedScrollingChildHelper mChildHelper;

    private int mLastY;
    /**
     * 滑动偏移
     */
    private final int[] mScrollOffset = new int[2];
    /**
     * 滑动消费
     */
    private final int[] mScrollConsumed = new int[2];
    /**
     * 嵌套偏移
     */
    private int mNestedOffsetY;

    public NestedListView(Context context) {
        super(context);
        init();
    }

    public NestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int y = (int) ev.getY();
        ev.offsetLocation(0, mNestedOffsetY);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mNestedOffsetY = 0;
                // 开始嵌套滑动
                this.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                // Y 的拖动距离
                int dy = mLastY - y;
                // 注意一般一直为 0
                int oldY = getScrollY();

                // 在自己消费之前先分发给父容器
                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                    // 剩余消费
                    dy -= mScrollConsumed[1];
                    ev.offsetLocation(0, -mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                }
                mLastY = y - mScrollOffset[1];
                int newScrollY = oldY + dy;
                // 全部消费完的意思
                dy -= newScrollY - oldY;

                // 自己消费
                if (dispatchNestedScroll(0, newScrollY - dy,
                        0, dy, mScrollOffset)) {
                    ev.offsetLocation(0, mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                    mLastY -= mScrollOffset[1];
                }

                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return super.onTouchEvent(ev);
    }
}
