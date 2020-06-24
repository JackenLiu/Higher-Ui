package com.jacken_liu.custom_recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private ListView mListView;
    private ListAdapter mListAdapter;

    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTv;
    private ImageView mSuspensionIv;

    private int mSuspensionBarHeight;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mListView = findViewById(R.id.list_view);
        mListAdapter = new ListAdapter(this);
        mListView.setAdapter(mListAdapter);

        //悬浮控件初始化
        mSuspensionBar = findViewById(R.id.suspension_bar);
        mSuspensionIv = findViewById(R.id.iv_avatar);
        mSuspensionTv = findViewById(R.id.tv_nickname);

        mSuspensionBar.setAlpha(0.5f);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 获取悬浮条的高度
                mSuspensionBarHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // 调整悬浮条的位置
                // 获取下一个显示的 item 的位置进行调整
                View nextView = mListView.getChildAt(mCurrentPosition + 1);
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
                if (mCurrentPosition != mListView.getFirstVisiblePosition()) {
                    mCurrentPosition = mListView.getFirstVisiblePosition();

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