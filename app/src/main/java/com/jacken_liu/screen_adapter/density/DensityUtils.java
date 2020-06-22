package com.jacken_liu.screen_adapter.density;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class DensityUtils {
    /**
     * 参考设备的宽 360 dp（设计稿？）
     */
    private static final float WIDTH = 360;
    /**
     * 表示屏幕密度
     */
    private static float appDensity;
    /**
     * 字体缩放比例，默认为 appDensity
     */
    private static float appScaleDensity;

    public static void setDensity(final Application application, Activity activity) {
        // 获取当前屏幕信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();

        if (appDensity == 0) {
            // 初始值赋值
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            // 监听字体变化
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    // 字体发生变化，重新计算 scaleDensity
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        // 计算目标 density ScaleDensity
        float targetDensity = displayMetrics.widthPixels / WIDTH;
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        // 替换到 activity
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;
    }
}
