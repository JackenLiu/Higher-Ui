package com.jacken_liu.custom_recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 负责承载每个子 view 的布局
        // 初始化布局 需要修改 attachToRoot 为 false，不传默认判断 viewGroup!= null 为 true
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_feed, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        // 会在绑定到 ViewGroup 时调用
        // 头像
        holder.mIvAvatar.setImageResource(getAvatarResourse(position));
        // 名字
        holder.mTvNickname.setText("NetEase" + (position + 1));
        // 内容
        holder.mIvContent.setImageResource(getContentResourse(position));
    }

    /**
     * 获取内容资源 id
     *
     * @param position
     * @return
     */
    private int getContentResourse(int position) {
        switch (position % 4) {
            case 0:
                return R.drawable.taeyeon_one;
            case 1:
                return R.drawable.taeyeon_two;
            case 2:
                return R.drawable.taeyeon_three;
            case 3:
                return R.drawable.taeyeon_four;
        }
        return 0;
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

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvAvatar;
        ImageView mIvContent;
        TextView mTvNickname;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mIvContent = itemView.findViewById(R.id.iv_content);
            mTvNickname = itemView.findViewById(R.id.tv_nickname);
        }
    }
}
