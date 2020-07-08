package com.jacken_liu.nemusicscreenadapt;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 直接修改子 view 宽高工具类
 * 注意：不同布局 LayoutParams 不一样
 */
public class ViewCalculateUtil {

    /**
     * 字体适配
     *
     * @param view
     * @param size 字体大小实际就是字体的高度
     */
    public static void setTextSize(TextView view, int size) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, UiUtils.getInstance().getHeight(size));
    }

    /**
     * 控件适配
     *
     * @param view
     * @param width
     * @param height
     * @param leftMargin
     * @param topMargin
     * @param rightMargin
     * @param bottomMargin
     * @param asWidth      是否只按照宽的比例缩放（如普通屏幕的正方形控件，超长屏幕也显示为正方形控件，不按照高比例缩放）
     */
    public static void setViewRelativeLayoutParam(View view, int width, int height, int leftMargin, int topMargin,
                                                  int rightMargin, int bottomMargin, boolean asWidth) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            // 如果是指定数值宽，不是匹配父控件或者包含内容（不确定的宽，如多个文字）
            if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT
                    && width != RelativeLayout.LayoutParams.FILL_PARENT) {
                layoutParams.width = UiUtils.getInstance().getWidth(width);
            } else {
                layoutParams.width = width;
            }

            // 如果是指定数值高，不是匹配父控件或者包含内容（不确定的高，如多个文字会令高不确定，内容可长可段）
            if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT
                    && height != RelativeLayout.LayoutParams.FILL_PARENT) {
                // 高是否也按照宽比例缩放
                layoutParams.height = asWidth ? UiUtils.getInstance().getWidth(height)
                        : UiUtils.getInstance().getHeight(height);
            } else {
                layoutParams.height = height;
            }

            // 其他 Y 轴上的属性是否也按照宽比例缩放
            layoutParams.topMargin = asWidth ? UiUtils.getInstance().getWidth(topMargin) :
                    UiUtils.getInstance().getHeight(topMargin);
            layoutParams.bottomMargin = asWidth ? UiUtils.getInstance().getWidth(bottomMargin) :
                    UiUtils.getInstance().getHeight(bottomMargin);
            layoutParams.leftMargin = UiUtils.getInstance().getWidth(leftMargin);
            layoutParams.rightMargin = UiUtils.getInstance().getWidth(rightMargin);

            view.setLayoutParams(layoutParams);
        }
    }

    public static void setViewFrameLayoutParam(View view, int width, int height, int lefMargin, int topMargin, int bottomMargin,
                                               int rightMargin, boolean asWidth) {

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UiUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UiUtils.getInstance().getWidth(height) : UiUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        layoutParams.topMargin = asWidth ? UiUtils.getInstance().getWidth(topMargin) : UiUtils.getInstance().getHeight(topMargin);
        layoutParams.bottomMargin = asWidth ? UiUtils.getInstance().getWidth(bottomMargin) : UiUtils.getInstance().getHeight(bottomMargin);
        layoutParams.leftMargin = UiUtils.getInstance().getWidth(lefMargin);
        layoutParams.rightMargin = UiUtils.getInstance().getWidth(rightMargin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置view的内边距
     *
     * @param view
     * @param topPadding
     * @param bottomPadding
     * @param leftPadding
     * @param rightPadding
     */
    public static void setViewPadding(View view, int topPadding, int bottomPadding, int leftPadding, int rightPadding) {
        view.setPadding(UiUtils.getInstance().getWidth(leftPadding),
                UiUtils.getInstance().getHeight(topPadding),
                UiUtils.getInstance().getWidth(rightPadding),
                UiUtils.getInstance().getHeight(bottomPadding));
    }


    /**
     * 设置LinearLayout中 view的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLinearLayoutParam(View view, int width, int height, boolean asWidth) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UiUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UiUtils.getInstance().getWidth(height) : UiUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParam(View view, int width, int height, boolean asWidth) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UiUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UiUtils.getInstance().getWidth(height) : UiUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置 LinearLayout 中 view 的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLinearLayoutParam(View view, int width, int lefMargin, int height, int topMargin, int bottomMargin,
                                                int rightMargin, boolean asWidth) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UiUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UiUtils.getInstance().getWidth(height) : UiUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        layoutParams.topMargin = asWidth ? UiUtils.getInstance().getWidth(topMargin) : UiUtils.getInstance().getHeight(topMargin);
        layoutParams.bottomMargin = asWidth ? UiUtils.getInstance().getWidth(bottomMargin) : UiUtils.getInstance().getHeight(bottomMargin);
        layoutParams.leftMargin = UiUtils.getInstance().getWidth(lefMargin);
        layoutParams.rightMargin = UiUtils.getInstance().getWidth(rightMargin);
        view.setLayoutParams(layoutParams);
    }
}
