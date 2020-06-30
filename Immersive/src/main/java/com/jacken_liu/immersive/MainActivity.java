package com.jacken_liu.immersive;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        1. 代码设置状态栏和导航栏透明
         */
        // 4.4 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                // 5.0 以上
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                window.setStatusBarColor(0);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

        /*
        2. 保证内容不扩充到状态栏区域
         */
        // 布局中添加占位状态栏
//        View statusBar = findViewById(R.id.statusBar);
//        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
//        layoutParams.height = getStatusBarHeight();
//        statusBar.setBackgroundColor(Color.RED);

        // 设置 paddingTop xml 中就不能再设置 android:fitsSystemWindows="true"
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLUE);
        } else {
            // 根布局添加占位符
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            View statusBar = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            statusBar.setBackgroundColor(Color.BLUE);
            decorView.addView(statusBar, lp);
        }
    }

    /**
     * 获取虚拟按键导航栏高度
     *
     * @return
     */
    private int getNavigationBarHeight() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
