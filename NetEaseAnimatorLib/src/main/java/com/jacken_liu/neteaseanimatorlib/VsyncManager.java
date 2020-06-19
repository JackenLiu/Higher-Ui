package com.jacken_liu.neteaseanimatorlib;

import java.util.ArrayList;
import java.util.List;

public class VsyncManager {
    private static final VsyncManager ourInstance = new VsyncManager();

    public static VsyncManager getInstance() {
        return ourInstance;
    }

    public VsyncManager() {
        new Thread(runnable).start();
    }

    /**
     * 同时执行多个动画
     */
    private List<AnimationFrameCallback> list = new ArrayList<>();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    // 16 毫秒发送一次信号
                    Thread.sleep(16);
                    for (AnimationFrameCallback callback : list) {
                        callback.doAnimationFrame(System.currentTimeMillis());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    interface AnimationFrameCallback{
        boolean doAnimationFrame(long currentTime);
    }

    public void add(AnimationFrameCallback animationFrameCallback) {
        list.add(animationFrameCallback);
    }
}
