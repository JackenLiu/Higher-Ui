package com.jacken_liu.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class DrawUtil {

    /**
     * 画辅助线与坐标点
     * @param canvas
     * @param mPaint
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void addAuxiliary( Canvas canvas, Paint mPaint) {
        // 背景充满整个屏幕的颜色
        canvas.drawColor(Color.WHITE);
        // 辅助线
        for (int i = 1; i < 30; i++) {
            // 横线
            canvas.drawLine(i * 100, 0, i * 100, 3000, mPaint);
            // 竖线
            canvas.drawLine(0, i * 100, 2000, i * 100, mPaint);
        }
        // 画坐标数
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 20; j++) {
                mPaint.setTextSize(20);
                canvas.drawText(i + "-" + j,
                        i * 100, j * 100, mPaint);
            }
        }
    }
}
