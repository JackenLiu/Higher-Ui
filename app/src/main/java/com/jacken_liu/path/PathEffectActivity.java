package com.jacken_liu.path;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.R;
import com.jacken_liu.util.DrawUtil;

/**
 * Path 路径效果
 */
public class PathEffectActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathEffectView(this));
    }

    static class PathEffectView extends View {
        private Paint mPaint;
        private Path mPath;

        // 不同路径效果集合
        private PathEffect[] effects = new PathEffect[9];
        private int[] pathsColor = new int[]{Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN,
                Color.MAGENTA, Color.RED, Color.MAGENTA, Color.BLACK, Color.BLUE};

        // 虚线效果偏移量
        private float phase;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public PathEffectView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setAlpha(180);
            mPaint.setStyle(Paint.Style.STROKE);

            // 提前初始化好 path ，避免在 onDraw 里多次初始化
            mPath = new Path();
            for (int i = 1; i < 40; i++) {
                mPath.lineTo(i * 50, (float) (Math.random() * 200));
            }
        }

        @SuppressLint("DrawAllocation")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(Color.BLACK);
            mPaint.setPathEffect(null);

            DrawUtil.addAuxiliary(canvas, mPaint);
            mPaint.setStrokeWidth(10);
            mPaint.setColor(getResources().getColor(R.color.colorAccent, null));

            // 普通效果
            effects[0] = null;
            // 圆角转折路径
            effects[1] = new CornerPathEffect(30);
            // 带偏离效果路径
            effects[2] = new DiscretePathEffect(1f, 5f);
            // 虚线效果，带偏移值
            effects[3] = new DashPathEffect(new float[]{20, 10, 5, 10}, phase);

            // 带特别图形的效果
            Path p = new Path();
            p.addRect(0, 0, 30, 15, Path.Direction.CCW);
            effects[4] = new PathDashPathEffect(p, 50, phase, PathDashPathEffect.Style.MORPH);
            effects[5] = new PathDashPathEffect(p, 50, phase, PathDashPathEffect.Style.ROTATE);
            effects[6] = new PathDashPathEffect(p, 50, phase, PathDashPathEffect.Style.TRANSLATE);

            // 路径组合效果
            effects[7] = new ComposePathEffect(effects[2], effects[4]);
            effects[8] = new SumPathEffect(effects[2], effects[4]);

            // 加入路径效果
            for (int i = 0; i < effects.length; i++) {
                mPaint.setPathEffect(effects[i]);
                mPaint.setColor(pathsColor[i]);
                canvas.drawPath(mPath, mPaint);
                canvas.translate(0, 200);
            }

            // 偏移效果增量
            phase += 1;
            invalidate();
        }

    }
}