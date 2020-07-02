package com.jacken_liu.svgmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;


public class ProvinceItem {
    private Path path;
    private String name;
    /**
     * 板块信息
     */
    private int drawColor;
    /**
     * 显示省份信息点击的点坐标
     */
    private PointF clickPoint;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public PointF getClickPoint() {
        return clickPoint;
    }

    public void setClickPoint(PointF clickPoint) {
        this.clickPoint = clickPoint;
    }

    void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            // 绘制内部颜色
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
            // 绘制边界
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xff0e8ef4);
            canvas.drawPath(path, paint);
        } else {
            // 加阴影加深颜色
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xFFFFFF);
            canvas.drawPath(path, paint);

            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
        }
    }

    public boolean isTouch(float x, float y) {
        // 获取 path 矩形区域
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        // 给定路径
        region.setPath(path, new Region(
                (int) rectF.left,
                (int) rectF.top,
                (int) rectF.right,
                (int) rectF.bottom
        ));
        return region.contains((int) x, (int) y);
    }
}
