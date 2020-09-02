package com.jacken_liu.path;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jacken_liu.R;
import com.jacken_liu.util.DrawUtil;

public class PathMeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathMeasureView(this));
    }

    static class PathMeasureView extends View {
        private Paint mPaint;
        private Paint mLinePaint;

        public PathMeasureView(Context context) {
            super(context);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(Color.RED);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(5);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // 画坐标线
            canvas.drawLine(0, getHeight() >> 1, getWidth(), getHeight() >> 1, mLinePaint);
            canvas.drawLine(getWidth() >> 1, 0, getWidth() >> 1, getHeight(), mLinePaint);

            // 移动零点位置到坐标上
            canvas.translate(getWidth() >> 1, getHeight() >> 1);

            // 获取 path 长度
//            testGetLength(canvas);

            // getPosTan 切线 tan 值
//            testPosTan(canvas);

            // 获取片段
//            testSegment(canvas);
//
            // 下一个轮廓
//            testNextContour(canvas);

            // 矩阵使用，实现箭头图片在圆圈上绕着转
            testMatrix(canvas);
        }

        /**
         * 矩阵使用，实现箭头图片在圆圈上绕着转
         * @param canvas
         */
        private void testMatrix(Canvas canvas) {
            mAFloat += 0.01;
            if (mAFloat >= 1) {
                mAFloat = 0;
            }

            Path path = new Path();
            path.addCircle(0, 0, 200, Path.Direction.CW);
            canvas.drawPath(path, mPaint);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, options);

            PathMeasure pathMeasure = new PathMeasure(path, false);

//            pathMeasure.getPosTan(
//                    pathMeasure.getLength() * mAFloat,
//                    pos, tan
//            );
//
//            double degree = Math.atan2(tan[1], tan[0]) * 180 / Math.PI;
//
//            matrix.reset();
//            matrix.postRotate((float) degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//            matrix.postTranslate(
//                    pos[0] - bitmap.getWidth() / 2,
//                    pos[1] - bitmap.getHeight() / 2
//            );

            /*
            · 使用 getMatrix 能够简化上面注释了的代码，实现同样的功能。
            · 对角度设置一块进行了封装
             */
            pathMeasure.getMatrix(
                    pathMeasure.getLength() * mAFloat,
                    matrix,
                    PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG
            );
            // 偏移加旋转
            matrix.preTranslate(pos[0] - bitmap.getWidth() / 2,
                    pos[1] - bitmap.getHeight() / 2);

            // 箭头跟着角度旋转
            canvas.drawBitmap(bitmap, matrix, mPaint);
            invalidate();
        }

        private Matrix matrix = new Matrix();
        /**
         * path 总长度的百分比
         */
        private float mAFloat;
        private float[] pos = new float[2];
        private float[] tan = new float[2];

        /**
         * 下一个轮廓
         *
         * @param canvas
         */
        private void testNextContour(Canvas canvas) {
            mPaint.setColor(Color.BLUE);
            Path path = new Path();
            path.addRect(-100, -100, 100, 100, Path.Direction.CW);
            path.addCircle(0, 0, 200, Path.Direction.CW);
            canvas.drawPath(path, mPaint);

            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, false);
            /*
            · 使用 nextContour 则把片段转移到下个个轮廓（图形）
             */
//            Log.d("TAG", "pathMeasure nextContour false: " + pathMeasure.getLength());
            pathMeasure.nextContour();
            Log.d("TAG", "pathMeasure nextContour true: " + pathMeasure.getLength());
            Path dst = new Path();
            pathMeasure.getSegment(0, 1000, dst, true);
            mPaint.setColor(Color.GREEN);
            canvas.drawPath(dst, mPaint);
        }

        /**
         * 获取片段
         *
         * @param canvas
         */
        private void testSegment(Canvas canvas) {
            mPaint.setColor(Color.LTGRAY);

            Path path = new Path();
            path.addRect(-100, -80, 100, 80, Path.Direction.CW);
            canvas.drawPath(path, mPaint);

            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, false);
            Path dst = new Path();
            /*
            startD 距离图形起点的片段起点
            stopD 距离图形起点的片段终点
            dst 获取的片段路径
            startWithMoveTo 是否从图形起点开始获取，否则从 canvas 的原点开始获取
             */
            pathMeasure.getSegment(50, 200, dst, true);
            mPaint.setColor(Color.MAGENTA);
            canvas.drawPath(dst, mPaint);
        }

        /**
         * getPosTan 切线 tan 值
         *
         * @param canvas
         */
        private void testPosTan(Canvas canvas) {
            mPaint.setColor(Color.GREEN);

            Path path = new Path();
            path.addCircle(0, 0, 100, Path.Direction.CW);
            canvas.drawPath(path, mPaint);

            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, false);
            float[] pos = new float[2];
            float[] tan = new float[2];
            /*
            · distance 表示起点到 pos[] 点的距离
            · pos[] 表示点的坐标值
            · tan[] 表示当前点在路径上的切线与 X 轴正方向的夹角A
            · tan[0] ==> x ==> cosA
            · tan[1] ==> y ==> sinA
             */
            pathMeasure.getPosTan(200, pos, tan);
            canvas.drawCircle(pos[0], pos[1], 10, mPaint);
            Log.d("TAG", pos[0] + " " + pos[1] + " " + tan[0] + " " + tan[1]);
            // 计算当前点与 X 轴的夹角
            double degree = Math.atan2(tan[1], tan[0]) * 180 / Math.PI;
            Log.d("TAG", "degree: " + degree);
        }

        /**
         * 获取 path 长度
         *
         * @param canvas
         */
        private void testGetLength(Canvas canvas) {
            Path path = new Path();
            path.lineTo(200, 0);
            path.lineTo(200, 200);
            path.lineTo(0, 200);
            canvas.drawPath(path, mPaint);

            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(path, false);
            Log.d("TAG", "Lenght without forceClose: " + pathMeasure.getLength());
            PathMeasure pathMeasure1 = new PathMeasure();
            pathMeasure1.setPath(path, true);
            Log.d("TAG", "Lenght with forceClose: " + pathMeasure1.getLength());
            PathMeasure pathMeasure2 = new PathMeasure(path, false);
            Log.d("TAG", "pathMeasure2 Lenght with forceClose: " + pathMeasure2.getLength());
        }
    }

}