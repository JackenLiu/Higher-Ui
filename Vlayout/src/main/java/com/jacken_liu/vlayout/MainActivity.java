package com.jacken_liu.vlayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.bumptech.glide.Glide;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    /**
     * menu 的应用
     */
    String[] ITEM_NAMES = {"天猫", "聚划算", "天猫国际", "外卖", "天猫超市", "充值中心", "飞猪旅行", "领金币",
            "拍卖", "分类"};
    int[] IMG_URLS = {R.mipmap.ic_tian_mao, R.mipmap.ic_ju_hua_suan, R.mipmap.ic_tian_mao_guoji,
            R.mipmap.ic_waimai, R.mipmap.ic_chaoshi, R.mipmap.ic_voucher_center, R.mipmap.ic_travel,
            R.mipmap.ic_tao_gold, R.mipmap.ic_auction, R.mipmap.ic_classify};

    /**
     * 高颜值商品位
     */
    int[] ITEM_URL = {R.mipmap.item1, R.mipmap.item2, R.mipmap.item3, R.mipmap.item4, R.mipmap.item5};
    int[] GRID_URL = {R.mipmap.flashsale1, R.mipmap.flashsale2, R.mipmap.flashsale3, R.mipmap.flashsale4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this);
        // RecyclerView 设置 LayoutManager
        mRecyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);
        mRecyclerView.setHasFixedSize(true);

        viewPool.setMaxRecycledViews(0, 10);

        /*
        Banner 区域
         */
        BaseDelegateAdapter bannerAdapter = new BaseDelegateAdapter(this, new SingleLayoutHelper(),
                R.layout.vlayout_banner, 1, 0) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                Banner mBanner = holder.getView(R.id.banner);
                // 数据绑定
                List<String> arrayList = new ArrayList<>();
                arrayList.add("https://img.alicdn.com/simba/img/TB1GRL1fUD1gK0jSZFGSuvd3FXa.jpg");
                arrayList.add("https://img.alicdn.com/tfs/TB16m08gmf2gK0jSZFPXXXsopXa-520-280.jpg_q90_.webp");
                arrayList.add("https://img.alicdn.com/simba/img/TB1GRL1fUD1gK0jSZFGSuvd3FXa.jpg");
                arrayList.add("https://img.alicdn.com/simba/img/TB1NHBZf4D1gK0jSZFKSuwJrVXa.jpg");
                arrayList.add("https://img.alicdn.com/simba/img/TB1TnSugi_1gK0jSZFqSuwpaXXa.jpg");
                // 设置banner样式
                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                // 设置图片加载器
                mBanner.setImageLoader(new GlideImageLoader());
                // 设置图片集合
                mBanner.setImages(arrayList);
                // 设置banner动画效果
                mBanner.setBannerAnimation(Transformer.DepthPage);
                // 设置标题集合（当banner样式有显示title时）
                //        mBanner.setBannerTitles(titles);
                // 设置自动轮播，默认为true
                mBanner.isAutoPlay(true);
                // 设置轮播时间
                mBanner.setDelayTime(3000);
                // 设置指示器位置（当 banner 模式中有指示器时）
                mBanner.setIndicatorGravity(BannerConfig.CENTER);
                // banner设置方法全部调用完毕时最后调用
                mBanner.start();
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(getApplicationContext(), "banner点击了" + position, Toast.LENGTH_SHORT).show();
                    }
                });
//                super.onBindViewHolder(holder, position);
            }
        };


        /*
        Menu 区域
         */
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        gridLayoutHelper.setPadding(0, 16, 0, 0);
        // 控制子元素间的距离
        gridLayoutHelper.setHGap(10);
        gridLayoutHelper.setVGap(10);
        BaseDelegateAdapter menuAdapter = new BaseDelegateAdapter(this, gridLayoutHelper,
                R.layout.vlayout_menu, 10, 1) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
                holder.setText(R.id.tv_menu_title_home, ITEM_NAMES[position] + "");
                holder.setImageResource(R.id.iv_menu_home, IMG_URLS[position]);
                holder.getView(R.id.ll_menu_home).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), ITEM_NAMES[position], Toast.LENGTH_SHORT).show();
                    }
                });
//                super.onBindViewHolder(holder, position);
            }
        };


        /*
        新闻区域
         */
        BaseDelegateAdapter newsAdapter = new BaseDelegateAdapter(this, new LinearLayoutHelper(),
                R.layout.vlayout_news, 1, 2) {
            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                MarqueeView marqueeView1 = holder.getView(R.id.marqueeView1);
                MarqueeView marqueeView2 = holder.getView(R.id.marqueeView2);

                List<String> info1 = new ArrayList<>();
                info1.add("天猫超市最近发大活动啦，快来抢");
                info1.add("没有最便宜，只有更便宜！");

                List<String> info2 = new ArrayList<>();
                info2.add("这个是用来搞笑的，不要在意这写小细节！");
                info2.add("啦啦啦啦，我就是来搞笑的！");

                marqueeView1.startWithList(info1);
                marqueeView2.startWithList(info2);
                // 在代码里设置自己的动画
                marqueeView1.startWithList(info1, R.anim.anim_bottom_in, R.anim.anim_top_out);
                marqueeView2.startWithList(info2, R.anim.anim_bottom_in, R.anim.anim_top_out);

                marqueeView1.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        Toast.makeText(getApplicationContext(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                marqueeView2.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        Toast.makeText(getApplicationContext(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
//                super.onBindViewHolder(holder, position);
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(menuAdapter);
        delegateAdapter.addAdapter(newsAdapter);


        /*
        热门商品
         */
        for (int i = 0; i < ITEM_URL.length; i++) {
            final int finalI = i;
            BaseDelegateAdapter titleAdapter = new BaseDelegateAdapter(this, new LinearLayoutHelper(),
                    R.layout.vlayout_title, 1, 3) {
                @Override
                public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    holder.setImageResource(R.id.iv, ITEM_URL[finalI]);
                }
            };

            BaseDelegateAdapter gridAdapter = new BaseDelegateAdapter(this, new GridLayoutHelper(2),
                    R.layout.vlayout_grid, 4, 3) {
                @Override
                public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    holder.setImageResource(R.id.iv, GRID_URL[position]);
                }
            };
            delegateAdapter.addAdapter(titleAdapter);
            delegateAdapter.addAdapter(gridAdapter);
        }

        /*
        分类 tab
         */
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        BaseDelegateAdapter stickyAdapter = new BaseDelegateAdapter(this, stickyLayoutHelper,
                R.layout.vlayout_table, 1, 6);
        delegateAdapter.addAdapter(stickyAdapter);

        /*
        瀑布流商品展示区域
         */
        StaggeredGridLayoutHelper staggeredHelper = new StaggeredGridLayoutHelper(2);
        staggeredHelper.setHGap(5);
        staggeredHelper.setVGap(5);
        final String goods[] = {
                "https://img.alicdn.com/imgextra/i4/248494226/O1CN010wK9hI1h5YINJkPVr_!!248494226.jpg_640x640q80_.webp",
                "https://img.alicdn.com/imgextra/i4/370244303/O1CN01T9yH1H1heooaaKHrk_!!370244303.png_640x640q80_.webp",
                "https://img.alicdn.com/bao/uploaded/i4/263320395/O1CN01k0Qkn21Emwyt3cCPy_!!263320395.jpg_640x640q80_.webp",
                "https://img.alicdn.com/imgextra/i2/1683088433/O1CN01oKdgR12CAMa7OZD2V_!!1683088433.jpg_640x640q80_.webp",
                "https://img.alicdn.com/imgextra/i3/1630108307/TB2qWjXc30kpuFjSspdXXX4YXXa_!!1630108307.jpg_640x640q80_.webp"};
        BaseDelegateAdapter staggeredAdapter = new BaseDelegateAdapter(this, staggeredHelper,
                R.layout.vlayout_goods, 50, 7) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                ImageView goodIv = holder.getView(R.id.iv_good);
//                goodIv.setImageResource(R.mipmap.flashsale1);
                Glide.with(MainActivity.this).load(goods[position % 5]).placeholder(R.mipmap.flashsale1).into(goodIv);
                super.onBindViewHolder(holder, position);
            }
        };
        delegateAdapter.addAdapter(staggeredAdapter);

        // 所有 Adapter 设置完毕
        mRecyclerView.setAdapter(delegateAdapter);
    }
}
