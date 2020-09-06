package com.jacken_liu.progressboat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressView extends View {

    /**
     * 最大进度
     */
    private float mMaxProgress;
    /**
     * 当前进度
     */
    private float mCurrentProgress;

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;

    private int width;
    private int height;

    /**
     * 水波偏移
     */
    private float mPathOffset;

    /**
     * 一个正弦周期宽
     */
    private float perCycleWidth;
    /**
     * 一个正弦周期路径长度
     */
    private float perCycleLength;
    /**
     * 周期个数
     */
    private float cycleCount = 6;

    private Bitmap mBoat;
    private float[] pos;
    private float[] tan;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 数据初始化
        mMaxProgress = 100;
        mCurrentProgress = 0;
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(0x771234FF);
        mPaint.setTextSize(40);

        mPath = new Path();
        mPathMeasure = new PathMeasure();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        mBoat = BitmapFactory.decodeResource(getResources(), R.drawable.boat, options);

        pos = new float[2];
        tan = new float[2];
    }

    public float getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(float mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
    }

    public float getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(float mCurrentProgress) {
        if (mCurrentProgress >= mMaxProgress) {
            this.mCurrentProgress = mMaxProgress;
        } else if (mCurrentProgress <= 0) {
            this.mCurrentProgress = 0;
        } else {
            this.mCurrentProgress = mCurrentProgress;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("onMeasure", "==========");

        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("onSizeChanged", "==========");

        super.onSizeChanged(w, h, oldw, oldh);

        // 对 path 进行初始化
        mPath.reset();

        // 获取周期宽
        perCycleWidth = w / cycleCount;
        // path 绘制一段正弦曲线
        mPath.moveTo(0, h / 2);

        for (int i = 0; i < cycleCount + 2; i++) {
            // 前二分之一段
            mPath.rQuadTo(perCycleWidth / 4, -perCycleWidth / 4, perCycleWidth / 2, 0);
            // 后二分之一段
            mPath.rQuadTo(perCycleWidth / 4, perCycleWidth / 4, perCycleWidth / 2, 0);
        }

        // PathMeasure 测量
        mPathMeasure.setPath(mPath, false);
        // 每一个周期路径的长度
        perCycleLength = mPathMeasure.getLength() / (cycleCount + 2);

        // 路径闭合形成水波
        mPath.lineTo(w + perCycleWidth * 2, h);
        mPath.lineTo(0, h);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("onDraw", "==========");
        super.onDraw(canvas);

        // 水波上的点
        mPathMeasure.getPosTan(mPathOffset, pos, tan);
        canvas.save();
        // 第一条水波
        canvas.translate(-pos[0], 0);
        canvas.drawPath(mPath, mPaint);
        canvas.save();
        // 第二条水波 （快，平移两次）
        canvas.translate(-pos[0], 0);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();

        /*
        再次测量加上当前的偏移（小船的位置）还与当前的进度有关
         */
        // 小船在 path 上的距离
        float boatOffset = mCurrentProgress / mMaxProgress * perCycleLength * cycleCount + mPathOffset;
        mPathMeasure.getPosTan(boatOffset, pos, tan);
        // 平移到图片底部中心
        canvas.translate(-mBoat.getWidth() / 2, -mBoat.getHeight());
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        // 绕着零点(图片底线中点)进行旋转
        canvas.rotate(degree,
                pos[0] + mBoat.getWidth() / 2,
                pos[1] + mBoat.getHeight()
        );
        // 文字(图片左上角坐标加上 Bitmap 半个宽度)
        canvas.drawText((int) (mCurrentProgress / mMaxProgress * 100) + "%",
                pos[0] + (mCurrentProgress > mMaxProgress / 2 ? -mBoat.getWidth() / 2 : mBoat.getWidth() / 2),
                pos[1], mPaint
        );
        // 直接使用在 path 上的点进行画图（已平移到图片底部中心）
        canvas.drawBitmap(mBoat, pos[0], pos[1], mPaint);
        canvas.restore();

        mPathOffset += 1;
        if (mPathOffset >= perCycleLength) {
            mPathOffset = 0;
        }

        invalidate();
    }
}
