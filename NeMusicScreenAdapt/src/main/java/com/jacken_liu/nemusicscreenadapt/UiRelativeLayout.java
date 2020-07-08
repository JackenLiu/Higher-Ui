package com.jacken_liu.nemusicscreenadapt;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 自定义布局，在测量子 view 的 onMeasure 方法里面修改子 view 宽高进行适配
 */
public class UiRelativeLayout extends RelativeLayout {
    private boolean flag = true;

    public UiRelativeLayout(Context context) {
        super(context);
    }

    public UiRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UiRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (flag) {
            flag = false;

            // 获取缩放系数
            float scaleX = UiUtils.getInstance().getHorizontalScaleValue();
            float scaleY = UiUtils.getInstance().getVerticalScaleValue();
            //获取子控件个数
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                //获取子控件
                View child = getChildAt(i);
                //对子控件属性进行相应的修改
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.width = (int) (layoutParams.width * scaleX);
                layoutParams.height = (int) (layoutParams.height * scaleY);
                layoutParams.leftMargin = (int) (layoutParams.leftMargin * scaleX);
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * scaleX);
                layoutParams.topMargin = (int) (layoutParams.topMargin * scaleY);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * scaleY);
                // padding
//                child.setPadding();
                Log.e("layoutParams", layoutParams.width + "     " + layoutParams.height);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
