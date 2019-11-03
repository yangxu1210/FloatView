package com.xu.floatingview.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.xu.floatingview.listener.FloatViewStateListener;
import com.xu.floatingview.listener.MusicPlayState;
import com.xu.floatingview.utils.SystemUtils;


/**
 * FloatingMagnetView
 * 磁力吸附悬浮窗
 */
public class FloatLayout extends FrameLayout {
    protected static final String TAG = "floatingView";
    protected static final int MARGIN_EDGE = 0;// 边距因子
    protected float mOriginalRawX;
    protected float mOriginalRawY;
    protected float mOriginalX;
    protected float mOriginalY;
    protected static final int TOUCH_TIME_THRESHOLD = 100;
    protected long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mStatusBarHeight;
    protected boolean isOpen; // 展示|折叠标识
    protected boolean hasMove;// 拖动标识
    // 触摸坐标
    private float downXDispatch;
    private float downYDispatch;
    // 停靠默认位置 右边
    private int mDefaultLocation = RIGHT;
    // 悬浮球 坐落 左 右 标记
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOUCH = 2;

    public int getLocation() {
        return mDefaultLocation;
    }

    public void setLocation(int location) {
        this.mDefaultLocation = location;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
    protected FloatViewStateListener viewStateLIstener;
    protected MusicPlayState musicPlayState = MusicPlayState.PLAY;

    public void setMusicPlayState(MusicPlayState musicPlayState) {
        this.musicPlayState = musicPlayState;
    }

    public MusicPlayState getMusicPlayState() {
        return musicPlayState;
    }

    public void setViewStateLIstener(FloatViewStateListener viewStateLIstener) {
        this.viewStateLIstener = viewStateLIstener;
    }

    public FloatLayout(Context context) {
        this(context, null);
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMoveAnimator = new MoveAnimator();
        mStatusBarHeight = SystemUtils.getStatusBarHeight(getContext());
        setClickable(true);
        updateSize();
    }

    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        }
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    public void moveToEdge() {
        float moveDistance = isNearestLeft() ? MARGIN_EDGE : mScreenWidth - MARGIN_EDGE;
        mMoveAnimator.start(moveDistance, getY());
        // 拖动 贴边以后
        if (isNearestLeft()) {
            mDefaultLocation = LEFT;
            updateView();
        } else {
            mDefaultLocation = RIGHT;
            updateView();
        }
    }

    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        return getX() < middle;
    }

    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - getY()) * progress;
            move(deltaX, deltaY);
            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }

    }

    private void move(float deltaX, float deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    // 更新view
    public void updateView() {
        // TODO  这里更新UI操作
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {// 好像只在down的时候触发
        final float scale = getContext().getResources().getDisplayMetrics().density;// density=dpi/160
        int clickDistance = (int) (3 * scale + 0.5f);
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                hasMove = false;
                downXDispatch = event.getX();
                downYDispatch = event.getY();
               /* downXDispatch = event.getRawX();
                downYDispatch = event.getRawY();*/
                // Log.e(TAG, "onInterceptTouchEvent down->" + downYDispatch + "," + event.getY());
                changeOriginalTouchParams(event);
                updateSize();
                mMoveAnimator.stop();
                return false;//首先子类优先处理 点击事件，
            case MotionEvent.ACTION_MOVE:
                float distanceX = event.getX() - downXDispatch;// 表示触摸到哪里，但是 你滑动具体是多少是得转换的
                float distanceY = event.getY() - downYDispatch;
                if (hasMove == false && Math.abs(distanceX) <= clickDistance && Math.abs(distanceY) <= clickDistance) {
                    // Log.e(TAG, "onInterceptTouchEvent 点击事件，交给子类处理触摸距离x:" + distanceX + ",distancey:" + distanceY + ",点击距离:" + clickDistance + "," + ",downY:" + downYDispatch + ",downX:" + downXDispatch);
                    return false;

                } else {
                    // Log.e(TAG, "onInterceptTouchEvent 触摸事件，交给自己处理");
                    return true;//交给自己处理
                }

            case MotionEvent.ACTION_UP:
                if (hasMove) {
                    return false;
                }
                break;


        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeOriginalTouchParams(event);
                updateSize();
                mMoveAnimator.stop();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOpen) {
                    updateViewPosition(event);
                }
                // moveToEdge();
                break;
            case MotionEvent.ACTION_UP:
                 moveToEdge();
                break;
        }
        return super.onTouchEvent(event);
    }
}
