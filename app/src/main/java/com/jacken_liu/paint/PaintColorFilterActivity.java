package com.jacken_liu.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.R;
import com.jacken_liu.util.DrawUtil;

/**
 * Paint 颜色滤镜
 */
public class PaintColorFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PaintColorFilterView(this));
    }

    static class PaintColorFilterView extends View {
        private Paint mPaint;

        public PaintColorFilterView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setAlpha(180);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            DrawUtil.addAuxiliary(canvas, mPaint);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taylor);

            // 颜色滤镜
            testLightingColorFilter(canvas, bitmap);

            testPorterDuffColorFilter(canvas, bitmap);

            testColorMatrixColorFilter(canvas, bitmap);

        }

        /**
         * 颜色矩阵调整
         * @param canvas
         * @param bitmap
         */
        private void testColorMatrixColorFilter(Canvas canvas, Bitmap bitmap) {
            // 颜色矩阵：底片效果
            float[] colorMatrix = {
                    -1, 0, 0, 0, 255, // red
                    0, -1, 0, 0, 255, // green
                    0, 0, -1, 0, 255, // blue
                    0, 0, 0, 1, 0     // alpha
            };

            ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
            mPaint.setColorFilter(colorMatrixColorFilter);
            canvas.drawBitmap(bitmap, 0, 600, mPaint);

            // 颜色矩阵封装类
            ColorMatrix colorMatrix1 = new ColorMatrix();
            // 亮度调节
            colorMatrix1.setScale(1.2f, 1.2f, 1.2f, 1);
            // 饱和度：0 无色彩、1 默认效果、>1 饱和度增强
//            colorMatrix1.setSaturation(0);
            // 色调调节
//            colorMatrix1.setRotate(2, 45);
            ColorMatrixColorFilter colorMatrixColorFilter1 = new ColorMatrixColorFilter(colorMatrix1);
            mPaint.setColorFilter(colorMatrixColorFilter1);
            canvas.drawBitmap(bitmap, 300, 600, mPaint);

        }

        /**
         * 单颜色滤镜跳转
         * @param canvas
         * @param bitmap
         */
        private void testPorterDuffColorFilter(Canvas canvas, Bitmap bitmap) {
            PorterDuffColorFilter duffColorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
            mPaint.setColorFilter(duffColorFilter);
            canvas.drawBitmap(bitmap, 0, 300, mPaint);
        }

        /**
         * 颜色增量减量滤镜
         * @param canvas
         * @param bitmap
         */
        private void testLightingColorFilter(Canvas canvas, Bitmap bitmap) {
            // 去除红色
            LightingColorFilter colorFilter = new LightingColorFilter(0x00ffff, 0x000000);
            mPaint.setColorFilter(colorFilter);
            canvas.drawBitmap(bitmap, 0, 0, mPaint);

            // 绿色更亮
            LightingColorFilter colorFilter1 = new LightingColorFilter(0xffffff, 0x003000);
            mPaint.setColorFilter(colorFilter1);
            canvas.drawBitmap(bitmap, 300, 0, mPaint);
        }
    }
}