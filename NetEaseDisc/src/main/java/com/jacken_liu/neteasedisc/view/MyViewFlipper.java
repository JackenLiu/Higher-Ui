package com.jacken_liu.neteasedisc.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.jacken_liu.neteasedisc.R;

public class MyViewFlipper extends ViewFlipper implements View.OnTouchListener {
    // 进行内容的切换
    // 切换监听？
    private int musicSize = 0;
    private int mCurrentItem = 0;    // 初始化在第一个位置
    private float originalX;// ACTION_DOWN 事件发生时的手指坐标
    private int flipper_width = 0;// 控件宽

    public static final int SCROLL_STATE_IDLE = 0;// 空闲状态
    public static final int SCROLL_STATE_DRAGGING = 1;// 滑动状态
    public static final int SCROLL_STATE_SETTLING = 2;// 滑动后自然沉降的状态

    private OnPageChangeListener mOnPageChangeListener;//滑动切换监听


    public MyViewFlipper(Context context) {
        this(context, null);
    }

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 数据初始化
        setOnTouchListener(this);
        setLongClickable(true);
    }

    public int getMusicSize() {
        return musicSize;
    }

    /**
     * 设置歌单音乐数量
     *
     * @return
     */
    public void setMusicSize(int musicSize) {
        this.musicSize = musicSize;
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    /**
     * 设置当前下标
     *
     * @param mCurrentItem
     */
    public void setCurrentItem(int mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    /**
     * 获取下一个下标（并切换图片）
     *
     * @param index
     * @return
     */
    private int nextItem(int index) {
        mCurrentItem = index;
        // 如果是最后一首音乐，下一首变成第一首
        if (mCurrentItem >= musicSize) {
            mCurrentItem = 0;

        } else if (mCurrentItem < 0) {
            // 如果是最第一首音乐，下一首变成最后一首
            mCurrentItem = musicSize - 1;
        }
        return mCurrentItem;
    }

    /**
     * 获取上一个下标（并切换图片）
     *
     * @param index
     * @return
     */
    public int previousItem(int index) {
        mCurrentItem = index;
        if (mCurrentItem >= musicSize) {
            mCurrentItem = 0;
        } else if (mCurrentItem < 0) {
            mCurrentItem = musicSize - 1;
        }
        return mCurrentItem;
    }

    /**
     * 获取下一首歌的索引（仅获取下标）
     *
     * @return
     */
    public int getNextItem() {
        int next = mCurrentItem + 1;
        if (next >= musicSize) {
            return 0;
        } else if (next < 0) {
            return musicSize - 1;
        }
        return next;
    }

    /**
     * 获取上一首歌的索引（仅获取下标）
     *
     * @return
     */
    public int getPreviousItem() {
        int previous = mCurrentItem - 1;
        if (previous >= musicSize) {
            return 0;
        } else if (previous < 0) {
            return musicSize - 1;
        }
        return previous;
    }


    /**
     * 获取没在屏幕中显示的 View 的下标
     *
     * @return
     */
    public int getOtherItem() {
        return getChildCount() - 1 - getDisplayedChild();
    }

    /**
     * 获取没在屏幕中显示的 View
     *
     * @return
     */
    public View getOtherView() {
        return getChildAt(getChildCount() - 1 - getDisplayedChild());
    }

    /**
     * 获取当前屏幕中显示的 Poster
     *
     * @return
     */
    public ImageView getCurrentPosterView() {
        return getCurrentView().findViewById(R.id.ivPoster);
    }

    /**
     * 获取没在屏幕中显示的 Poster
     *
     * @return
     */
    public ImageView getOtherPosterView() {
        return getChildAt(getChildCount() - 1 - getDisplayedChild())
                .findViewById(R.id.ivPoster);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        flipper_width = w;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 切歌
        // 滑动距离
        float dx = event.getX() - originalX;
        float pageOffset = Math.abs(dx) / flipper_width;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                originalX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 处理碟片的移动
                getCurrentView().setTranslationX(dx);
                // 不可见？？？
                getOtherView().setVisibility(VISIBLE);
                if (dx > 0) {
                    //右滑  上一曲
                    getOtherView().setTranslationX(dx - flipper_width);
                } else {
                    //左滑    下一曲
                    getOtherView().setTranslationX(dx + flipper_width);
                }
                // 提供接口回调
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(mCurrentItem, pageOffset, dx);
                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_DRAGGING);
                }
                break;
            case MotionEvent.ACTION_UP:
                // 回弹
                final boolean isNext = dx < 0;
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_SETTLING);
                }
                if (pageOffset > 0.5) {
                    /*
                    切歌
                    从偏移的位置移动到一个控件宽的位置
                     */
                    ValueAnimator animator = ValueAnimator.ofFloat(dx, isNext ?
                            -flipper_width : flipper_width);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                            // 下一曲与当前的差了一个控件的宽度
                            getOtherView().setTranslationX((Float) animation.getAnimatedValue() +
                                    (isNext ? flipper_width : -flipper_width));
                            if (Math.abs((Float) animation.getAnimatedValue()) == flipper_width) {
                                //
                                if (isNext) {
                                    // 下一曲
                                    nextItem(mCurrentItem + 1);
                                    showNext();
                                } else {
                                    // 上一曲
                                    previousItem(mCurrentItem - 1);
                                    showPrevious();
                                }

                                if (mOnPageChangeListener != null) {
                                    mOnPageChangeListener.onPageSelected(mCurrentItem, isNext);
                                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                                }
                            }
                        }
                    });
                    animator.start();
                } else {
                    // 回弹
                    ValueAnimator animator = ValueAnimator.ofFloat(dx, 0);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                            getOtherView().setTranslationX((Float) animation.getAnimatedValue() +
                                    (isNext ? flipper_width : -flipper_width));
                            if (Math.abs((Float) animation.getAnimatedValue()) == 0) {
                                if (mOnPageChangeListener != null) {
                                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                                }
                            }
                        }
                    });
                    animator.start();
                }
                break;
        }
        return false;
    }

    /**
     * 点击上一首按钮调用
     */
    public void showPreviousWithAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, getWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                // 另一个子 View 的下标
                getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                getOtherView().setVisibility(VISIBLE);
                getOtherView().setTranslationX((Float) animation.getAnimatedValue() - getWidth());
                if (Math.abs((float) animation.getAnimatedValue()) == getWidth()) {
                    previousItem(mCurrentItem - 1);
                    showPrevious();
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener.onPageSelected(mCurrentItem, false);
                        mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                    }
                }
            }
        });
        animator.start();
    }

    /**
     * 点击下一首按钮调用
     */
    public void showNextWithAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, -getWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 另一个子 View 的下标
                getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                getOtherView().setVisibility(VISIBLE);
                getOtherView().setTranslationX((Float) animation.getAnimatedValue() + getWidth());
                if (Math.abs((float) animation.getAnimatedValue()) == getWidth()) {
                    nextItem(mCurrentItem + 1);
                    showNext();
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener.onPageSelected(mCurrentItem, true);
                        mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                    }
                }
            }
        });
        animator.start();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public interface OnPageChangeListener {
        /**
         * 页面滑动时回调
         *
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        void onPageScrolled(int position, float positionOffset, float positionOffsetPixels);

        /**
         * 页面成功切换时回调
         *
         * @param position
         * @param isNext
         */
        void onPageSelected(int position, boolean isNext);

        /**
         * 滑动的状态更改
         *
         * @param state
         */
        void onPageScrollStateChanged(int state);
    }
}
