package com.xu.floatingview.listener;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xu.floatingview.view.FloatView;
import com.xu.floatingview.view.XuFloatManager;

// 音乐浮动按钮 行为定义
public interface IFloatView {

    XuFloatManager remove();

    XuFloatManager init();

    XuFloatManager data(String... data);

    XuFloatManager attach(Activity activity);

    XuFloatManager attach(FrameLayout container);

    XuFloatManager detach(Activity activity);

    XuFloatManager detach(FrameLayout container);

    FloatView getView();

    XuFloatManager layoutParams(ViewGroup.LayoutParams params);

    XuFloatManager listener(FloatViewStateListener viewStateLIstener);

}
