package com.jacken_liu.svgmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapView extends View {
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFF4087A3};

    /**
     * 所有省份省份集合
     */
    private List<ProvinceItem> itemList;
    private Paint paint;
    /**
     * 当前选中的省份
     */
    private ProvinceItem select;
    /**
     * 地图大小信息
     */
    private RectF totalRect;
    private float scale = 1.0f;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    /**
     * 第一个按下的手指的点
     */
    private PointF startPoint = new PointF();
    /**
     * 两个按下的手指的触摸点的中点
     */
    private PointF midPoint = new PointF();
    /**
     * 初始的两个手指按下的触摸点的距离
     */
    private float oriDis = 1f;
    private boolean actionClick = true;
    private float translateX;
    private float translateY;
    /**
     * 是否需要显示省份名
     */
    private boolean shouldShowText;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemList = new ArrayList<>();
        loadThread.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取当前控件的宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (totalRect != null) {
            float mapWidth = totalRect.width();
            scale = width / mapWidth;
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        // 当前缩放系数
        float currentScaleCount = 0;
        // 当前 x 平移距离
        float currentTranslateX = 0;
        // 当前 y 平移距离
        float currentTranslateY = 0;
        // MotionEvent.ACTION_MASK 为多点触控掩饰码
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 单点触控
                startPoint.set(event.getX(), event.getY());
                mode = DRAG;
                actionClick = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 多点触控
                oriDis = distance(event);
                if (oriDis > 10) {
                    midPoint = midPoint(event);
                    mode = ZOOM;
                }
                actionClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动
                if (mode == DRAG) {
                    // 单指拖动
                    if (Math.abs(x - startPoint.x) > 10 || Math.abs(y - startPoint.y) > 10) {
                        currentTranslateX = translateX + x - startPoint.x;
                        currentTranslateY = translateY + y - startPoint.y;
                        translateX = currentTranslateX;
                        translateY = currentTranslateY;
                        startPoint.set(x, y);
                        actionClick = false;
                        invalidate();
                    }
                } else if (mode == ZOOM) {
                    // 两指缩放 当前两指的距离
                    float newDist = distance(event);
                    if (Math.abs(newDist - oriDis) > 10) {
                        float scaleInner = newDist / oriDis;
                        currentScaleCount = scale + scaleInner - 1;
                        if (currentScaleCount < 1) {
                            scale = 1;
                        } else {
                            scale = currentScaleCount;
                        }
                        oriDis = newDist;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = NONE;
                if (actionClick) {
                    handleTouch(x / scale - translateX, y / scale - translateY);
                }
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            default:
        }
        return true;
    }

    private void handleTouch(float x, float y) {
        // 默认不显示文字
        shouldShowText = false;
        if (itemList == null) {
            return;
        }
        ProvinceItem selectItem = null;
        for (ProvinceItem provinceItem : itemList) {
            if (provinceItem.isTouch(x, y)) {
                selectItem = provinceItem;
                provinceItem.setClickPoint(new PointF(x, y));
                shouldShowText = true;
            }
        }
        if (selectItem != null) {
            select = selectItem;
            postInvalidate();
        }
    }

    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            InputStream inputStream = getResources().openRawResource(R.raw.china);
            // 获取 DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                // 解析输入流 获取 Document 实例
                Document document = builder.parse(inputStream);
                Element rootElement = document.getDocumentElement();
                // 先找到 Path
                NodeList path = rootElement.getElementsByTagName("path");
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;
                List<ProvinceItem> list = new ArrayList<>();
                for (int i = 0; i < path.getLength(); i++) {
                    Element element = (Element) path.item(i);
                    // 路径信息
                    String pathData = element.getAttribute("d");
                    // 名称
                    String name = element.getAttribute("title");
                    // 将 pathData 转成 Path
                    Path path1 = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path1);
                    provinceItem.setName(name);
                    provinceItem.setDrawColor(colorArray[i % 4]);
                    RectF rectF = new RectF();
                    path1.computeBounds(rectF, true);

                    left = left == -1 ? rectF.left : Math.min(left, rectF.left);
                    right = right == -1 ? rectF.right : Math.max(right, rectF.right);
                    top = top == -1 ? rectF.top : Math.min(top, rectF.top);
                    bottom = bottom == -1 ? rectF.bottom : Math.max(bottom, rectF.bottom);
                    list.add(provinceItem);
                }
                itemList = list;
                totalRect = new RectF(left, top, right, bottom);
                // 刷新界面(防止还没加载完数据就把 view 画出来了)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        invalidate();
                    }
                });
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (itemList != null && itemList.size() > 0) {
            canvas.save();
            canvas.scale(scale, scale);
            canvas.translate(translateX, translateY);
            for (ProvinceItem provinceItem : itemList) {
                if (provinceItem != select) {
                    provinceItem.drawItem(canvas, paint, false);
                } else {
                    provinceItem.drawItem(canvas, paint, true);
                }
            }

            if (shouldShowText) {
                // 绘制文本
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(40);
                canvas.drawText(select.getName(), select.getClickPoint().x, select.getClickPoint().y, paint);
                canvas.restore();
            }
        }
    }

    /**
     * 计算两个手指头之间的中心点的位置
     * x = (x1+x2)/2;
     * y = (y1+y2)/2;
     *
     * @param event 触摸事件
     * @return 返回中心点的坐标
     */
    private PointF midPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        // 两点间距离公式
        return (float) Math.sqrt(x * x + y * y);
    }
}
