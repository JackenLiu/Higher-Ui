package com.jacken_liu.neteasedisc.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jacken_liu.neteasedisc.model.MusicData;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    /*操作指令Service使用*/
    public static final String ACTION_OPT_MUSIC_PLAY = "ACTION_OPT_MUSIC_PLAY";
    public static final String ACTION_OPT_MUSIC_PAUSE = "ACTION_OPT_MUSIC_PAUSE";
    public static final String ACTION_OPT_MUSIC_NEXT = "ACTION_OPT_MUSIC_NEXT";
    public static final String ACTION_OPT_MUSIC_PREVIOUS = "ACTION_OPT_MUSIC_PREVIOUS";
    public static final String ACTION_OPT_MUSIC_SEEK_TO = "ACTION_OPT_MUSIC_SEEK_TO";

    /*状态指令Activity使用*/
    public static final String ACTION_STATUS_MUSIC_PLAY = "ACTION_STATUS_MUSIC_PLAY";
    public static final String ACTION_STATUS_MUSIC_PAUSE = "ACTION_STATUS_MUSIC_PAUSE";
    public static final String ACTION_STATUS_MUSIC_COMPLETE = "ACTION_STATUS_MUSIC_COMPLETE";
    public static final String ACTION_STATUS_MUSIC_DURATION = "ACTION_STATUS_MUSIC_DURATION";

    /*参数Key*/
    public static final String PARAM_MUSIC_DURATION = "PARAM_MUSIC_DURATION";
    public static final String PARAM_MUSIC_SEEK_TO = "PARAM_MUSIC_SEEK_TO";
    public static final String PARAM_MUSIC_CURRENT_POSITION = "PARAM_MUSIC_CURRENT_POSITION";
    public static final String PARAM_MUSIC_IS_OVER = "PARAM_MUSIC_IS_OVER";
    public static final String PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST";

    private int mCurrentMusicIndex = 0;//当前播放的音乐下标
    private boolean mIsMusicPause = false;
    private List<MusicData> mMusicData = new ArrayList<>();

    private MusicReceiver mMusicReceiver = new MusicReceiver();
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取音乐数据
        initMusicData(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化广播接收者
        initBoardCastReceiver();
    }

    private void initMusicData(Intent intent) {
        if (intent == null) return;
        List<MusicData> musicData = (List<MusicData>) intent.getSerializableExtra(PARAM_MUSIC_LIST);
        this.mMusicData.addAll(musicData);
    }

    private void initBoardCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //意图过滤器添加
        intentFilter.addAction(ACTION_OPT_MUSIC_PLAY);
        intentFilter.addAction(ACTION_OPT_MUSIC_PAUSE);
        intentFilter.addAction(ACTION_OPT_MUSIC_NEXT);
        intentFilter.addAction(ACTION_OPT_MUSIC_PREVIOUS);
        intentFilter.addAction(ACTION_OPT_MUSIC_SEEK_TO);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();//释放媒体播放器
        mMediaPlayer = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver);//取消广播监听
    }

    private void play(final int index) {
        if (index >= mMusicData.size()) return;
        if (mCurrentMusicIndex == index && mIsMusicPause) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer.stop();
            mMediaPlayer = null;

            mMediaPlayer = MediaPlayer.create(getApplicationContext(), mMusicData.get(index)
                    .getMusicRes());
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(this);
            mCurrentMusicIndex = index;
            mIsMusicPause = false;

            int duration = mMediaPlayer.getDuration();
            sendMusicDurationBroadCast(duration);
        }
        sendMusicStatusBroadCast(ACTION_STATUS_MUSIC_PLAY);
    }

    private void pause() {
        mMediaPlayer.pause();
        mIsMusicPause = true;
        sendMusicStatusBroadCast(ACTION_STATUS_MUSIC_PAUSE);
    }

    private void stop() {
        mMediaPlayer.stop();
    }

    private void next() {
        if (mCurrentMusicIndex + 1 < mMusicData.size()) {
            play(mCurrentMusicIndex + 1);
        } else {
            play(0);
        }
    }

    private void previous() {

        if (mCurrentMusicIndex != 0) {
            play(mCurrentMusicIndex - 1);
        } else {
            play(mMusicData.size() - 1);
        }
    }

    private void seekTo(Intent intent) {
        if (mMediaPlayer.isPlaying()) {
            int position = intent.getIntExtra(PARAM_MUSIC_SEEK_TO, 0);
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        sendMusicCompleteBroadCast();
    }

    class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;//断言 为true继续执行，false退出并抛出异常
            switch (action) {
                case ACTION_OPT_MUSIC_PLAY:
                    play(mCurrentMusicIndex);
                    break;
                case ACTION_OPT_MUSIC_PAUSE:
                    pause();
                    break;
                case ACTION_OPT_MUSIC_PREVIOUS:
                    previous();
                    break;
                case ACTION_OPT_MUSIC_NEXT:
                    next();
                    break;
                case ACTION_OPT_MUSIC_SEEK_TO:
                    seekTo(intent);
                    break;
            }
        }
    }

    private void sendMusicCompleteBroadCast() {
        //音乐播放完成通知应用，根据当前的循环模式判断下一步操作
        Intent intent = new Intent(ACTION_STATUS_MUSIC_COMPLETE);
        intent.putExtra(PARAM_MUSIC_IS_OVER, (mCurrentMusicIndex == mMusicData.size() - 1));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendMusicDurationBroadCast(int duration) {
        //更新当前音乐播放进度
        Intent intent = new Intent(ACTION_STATUS_MUSIC_DURATION);
        intent.putExtra(PARAM_MUSIC_DURATION, duration);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendMusicStatusBroadCast(String action) {
        //更新当前应用播放状态
        Intent intent = new Intent(action);
        if (action.equals(ACTION_STATUS_MUSIC_PLAY)) {
            intent.putExtra(PARAM_MUSIC_CURRENT_POSITION, mMediaPlayer.getCurrentPosition());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
