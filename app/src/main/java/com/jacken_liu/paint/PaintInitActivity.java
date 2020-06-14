package com.jacken_liu.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.R;
import com.jacken_liu.util.DrawUtil;

/**
 * Paint 最基本使用
 */
public class PaintInitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PaintInitView(this));
    }


    class PaintInitView extends View {

        private Paint mPaint;

        public PaintInitView(Context context) {
            super(context);
            mPaint = new Paint();
            // 抗锯齿
            mPaint.setAntiAlias(true);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            DrawUtil.addAuxiliary(canvas, mPaint);

            // 画背景：默认填充实心
            mPaint.setColor(getResources().getColor(R.color.colorPrimaryDark, null));

            // Paint 默认画黑色
            canvas.drawCircle(100, 100, 100, mPaint);

            // 重新设置透明度，重新设置颜色后会取消之前设置的透明度
            mPaint.setAlpha(170);
            canvas.drawCircle(100, 400, 100, mPaint);

            // 描边与描边线宽
            testStroke(canvas);

            // 重新更改线宽
            mPaint.setStrokeWidth(40);

            // 测试线帽
            testStrokeCap(canvas);

            // 测试线段连接处
            testStrokeJoin(canvas);

            // 测试文字
            testText(canvas);
        }

        /**
         * 测试文字
         * @param canvas
         */
        private void testText(Canvas canvas) {
            // 重新更改线宽
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(120);
            // 默认模式，文字指定的坐标在文字左边
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("A", 100, 1400, mPaint);
            // 文字指定的坐标在文字中间
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("A", 100, 1500, mPaint);
            // 文字指定的坐标在文字右边
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("A", 100, 1600, mPaint);

            // 画居中文字
            mPaint.setTextAlign(Paint.Align.LEFT);
            // 文字宽
            float textWidth = mPaint.measureText("A");
            // 文字 baseline 在 y 轴方向的位置
            float baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;
            // 在坐标点（100，1700）画坐标为文字中心的文字
            canvas.drawText("A", -textWidth / 2 + 100, baseLineY + 1700, mPaint);

            String str = "Android工程师";
            Rect rect = new Rect();
            // 测量文本大小，将文本大小信息存放在rect中
            mPaint.getTextBounds(str, 0, str.length(), rect);
            canvas.translate(200, 1500);
            canvas.drawRect(rect, mPaint);
            canvas.drawText(str, 0, 0, mPaint);
            // 获取字体度量对象
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float baseY = Math.abs(fontMetrics.ascent + fontMetrics.descent) / 2;

            // 画坐标为文字中心的文字
            canvas.drawText(str, -mPaint.measureText(str) / 2 + 400, baseY + 200, mPaint);
        }

        /**
         * 测试线段连接处
         * @param canvas
         */
        private void testStrokeJoin(Canvas canvas) {
            mPaint.setStyle(Paint.Style.STROKE);
            // 结合处为斜角
            mPaint.setStrokeJoin(Paint.Join.BEVEL);
            canvas.drawRect(300, 400, 600, 600, mPaint);
            // 结合处为锐角
            mPaint.setStrokeJoin(Paint.Join.MITER);
            canvas.drawRect(300, 700, 600, 900, mPaint);
            // 结合处为圆角
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawRect(300, 1000, 600, 1200, mPaint);
        }

        /**
         * 测试线帽
         * @param canvas
         */
        private void testStrokeCap(Canvas canvas) {
            mPaint.setStrokeCap(Paint.Cap.SQUARE);
            canvas.drawLine(300, 50, 600, 50, mPaint);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            canvas.drawLine(300, 150, 600, 150, mPaint);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawLine(300, 250, 600, 250, mPaint);
        }

        /**
         * 描边与描边线宽
         * @param canvas
         */
        private void testStroke(Canvas canvas) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(50);
            canvas.drawCircle(100, 700, 100, mPaint);

            // 描边+填充
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(100, 1000, 100, mPaint);
        }

    }
}
