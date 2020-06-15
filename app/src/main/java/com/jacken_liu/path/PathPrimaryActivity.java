package com.jacken_liu.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.util.DrawUtil;

/**
 * Path 的基本应用
 */
public class PathPrimaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathPrimaryView(this));
    }

    static class PathPrimaryView extends View {

        private Paint mPaint;
        private Path mPath1;
        private Path mPath2;

        public PathPrimaryView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPath1 = new Path();
            mPath2 = new Path();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            DrawUtil.addAuxiliary(canvas, mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);

            // 测试两段路径的相交效果
            testOperation(canvas);

            // 单个路径添加多个形状后的填充效果
            testFillType(canvas);

            // 画线
            testLineTo(canvas);

            // 添加图形
            testAddShape(canvas);
        }

        /**
         * 添加图形
         * @param canvas
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void testAddShape(Canvas canvas) {
            mPath1 = new Path();
            // 圆
            mPath1.addCircle(900, 300, 100, Path.Direction.CW);
            // 弧
            mPath1.addArc(800, 500, 900, 550, 0, 180);
            mPath1.addArc(800, 500, 900, 600, 0, 180);

            // 带有连接功能的弧
            mPath1.arcTo(800, 700, 900, 800, 0, 180, true);
            mPath1.arcTo(800, 800, 900, 900, 0, 180, false);

            // 椭圆
            mPath1.addOval(800, 1000, 1000, 1300, Path.Direction.CW);

            // 矩形
            mPath1.addRect(800, 1400, 1000, 1500, Path.Direction.CW);

            // 圆角矩形
            mPath1.addRoundRect(800, 1600, 1000, 1700, 50, 80, Path.Direction.CW);
            canvas.drawPath(mPath1, mPaint);
        }

        /**
         * 画线
         *
         * @param canvas
         */
        private void testLineTo(Canvas canvas) {
            mPath1 = new Path();
            // WARN 如果不是 STROKE 画不出来
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(10);

            mPath1.moveTo(700, 0);
            mPath1.rMoveTo(100, 100);
            mPath1.moveTo(800, 0);
            mPath1.rLineTo(100, 100);
            mPath1.lineTo(1000, 100);
            mPath1.close();
            canvas.drawPath(mPath1, mPaint);
        }

        /**
         * 单个路径添加多个形状后的填充效果
         *
         * @param canvas
         */
        private void testFillType(Canvas canvas) {
            // 取非交集
            mPath1 = new Path();
            mPath1.addCircle(500, 100, 100, Path.Direction.CW);
            mPath1.addCircle(600, 100, 100, Path.Direction.CW);
            mPath1.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(mPath1, mPaint);

            // TODO 非交集的反向（目前会包含整个屏幕，与网上部分例子不符，尚未找到原因）
            mPath1 = new Path();
            mPath1.addCircle(500, 400, 100, Path.Direction.CW);
            mPath1.addCircle(600, 400, 100, Path.Direction.CW);
            mPath1.setFillType(Path.FillType.INVERSE_EVEN_ODD);
//            canvas.drawPath(mPath1, mPaint);

            // TODO 路径内的反向 （目前会包含整个屏幕，与网上部分例子不符，尚未找到原因）
            mPath1 = new Path();
            mPath1.addCircle(500, 700, 100, Path.Direction.CW);
            mPath1.addCircle(600, 700, 100, Path.Direction.CW);
            mPath1.setFillType(Path.FillType.INVERSE_WINDING);
//            canvas.drawPath(mPath1, mPaint);

            // 取路径内，默认效果
            mPath1 = new Path();
            mPath1.addCircle(500, 1000, 100, Path.Direction.CW);
            mPath1.addCircle(600, 1000, 100, Path.Direction.CW);
            mPath1.setFillType(Path.FillType.WINDING);
            canvas.drawPath(mPath1, mPaint);
        }

        /**
         * 测试两段路径的相交效果
         *
         * @param canvas
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void testOperation(Canvas canvas) {
            mPath1.addCircle(100, 100, 100, Path.Direction.CW);
            mPath2.addCircle(200, 100, 100, Path.Direction.CW);
            // 路径1 减去 路径2
            mPath1.op(mPath2, Path.Op.DIFFERENCE);
            canvas.drawPath(mPath1, mPaint);

            mPath1 = new Path();
            mPath2 = new Path();
            mPath1.addCircle(100, 400, 100, Path.Direction.CW);
            mPath2.addCircle(200, 400, 100, Path.Direction.CW);
            // 路径1 路径2 取交集
            mPath1.op(mPath2, Path.Op.INTERSECT);
            canvas.drawPath(mPath1, mPaint);

            mPath1 = new Path();
            mPath2 = new Path();
            mPath1.addCircle(100, 700, 100, Path.Direction.CW);
            mPath2.addCircle(200, 700, 100, Path.Direction.CW);
            // 路径2 减去 路径1
            mPath1.op(mPath2, Path.Op.REVERSE_DIFFERENCE);
            canvas.drawPath(mPath1, mPaint);

            mPath1 = new Path();
            mPath2 = new Path();
            mPath1.addCircle(100, 1000, 100, Path.Direction.CW);
            mPath2.addCircle(200, 1000, 100, Path.Direction.CW);
            // 路径1 路径2 融合
            mPath1.op(mPath2, Path.Op.UNION);
            canvas.drawPath(mPath1, mPaint);

            mPath1 = new Path();
            mPath2 = new Path();
            mPath1.addCircle(100, 1300, 100, Path.Direction.CW);
            mPath2.addCircle(200, 1300, 100, Path.Direction.CW);
            // 路径1 路径2 取反交集
            mPath1.op(mPath2, Path.Op.XOR);
            canvas.drawPath(mPath1, mPaint);
        }
    }

}