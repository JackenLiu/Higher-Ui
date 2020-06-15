package com.jacken_liu.path;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jacken_liu.util.DrawUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathBezierCurveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathBezierCurveView(this));
    }

    static class PathBezierCurveView extends View {
        private Path mPath;
        private Paint mPaint;

        private Paint mLinePointPaint;
        // 控制点集合（包含数据点）
        private List<PointF> mControlPoints;

        public PathBezierCurveView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPath = new Path();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.reset();
            DrawUtil.addAuxiliary(canvas, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100);

            // 三个点，二阶贝塞尔曲线
            mPath.moveTo(100, 200);
            mPath.quadTo(200, 0, 500, 300);
            // 相对坐标情况
//            mPath.rQuadTo(200, 0, 500, 300);
            canvas.drawCircle(100, 200, 10, mPaint);
            canvas.drawCircle(200, 0, 10, mPaint);
            canvas.drawCircle(500, 300, 10, mPaint);

            // 四个点，三阶贝塞尔曲线
            mPath.moveTo(300, 500);
            mPath.cubicTo(100, 500, 400, 700, 200, 800);
            canvas.drawCircle(300, 500, 10, mPaint);
            canvas.drawCircle(100, 500, 10, mPaint);
            canvas.drawCircle(400, 700, 10, mPaint);
            canvas.drawCircle(200, 800, 10, mPaint);
            canvas.drawPath(mPath, mPaint);

            // 多阶贝塞尔曲线
            mLinePointPaint.setStyle(Paint.Style.STROKE);
            mLinePointPaint.setStrokeWidth(5);
            mLinePointPaint.setColor(Color.GRAY);
            mLinePointPaint.setAlpha(100);
            mPath.reset();
            mControlPoints = new ArrayList<>();
            // 初始化随机点
            initPoint();
            // 画连线与点
            drawLineAndPoint(canvas);

            // 德卡斯特里奥算法
//            buildBezierPoints();
            // 通用计算公式
            calculate();
            canvas.drawPath(mPath, mPaint);
        }

        /**
         * 贝塞尔曲线 通用计算公式
         *
         * @return
         */
        private ArrayList<PointF> calculate() {
//            mPath.reset();
            //控制点个数(goalLineNum-1阶)
            int goalLineNum = mControlPoints.size();
            //小于2阶省略
            if (goalLineNum < 2) {
                return null;
            }
            ArrayList<PointF> points = new ArrayList<>();

            // 计算杨辉三角的目标行数据
            int[] goalData = generateLineData(goalLineNum);

            //计算坐标点
            for (int i = 0; i < 1000; i++) {
                float t = (float) i / 1000;
                // 分别计算x,y坐标
                // 计算各项和(𝑛¦𝑖) 𝑃_𝑖 〖(1−𝑡)〗^(𝑛−i) 𝑡^𝑖
                PointF pointF = new PointF();
                for (int j = 0; j < goalLineNum; j++) {
                    double pow = Math.pow(1 - t, goalLineNum - 1 - j);
                    pointF.x += goalData[j] * mControlPoints.get(j).x * pow * Math.pow(t, j);
                    pointF.y += goalData[j] * mControlPoints.get(j).y * pow * Math.pow(t, j);
                }
                points.add(pointF);
                //0 moveTo
                if (i == 0) {
                    mPath.moveTo(pointF.x, pointF.y);
                } else {
                    mPath.lineTo(pointF.x, pointF.y);
                }
            }

            return points;
        }

        /**
         * WARN 通用计算公式，未使用过，不知道怎么使用
         *
         * @param positions 贝塞尔曲线控制点坐标
         * @param precision 精度，需要计算的该条贝塞尔曲线上的点的数目
         * @return 该条贝塞尔曲线上的点（二维坐标）
         */
        public float[][] calculate(float[][] positions, int precision) {
            //维度，坐标轴数（二维坐标，三维坐标...）
            int dimension = positions[0].length;

            //贝塞尔曲线控制点数（number-1阶数）
            int number = positions.length;

            //控制点数不小于 2 ，至少为二维坐标系
            if (number < 2 || dimension < 2)
                return null;

            float[][] result = new float[precision][dimension];

            //计算杨辉三角
            int[] mi = new int[number];
            mi[0] = mi[1] = 1;//第二层（一阶时常数项）
            for (int i = 3; i <= number; i++) {
                //得到上一层的数据
                int[] t = new int[i - 1];
                for (int j = 0; j < t.length; j++) {
                    t[j] = mi[j];
                }
                //计算当前行的数据
                mi[0] = mi[i - 1] = 1;
                for (int j = 0; j < i - 2; j++) {
                    mi[j + 1] = t[j] + t[j + 1];
                }
            }

            //计算坐标点
            for (int i = 0; i < precision; i++) {
                float t = (float) i / precision;
                //分别计算各轴上的坐标
                for (int j = 0; j < dimension; j++) {
                    //计算各项和(𝑛¦𝑖) 𝑃_𝑖 〖(1−𝑡)〗^(𝑛−i) 𝑡^𝑖
                    float temp = 0.0f;
                    for (int k = 0; k < number; k++) {
                        temp += mi[k] * positions[k][j] * Math.pow(1 - t, number - 1 - k) * Math.pow(t, k);
                    }
                    result[i][j] = temp;
                }
            }
            return result;
        }

        /**
         * 计算杨辉三角数
         *
         * @param goalLineNum
         * @return
         */
        private int[] generateLineData(int goalLineNum) {
            int[][] triangle = new int[goalLineNum][];

            for (int i = 0; i < triangle.length; i++) {
                // 每行多少个
                triangle[i] = new int[i + 1];

                // 计算每行的数
                for (int j = 0; j < triangle[i].length; j++) {
                    if (i == 0 || j == 0 || i == j) {
                        triangle[i][j] = 1;
                    } else {
                        triangle[i][j] = triangle[i - 1][j] + triangle[i - 1][j - 1];
                    }
                }
            }
            return triangle[goalLineNum - 1];
        }


        /**
         * 德卡斯特里奥算法
         *
         * @return
         */
        private ArrayList<PointF> buildBezierPoints() {
            ArrayList<PointF> points = new ArrayList<>();
            int order = mControlPoints.size() - 1;//阶数
            //份数
            float delta = 1.0f / 1000;
            for (float t = 0; t <= 1; t += delta) {
                // bezier 点集
                PointF pointF = new PointF(
                        deCastelJau(order, 0, t, true),
                        deCastelJau(order, 0, t, false)
                );// 计算在曲线上的位置
                points.add(pointF);
                if (points.size() == 1) {
                    mPath.moveTo(points.get(0).x, points.get(0).y);
                } else {
                    mPath.lineTo(pointF.x, pointF.y);
                }
            }
            return points;
        }

        /**
         * 德卡斯特里奥算法 计算曲线上的点坐标
         * p(i,j) = (1-t) * p(i-1,j) + t * p(i-1,j+1)
         *
         * @param i          阶数
         * @param j          控制点
         * @param t          时间
         * @param calculateX 计算哪个坐标 true==x
         * @return
         */
        private float deCastelJau(int i, int j, float t, boolean calculateX) {
            if (i == 1) {
                return calculateX ?
                        (1 - t) * mControlPoints.get(j).x + t * mControlPoints.get(j + 1).x :
                        (1 - t) * mControlPoints.get(j).y + t * mControlPoints.get(j + 1).y;
            } else {
                return (1 - t) * deCastelJau(i - 1, j, t, calculateX) +
                        t * deCastelJau(i - 1, j + 1, t, calculateX);
            }
        }

        private void drawLineAndPoint(Canvas canvas) {
            int size = mControlPoints.size();
            PointF pointF;
            for (int i = 0; i < size; i++) {
                pointF = mControlPoints.get(i);
                // 非起点
                if (i > 0) {
                    mLinePointPaint.setColor(Color.GRAY);
                    // 控制点连线
                    PointF before = mControlPoints.get(i - 1);
                    canvas.drawLine(
                            before.x, before.y,
                            pointF.x, pointF.y,
                            mLinePointPaint
                    );
                }
                // 起点、终点，改变颜色
                if (i == 0) {
                    mLinePointPaint.setColor(Color.RED);
                } else if (i == size - 1) {
                    mLinePointPaint.setColor(Color.BLUE);
                }

                canvas.drawCircle(pointF.x, pointF.y, 10, mLinePointPaint);
            }
        }

        private void initPoint() {
            mControlPoints.clear();
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                int x = random.nextInt(600) + 300;
                int y = random.nextInt(600) + 1000;
                PointF pointF = new PointF(x, y);
                mControlPoints.add(pointF);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initPoint();
                mPath.reset();
                invalidate();
            }
            return super.onTouchEvent(event);
        }
    }

}