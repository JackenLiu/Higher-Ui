package com.jacken_liu.nerippleanimator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class LoadingView extends View {
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private Path dst;
    private float length;
    private float animatorValue;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        path = new Path();
        path.addCircle(500, 500, 100, Path.Direction.CW);

        pathMeasure = new PathMeasure(path, true);

        dst = new Path();
        length = pathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorValue = animation.getAnimatedFraction();
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.setRepeatCount(-1);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dst.reset();

        // path 末点
        float distance = animatorValue * length;
        /*
        path 起点
        小于 50% start 为 0，大于 50% start 为 distance 的两倍
         */
        float start = distance - (0.5f - Math.abs(animatorValue - 0.5f)) * length;
//        if (animatorValue <= 0.5) {
//            start = 0;
//        } else {
//            start = 2 * (animatorValue - 0.5f) * length;
//        }

        pathMeasure.getSegment(start, distance, dst, true);
        canvas.drawPath(dst, paint);
    }
}
