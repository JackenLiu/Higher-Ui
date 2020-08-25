package com.jacken_liu.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.util.DrawUtil;

/**
 * Canvas 基本用法
 */
public class CanvasPrimaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CanvasPrimaryView(this));
    }

    static class CanvasPrimaryView extends View {

        private Paint mPaint;

        public CanvasPrimaryView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onDraw(Canvas canvas) {
            DrawUtil.addAuxiliary(canvas, mPaint);

            mPaint.setStrokeWidth(30);
            Rect rect = new Rect(300, 300, 600, 600);

//            testScale(canvas, rect);
//            testRotate(canvas, rect);
//            textSkew(canvas, rect);
//            testClip(canvas);
//            testMatrix(canvas);
//            testSave(canvas);
            testSaveLayer(canvas);
        }

        /**
         * 测试 Canvas 图层保存
         * @param canvas
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void testSaveLayer(Canvas canvas) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
            // 保存指定范围的图层，范围之外不显示
            int saveCount = canvas.saveLayer(0, 0, 600, 600, mPaint);

            mPaint.setColor(Color.GREEN);
            canvas.drawRect(350, 350, 650, 650, mPaint);
            canvas.restoreToCount(saveCount);
            mPaint.setColor(Color.BLUE);
            mPaint.setAlpha(100);
            canvas.drawRect(350, 350, 650, 650, mPaint);
        }

        /**
         * 测试 Canvas 状态保存
         * @param canvas
         */
        private void testSave(Canvas canvas) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
            //
            int saveCount = canvas.save();
            canvas.translate(50, 50);
            canvas.scale(0.5f, 0.5f);
            // save 与 restore 之间 canvas 更改的属性全部失效
//            canvas.restore();
            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);
            canvas.drawRect(350, 350, 650, 650, mPaint);
            canvas.save();
            canvas.translate(50, 50);
            mPaint.setColor(Color.GRAY);
            mPaint.setAlpha(100);
            canvas.drawRect(350, 350, 650, 650, mPaint);
//            canvas.restore();
//            canvas.restore();

            // Paint 恢复到画红色框的状态
            canvas.restoreToCount(saveCount);
            mPaint.setColor(Color.BLUE);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
        }

        /**
         * 测试矩阵
         * @param canvas
         */
        private void testMatrix(Canvas canvas) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
            Matrix matrix = new Matrix();
            matrix.setTranslate(50, 50);
            matrix.setRotate(45);
            matrix.setScale(0.5f, 0.5f);
            // 上面设置的矩阵代码与下面三行代码的效果不一致，矩阵只实现最后设置的值（功能），下面的功能则叠加实现
//            canvas.translate(50, 50);
//            canvas.rotate(45);
//            canvas.scale(0.5f, 0.5f);
            canvas.setMatrix(matrix);
            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
        }

        /**
         * 测试裁剪
         * @param canvas
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void testClip(Canvas canvas) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(300, 300, 600, 600, mPaint);
            // 限定区域内裁剪
//            canvas.clipRect(300, 300, 600, 600);
            // 限定区域外裁剪
            canvas.clipOutRect(300, 300, 600, 600);
            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);
            canvas.drawRect(350, 350, 650, 650, mPaint);
        }

        /**
         * 测试倾斜
         * @param canvas
         * @param rect
         */
        private void textSkew(Canvas canvas, Rect rect) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(rect, mPaint);
            // 整个画布的 Y 方向倾斜 45 度
            canvas.skew(0, 1);
            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);
            canvas.drawRect(rect, mPaint);
        }

        /**
         * 测试旋转
         * @param canvas
         * @param rect
         */
        private void testRotate(Canvas canvas, Rect rect) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(rect, mPaint);
            // 以 0 点坐标旋转
//            canvas.rotate(45);
            // 指定旋转中心
            canvas.rotate(45, 450, 450);
            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);
            canvas.drawRect(rect, mPaint);
        }

        /**
         * 测试缩放
         * @param canvas
         * @param rect
         */
        private void testScale(Canvas canvas, Rect rect) {
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);
            canvas.drawRect(rect, mPaint);

            mPaint.setColor(Color.GREEN);
            mPaint.setAlpha(100);

            // 缩放除了 x 、y 坐标， x 、y 轴上的线宽同样缩放
            canvas.scale(0.5f, 0.5f);
            canvas.drawRect(rect, mPaint);
        }
    }
}