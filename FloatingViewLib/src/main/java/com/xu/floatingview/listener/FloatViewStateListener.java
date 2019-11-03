package com.xu.floatingview.listener;

// floatingView 状态监听
public interface FloatViewStateListener {
    // 叉叉点击销毁事件
    void onRemove();

    // 播放状态
    void onMusicPlayState(MusicPlayState state);
}
