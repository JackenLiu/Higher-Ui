package com.jacken_liu.screen_adapter.pixel_adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {

    /**
     * 设计稿参考宽高
     */
    private static final float STANDARD_WIDTH = 1080;
    private static final float STANDARD_HEIGHT = 1920;

    /**
     * 屏幕显示宽高
     */
    private int mDisplayWidth;
    private int mDisplayHeight;

    private static Utils utils;

    public Utils(Context context) {
        // 获取屏幕宽高
        if (mDisplayWidth == 0 || mDisplayHeight == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                /*
                宽高获取
                 */
                DisplayMetrics displayMetrics = new DisplayMetrics();
                // 不是 NavigationBar 沉浸式下获取的宽高（不包含 NavigationBar）
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                // 真实屏幕宽高，包含 NavigationBar
//                windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);


                /*
                判断横竖屏
                 */
                if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                    // 横屏
                    mDisplayWidth = displayMetrics.heightPixels;
                    mDisplayHeight = displayMetrics.widthPixels - getStatusBarHeight(context);
                } else {
                    mDisplayWidth = displayMetrics.widthPixels;
                    mDisplayHeight = displayMetrics.heightPixels - getStatusBarHeight(context);
                }
            }
        }
    }

    public static Utils getInstance(Context context) {
        if (utils == null) {
            utils = new Utils(context);
        }
        return utils;
    }

    public int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resId > 0) {
            // 获取具体的像素值
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 获取水平方向的缩放比
     * @return
     */
    public float getHorizontalScale() {
        return mDisplayWidth / STANDARD_WIDTH;
    }

    /**
     * 获取垂直方向的缩放比
     * @return
     */
    public float getVerticalScale() {
        return mDisplayHeight / STANDARD_HEIGHT;
    }
}
