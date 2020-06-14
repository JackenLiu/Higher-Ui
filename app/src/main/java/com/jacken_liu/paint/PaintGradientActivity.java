package com.jacken_liu.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jacken_liu.R;
import com.jacken_liu.util.DrawUtil;

/**
 * 颜色渐变模式
 */
public class PaintGradientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PaintColorView(this));
    }

    class PaintColorView extends View {

        private Paint mPaint;
        private Shader mShader;

        public PaintColorView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected void onDraw(Canvas canvas) {
            // 辅助线与坐标
            DrawUtil.addAuxiliary(canvas, mPaint);

            mPaint.setAlpha(180);

            // 测试线性渐变
            testLinearGradient(canvas);

            // 圆形渲染
            testRadialGradient(canvas);

            // 扫描渲染
            testSweepGradient(canvas);

            // 位图渲染
            testBitmapShader(canvas);

            // 组合渲染
            testComposeShader(canvas);
        }

        /**
         * 组合渐变
         * @param canvas
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        private void testComposeShader(Canvas canvas) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taylor);
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
            LinearGradient linearGradient = new LinearGradient(0, 0, 100, 0,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    // 渐变颜色的比重
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.REPEAT
            );

            mShader = new ComposeShader(bitmapShader, linearGradient, BlendMode.MULTIPLY);
            mPaint.setShader(mShader);
            canvas.drawRect(600, 600, 1000, 1000, mPaint);
        }

        /**
         * Bitmap 着色器
         * @param canvas
         */
        private void testBitmapShader(Canvas canvas) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taylor);
            mShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
            mPaint.setShader(mShader);
            canvas.drawRect(600, 0, 1000, 400, mPaint);
        }

        /**
         * 扫描渐变
         * @param canvas
         */
        private void testSweepGradient(Canvas canvas) {
            // 与线性渐变类似
            mShader = new SweepGradient(400, 1000,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    new float[]{0f, 0.5f, 1f}
            );
            mPaint.setShader(mShader);
            canvas.drawCircle(400, 1000, 100, mPaint);
        }

        /**
         * 辐射（水波纹）渐变
         * @param canvas
         */
        private void testRadialGradient(Canvas canvas) {
            // 与线性渐变类似
            mShader = new RadialGradient(400, 100, 100,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.REPEAT
            );
            mPaint.setShader(mShader);
            canvas.drawCircle(400, 100, 100, mPaint);
            canvas.drawRect(300, 300, 500, 500, mPaint);

            mShader = new RadialGradient(400, 700, 50,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.REPEAT
            );
            mPaint.setShader(mShader);
            canvas.drawRect(300, 600, 500, 800, mPaint);
        }

        /**
         * 测试线性渐变
         * @param canvas
         */
        private void testLinearGradient(Canvas canvas) {
            // 重复模式
            mShader = new LinearGradient(0, 0, 100, 0,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    // 渐变颜色的比重
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.REPEAT
            );
            mPaint.setShader(mShader);
            canvas.drawRect(0, 0, 200, 200, mPaint);

            // 坐标值改变渐变的方向
            mShader = new LinearGradient(0, 0, 100, 100,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    // 渐变颜色的比重
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.REPEAT
            );
            mPaint.setShader(mShader);
            canvas.drawRect(0, 300, 200, 500, mPaint);


            // 单色结束模式
            mShader = new LinearGradient(0, 0, 100, 0,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.CLAMP
            );
            mPaint.setShader(mShader);
            canvas.drawRect(0, 600, 200, 800, mPaint);

            // 镜像模式
            mShader = new LinearGradient(0, 0, 100, 0,
                    new int[]{Color.RED, Color.BLUE, Color.GREEN},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.MIRROR
            );
            mPaint.setShader(mShader);
            canvas.drawRect(0, 900, 200, 1100, mPaint);
        }

    }

}