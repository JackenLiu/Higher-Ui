package com.jacken_liu.nerippleanimator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RippleCircle extends View {

    RippleAnimatorView rippleAnimatorView;
    private int center;

    public RippleCircle(RippleAnimatorView rippleAnimatorView) {
        this(rippleAnimatorView.getContext(), null);
        this.rippleAnimatorView = rippleAnimatorView;
    }

    public RippleCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画水波
        center = (Math.min(getWidth(), getHeight())) / 2;
        canvas.drawCircle(center, center, center - rippleAnimatorView.paint.getStrokeWidth() / 2,
                rippleAnimatorView.paint);
    }
}
