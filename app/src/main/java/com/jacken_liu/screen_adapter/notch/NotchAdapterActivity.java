package com.jacken_liu.screen_adapter.notch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jacken_liu.R;

import java.util.List;

public class NotchAdapterActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1.设置全屏显示
        Window window = getWindow();
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//        );

        // 2.让内容延伸进入刘海区域
        /*
         * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT       全屏模式，内容下移，非全屏模式下不受影响
         * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES   允许内容进入刘海区域
         * #LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER         不允许内容进入刘海区域
         */
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        // 3.状态栏透明
        window.setStatusBarColor(0);

        // 4.设置沉浸式
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        int visibility = window.getDecorView().getSystemUiVisibility();
        visibility |= flags;
        window.getDecorView().setSystemUiVisibility(visibility);

        setContentView(R.layout.activity_notch_adapter);

        // 遮挡 view 下沉
//        Button button = findViewById(R.id.btn);
//        ConstraintLayout.LayoutParams layoutParamsB = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        // 刘海高度
//        layoutParamsB.topMargin = heightForDisplayCutOut();


        /*
        刘海屏适配步骤
        · 1、设置（全屏）
        · 2、判断手机厂商
        · 3、判断是否有刘海屏
        · 4、获取刘海屏区域宽高
        · 5、控件避开刘海屏区域
         */
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 判断有没有刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
            if (displayCutout != null) {
                // 有刘海
                Log.e("DisplayCutout", "Rect " + displayCutout.getBoundingRects());
                Log.e("DisplayCutout", "Left " + displayCutout.getSafeInsetLeft());
                Log.e("DisplayCutout", "Top " + displayCutout.getSafeInsetTop());
                Log.e("DisplayCutout", "Right " + displayCutout.getSafeInsetRight());
                Log.e("DisplayCutout", "Bottom " + displayCutout.getSafeInsetBottom());

                // 刘海区域
                List<Rect> boundingRects = displayCutout.getBoundingRects();
                for (Rect rect : boundingRects) {
                    // 可能有多个刘海
                    Log.e("刘海区域", "宽：" + rect.width() + "," + "高：" + rect.height());
                }

                // 使用 layout 下沉
                ConstraintLayout layout = findViewById(R.id.layout);
                layout.setPadding(
                        displayCutout.getSafeInsetLeft(),
                        displayCutout.getSafeInsetTop(),
                        displayCutout.getSafeInsetRight(),
                        displayCutout.getSafeInsetBottom()
                );
            }
        }
    }

    private int heightForDisplayCutOut() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }

        // 一般刘海高度
        return 96;
    }
}