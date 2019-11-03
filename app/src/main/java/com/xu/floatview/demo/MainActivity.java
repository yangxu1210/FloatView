package com.xu.floatview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xu.floatingview.view.XuFloatManager;
import com.xu.floatingview.listener.FloatViewStateListener;
import com.xu.floatingview.listener.MusicPlayState;


public class MainActivity extends BaseAct{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void hideView(View view) {
        XuFloatManager.get().remove();
    }

    public void playAnim(View view) {
        XuFloatManager.get().getView().startRotateAnim();
    }

    public void stopAnim(View view) {
        XuFloatManager.get().getView().stopRotateAnim();
    }

    public void skipB(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TestActB.class);
        startActivity(intent);
    }


    public void showFloatView(View view) {
        XuFloatManager.get().data("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2357722536,1561223771&fm=11&gp=0.jpg","血色冰封 - Novasonic - 单曲 - 网易云音乐")
                .init()
                .listener(mListener);
    }

    private FloatViewStateListener mListener = new FloatViewStateListener() {
        @Override
        public void onRemove() {
           // 删除 浮动按钮
            XuFloatManager.get().remove();

        }

        @Override
        public void onMusicPlayState(MusicPlayState state) {

        }
    };
}
