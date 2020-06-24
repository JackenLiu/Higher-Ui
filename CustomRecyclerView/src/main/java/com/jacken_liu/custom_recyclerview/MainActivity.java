package com.jacken_liu.custom_recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;

    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTv;
    private ImageView mSuspensionIv;

    private int mSuspensionBarHeight;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mFeedAdapter = new FeedAdapter();
        mRecyclerView.setAdapter(mFeedAdapter);
        // 宽高确定 （在刷新的时候不会重新设置宽高，属于性能优化）
        mRecyclerView.setHasFixedSize(true);

        //悬浮控件初始化
        mSuspensionBar = findViewById(R.id.suspension_bar);
        mSuspensionIv = findViewById(R.id.iv_avatar);
        mSuspensionTv = findViewById(R.id.tv_nickname);

        mSuspensionBar.setAlpha(0.5f);

        // 监听 RecyclerView 的滑动
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取悬浮条的高度
                mSuspensionBarHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 调整悬浮条的位置
                // 获取下一个显示的 item 的位置进行调整
                View nextView = linearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                if (nextView != null) {

                    // 下一个 item 到顶的高度小于悬浮条的高度时，进行推进
                    if (nextView.getTop() < mSuspensionBarHeight) {
                        // 调整悬浮条位置，此时悬浮条进行推进效果
                        mSuspensionBar.setTranslationY(nextView.getTop() - mSuspensionBarHeight);
                    } else {
                        // 下一个 item 到顶的高度大于悬浮条的高度时，悬浮条位置保持不变
                        mSuspensionBar.setTranslationY(0);
                    }
                }

                // 滑动时更新 mCurrentPosition
                if (mCurrentPosition != linearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    // 更新悬浮条内容
                    updateSuspensionBar();
                }
            }
        });

        // 首次更改
        updateSuspensionBar();
    }

    private void updateSuspensionBar() {
        // 头像
        mSuspensionIv.setImageResource(getAvatarResourse(mCurrentPosition));
        // 名字
        mSuspensionTv.setText("NetEase" + (mCurrentPosition + 1));
    }

    /**
     * 获取头像资源 id
     *
     * @param position
     * @return
     */
    private int getAvatarResourse(int position) {
        switch (position % 4) {
            case 0:
                return R.drawable.avatar1;
            case 1:
                return R.drawable.avatar2;
            case 2:
                return R.drawable.avatar3;
            case 3:
                return R.drawable.avatar4;
        }
        return 0;
    }
}
