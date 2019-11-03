package com.xu.floatingview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xu.floatingview.listener.FloatViewStateListener;
import com.xu.floatingview.listener.IFloatView;
import com.xu.floatingview.utils.EnContext;

/**
 * create by YangXu on 2019/10/12
 * 悬浮窗管理器
 */
public class XuFloatManager implements IFloatView {

    private FloatView xuFloatView;
    private static volatile XuFloatManager mInstance;
    private FrameLayout mContainer;
    private String[] mdata;// 数据源

    private XuFloatManager() {
    }

    public static XuFloatManager get() {
        if (mInstance == null) {
            synchronized (XuFloatManager.class) {
                if (mInstance == null) {
                    mInstance = new XuFloatManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public XuFloatManager remove() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (xuFloatView == null) {
                    return;
                }
                if (ViewCompat.isAttachedToWindow(xuFloatView) && mContainer != null) {
                    xuFloatView.clearAnimation();
                    int targetX = xuFloatView.getLocation() == xuFloatView.LEFT ? -(xuFloatView.getWidth()) : xuFloatView.getWidth();
                    PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0f, targetX);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(xuFloatView, translationX);
                    // 设置间隔时间
                    objectAnimator.setDuration(300);
                    // 监听
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (mContainer != null && xuFloatView != null) {
                                mContainer.removeView(xuFloatView);
                            }
                        }
                    });
                    // 开始动画
                    objectAnimator.start();
                }
                xuFloatView = null;
            }
        });
        return this;
    }

    private void ensureMiniPlayer(Context context) {
        synchronized (this) {
            if (xuFloatView != null) {
                return;
            }
            xuFloatView = new FloatView(context.getApplicationContext(), mdata);
            xuFloatView.setLayoutParams(getParams());
            addViewToWindow(xuFloatView);
        }
    }

    @Override
    public XuFloatManager init() {
        ensureMiniPlayer(EnContext.get());
        return this;
    }

    @Override
    public XuFloatManager data(String... data) {
        this.mdata = data;// 第一次初始化
        // 用于更新数据
        if (xuFloatView != null) {
            xuFloatView.setupMusicData(data);
        }
        return this;
    }

    @Override
    public XuFloatManager attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    @Override
    public XuFloatManager attach(FrameLayout container) {
        if (container == null || xuFloatView == null) {
            mContainer = container;
            return this;
        }
        if (xuFloatView.getParent() == container) {
            return this;
        }
        if (mContainer != null && xuFloatView.getParent() == mContainer) {
            mContainer.removeView(xuFloatView);
        }
        mContainer = container;
        mContainer.addView(xuFloatView);
        return this;
    }

    @Override
    public XuFloatManager detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    @Override
    public XuFloatManager detach(FrameLayout container) {
        if (xuFloatView != null && container != null && ViewCompat.isAttachedToWindow(xuFloatView)) {
            container.removeView(xuFloatView);
        }
        if (mContainer == container) {
            mContainer = null;
        }
        return this;
    }

    @Override
    public FloatView getView() {
        return xuFloatView;
    }


    @Override
    public XuFloatManager layoutParams(ViewGroup.LayoutParams params) {
        if (xuFloatView != null) {
            xuFloatView.setLayoutParams(params);
        }
        return this;
    }

    @Override
    public XuFloatManager listener(FloatViewStateListener viewStateLIstener) {
        if (xuFloatView != null) {
            xuFloatView.setViewStateLIstener(viewStateLIstener);
        }
        return this;
    }

    private void addViewToWindow(final FloatView view) {
        if (mContainer == null) {
            return;
        }
        mContainer.addView(view);
    }

    @SuppressLint("RtlSetMargins")
    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.RIGHT;
        params.setMargins(params.leftMargin, params.topMargin, 0, 56);
        return params;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}