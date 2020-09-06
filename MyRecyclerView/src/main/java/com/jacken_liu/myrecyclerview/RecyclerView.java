package com.jacken_liu.myrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends ViewGroup {
    private Adapter adapter;

    /**
     * 当前显示的 View
     */
    private List<View> viewList;
    /**
     * 当前滑动的 y 轴的距离
     */
    private int currentY;
    /**
     * 加载的总数据条数
     */
    private int allRowCount;
    /**
     * 屏幕中第一行 item 在总 item 数中的位置
     */
    private int firstRow;
    /**
     * 第一个 view 的左上角离屏幕顶点的距离
     */
    private int scrollY;
    /**
     * 是否初始化刷新布局（第一屏最慢）
     */
    private boolean needRelayout;

    private int width;
    private int height;

    /**
     * item 高度
     */
    private int[] heights;
    /**
     * 回收池
     */
    private Recycler recycler;
    /**
     * 最小滑动距离
     */
    private int touchSlop;

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();
        viewList = new ArrayList<>();
        needRelayout = true;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        // 判断回收池是否初始化
        if (adapter != null) {
            // 回收池初始化
            recycler = new Recycler(adapter.getViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            // 执行 onMeasure onLayout
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int h = 0;
        if (adapter != null) {
            allRowCount = adapter.getCount();
            heights = new int[allRowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight(i);
            }
        }

        // 获取数据的高度
        int tempH = sumArray(heights, 0, heights.length);
        // 取最小值
        h = Math.min(heightSize, tempH);
        // 改变容器的高
        setMeasuredDimension(widthSize, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @param array      需要计算的数组
     * @param firstIndex 第一个条目下标
     * @param count      一共计算多少个条目
     * @return
     */
    private int sumArray(int[] array, int firstIndex, int count) {
        int sum = 0;
        // 从 firstIndex 到 firstIndex+count
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (needRelayout || changed) {
            needRelayout = false;
            viewList.clear();
            removeAllViews();
            if (adapter != null) {
                // 摆放
                width = r - l;
                height = b - t;
                int left = 0, top = 0, right, botttom;
                for (int i = 0; i < allRowCount && top < height; i++) {
                    right = width;
                    botttom = top + heights[i];
                    // 生成一个 item 的 View 
                    View view = makeAndStep(i, left, top, right, botttom);
                    viewList.add(view);
                    // 循环摆放
                    top = botttom;
                }
            }
        }
    }

    /**
     * 摆放一个 item 的 View
     *
     * @param row     item 索引
     * @param left
     * @param top
     * @param right
     * @param botttom
     * @return
     */
    private View makeAndStep(int row, int left, int top, int right, int botttom) {
        View view = obtainView(row, right - left, botttom - top);
        view.layout(left, top, right, botttom);
        return view;
    }

    /**
     * 根据 viewHolder 获取 item 的 view
     *
     * @param row
     * @param width
     * @param height
     * @return
     */
    private View obtainView(int row, int width, int height) {
        // key viewType
        int viewType = adapter.getItemViewType(row);
        View recyclerView = recycler.get(viewType);
        // 取不到情况
        View view = null;
        if (recyclerView == null) {
            view = adapter.onCreateViewHolder(row, recyclerView, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHolder 必须要填充布局");
            }
        } else {
            view = adapter.onBinderViewHolder(row, recyclerView, this);
        }
        view.setTag(R.id.tag_type_view, viewType);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y2 = Math.abs(currentY - ev.getRawY());
                // 如果产生有效滑动距离，则拦截事件，开始滚动操作
                if (y2 > touchSlop) {
                    intercept = true;
                }
                break;
            default:
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float y2 = ev.getRawY();
                float diffY = currentY - y2;
                // 及时更新 currentY ，不加反应会变慢
                currentY = (int) y2;
                scrollBy(0, (int) diffY);
                break;
            default:
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void scrollBy(int x, int y) {
        // scrollY 第一个 item 左上角距离屏幕顶点的距离
        scrollY += y;
        scrollY = scrollBounds(scrollY);
        // 修正(边界值)
        if (scrollY > 0) {
            // 上滑移除
            while (scrollY > heights[firstRow]) {
                removeView(viewList.remove(0));
                scrollY -= heights[firstRow];
                firstRow++;
            }

            // 上滑加载
            while (getFillHeight() < height) {
                int addLast = firstRow + viewList.size();
                View view = obtainView(addLast, width, heights[addLast]);
                viewList.add(viewList.size(), view);
            }

        } else if (scrollY < 0) {
            // 下滑加载
            while (scrollY < 0) {
                int firstAddRow = firstRow - 1;
                View view = obtainView(firstAddRow, width, heights[firstAddRow]);
                viewList.add(0, view);
                firstRow--;
                scrollY += heights[firstRow + 1];
            }

            // 下滑移除
            while (sumArray(heights, firstRow, viewList.size()) - scrollY - heights[firstRow + viewList.size() - 1] > height) {
                removeView(viewList.remove(viewList.size() - 1));
            }
        } else {

        }

        // 滑动后重写摆放 item
        rePositionViews();
//        super.scrollBy(x, y);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        // 添加到回收池
        int key = (int) view.getTag(R.id.tag_type_view);
        recycler.put(view, key);
    }

    private int scrollBounds(int scrollY) {
        if (scrollY > 0) {
            // 上滑
            scrollY = Math.min(scrollY,
                    sumArray(heights, firstRow, heights.length - firstRow) - height);
        } else {
            // 下滑 极限值 获取零
            scrollY = Math.max(scrollY, -sumArray(heights, 0, firstRow));
        }
        return scrollY;
    }

    private void rePositionViews() {
        int left = 0, top = 0, right, botttom, i;
        top = -scrollY;
        i = firstRow;
        for (View view : viewList) {
            botttom = top + heights[i++];
            view.layout(0, top, width, botttom);
            top = botttom;
        }
    }

    private int getFillHeight() {
        // 数据高度 -scrollY，因为可能会只滑出半个 item
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }


    interface Adapter {

        View onCreateViewHolder(int position, View convertView, ViewGroup parent);

        View onBinderViewHolder(int position, View convertView, ViewGroup parent);

        // Item 的类型
        int getItemViewType(int row);

        // Item 的类型数量
        int getViewTypeCount();

        int getCount();

        int getHeight(int index);
    }
}
