package com.jacken_liu.screen_adapter.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jacken_liu.R;

public class PercentLayout extends RelativeLayout {
    public PercentLayout(Context context) {
        super(context);
    }

    public PercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    · 创建自定义属性
    · 在容器中去创建一个静态内部类 LayoutParams
    · 在 LayoutParams 构造方法中获取自定义属性
    · onMeasure 中给子控件设置修改后的属性值
     */
    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            widthPercent = typedArray.getFraction(R.styleable.PercentLayout_widthPercent, 1, 2, 0);
            heightPercent = typedArray.getFraction(R.styleable.PercentLayout_heightPercent, 1, 2, 0);
            marginLeftPercent = typedArray.getFraction(R.styleable.PercentLayout_marginLeftPercent, 1, 2, 0);
            marginRightPercent = typedArray.getFraction(R.styleable.PercentLayout_marginRightPercent, 1, 2, 0);
            marginTopPercent = typedArray.getFraction(R.styleable.PercentLayout_marginTopPercent, 1, 2, 0);
            marginBottomPercent = typedArray.getFraction(R.styleable.PercentLayout_marginBottomPercent, 1, 2, 0);
            // 回收
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 不用加 flag ，因为当前计算的子 view 只与父容器宽高有关，重新测量也是同样的值
//        if (!flag)
        // 获取父容器宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 给子控件设置修改后的属性值
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            // 获取子控件
            View child = getChildAt(i);
            // 获取子控件 LayoutParams
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            // 判断子控件是否是百分比布局属性
            if (checkLayoutParams(layoutParams)) {
                // 是
                LayoutParams lp = (LayoutParams) layoutParams;

                float widthPercent = lp.widthPercent;
                float heightPercent = lp.heightPercent;
                float marginLeftPercent = lp.marginLeftPercent;
                float marginRightPercent = lp.marginRightPercent;
                float marginTopPercent = lp.marginTopPercent;
                float marginBottomPercent = lp.marginBottomPercent;
                if (widthPercent > 0) {
                    lp.width = (int) (widthSize * widthPercent);
                }
                if (heightPercent > 0) {
                    lp.height = (int) (heightSize * heightPercent);
                }
                if (marginLeftPercent > 0) {
                    lp.leftMargin = (int) (widthSize * marginLeftPercent);
                }
                if (marginRightPercent > 0) {
                    lp.rightMargin = (int) (widthSize * marginRightPercent);
                }
                if (marginTopPercent > 0) {
                    lp.topMargin = (int) (heightSize * marginTopPercent);
                }
                if (marginBottomPercent > 0) {
                    lp.bottomMargin = (int) (heightSize * marginBottomPercent);
                }
            }
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
