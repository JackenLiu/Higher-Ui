package com.jacken_liu.nemusicscreenadapt;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 像素标准缩放工具
 */
public class UiUtils {
    private static UiUtils instance;

    /**
     * 标准值(如果项目中设计稿尺寸不同可以改成非常量)
     */
    public static final float STANDARD_WIDTH = 1080f;
    public static final float STANDARD_HEIGHT = 1920f;
    /**
     * 获取屏幕实际宽高
     */
    public static float displayMetricsWidth;
    public static float displayMetricsHeight;

    /**
     * 状态栏
     */
    public int statusBarHeight;

    public static UiUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UiUtils(context.getApplicationContext());
        }
        return instance;
    }

    public static UiUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("UiUtils 应先初始化");
        }
        return instance;
    }

    /**
     * 修改标准尺寸值
     * @param context
     * @return
     */
    public static UiUtils notifyInstance(Context context) {
        instance = new UiUtils(context.getApplicationContext());
        return instance;
    }


    public UiUtils(Context context) {
        // 计算缩放系数
        if (displayMetricsHeight == 0 || displayMetricsWidth == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            // 不包含 navigationbar 区域
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            // 包含 navigationbar 区域
//            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            statusBarHeight = getSystemBarHeight(context);

            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                // 横屏
                displayMetricsWidth = displayMetrics.heightPixels;
                displayMetricsHeight = displayMetrics.widthPixels - statusBarHeight;
            } else {
                // 竖屏
                displayMetricsWidth = displayMetrics.widthPixels;
                displayMetricsHeight = displayMetrics.heightPixels - statusBarHeight;
            }

        }
    }

    /**
     * 缩放系数
     * @return
     */
    public float getHorizontalScaleValue() {
        return  displayMetricsWidth / STANDARD_WIDTH;
    }
    public float getVerticalScaleValue() {
        return  displayMetricsHeight / STANDARD_HEIGHT;
    }

    /**
     * 取整 ，获取接近但不小于当前值的整数
     *
     * @param width 控件的宽
     * @return 适配后的宽
     */
    public int getWidth(int width) {
        return Math.round(width * displayMetricsWidth / STANDARD_WIDTH);
    }

    /**
     *
     * @param height 控件的高
     * @return 适配后的高
     */
    public int getHeight(int height) {
        return Math.round(height * displayMetricsHeight / STANDARD_HEIGHT);
    }


    private int getSystemBarHeight(Context context) {
        return getValue(context, "com.android.internal.R$dimen", "status_bar_height", 48);
    }

    private int getValue(Context context, String dimenName, String status_bar_height, int defaultValue) {
        try {
            Class<?> cls = Class.forName(dimenName);
            Object instance = cls.newInstance();

            Field field = cls.getField(status_bar_height);
            int id = Integer.parseInt(field.get(instance).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
