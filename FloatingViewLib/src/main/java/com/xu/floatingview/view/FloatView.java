package com.xu.floatingview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xu.floatingview.R;
import com.xu.floatingview.listener.MusicPlayState;

public class FloatView extends FloatLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private String[] mData;
    private ObjectAnimator objectAnimator;

    private View mLayout;
    private ConstraintLayout rootLayoutCL;
    private ConstraintLayout contenCL;
    private FrameLayout closeFL;
    private TextView textTv;
    private ImageView iconIv;
    private int[] bgResId;

    public FloatView(@NonNull Context context, String... data) {
        super(context, null);
        mData = data;
        bgResId = new int[]{R.drawable.widget_float_button_left_bg,R.drawable.widget_float_button_right_bg,R.drawable.widget_float_button_touch_bg};
        mInflater = LayoutInflater.from(context);
        initFloatView();
        setDataToView();
    }

    // 默认初始右边
    private void initFloatView() {
        setLayoutParams(getRightParams());
        mLayout = mInflater.inflate(R.layout.widget_float_window, this);
        rootLayoutCL = findViewById(R.id.cl_root_layout);
        contenCL = findViewById(R.id.content);
        closeFL = findViewById(R.id.fl_close);
        textTv = findViewById(R.id.tv_text);
        iconIv = findViewById(R.id.icon);
        rootLayoutCL.setOnClickListener(this);
        contenCL.setOnClickListener(this);
        iconIv.setOnClickListener(this);
        closeFL.setOnClickListener(this);
        this.setOnClickListener(null);
        setRotateAnim(iconIv);
    }

    // 布局参数
    private FrameLayout.LayoutParams getLeftParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.LEFT;
        params.setMargins(0, params.topMargin, params.rightMargin, 56);
        return params;
    }

    private FrameLayout.LayoutParams getRightParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.RIGHT;
        params.setMargins(params.leftMargin, params.topMargin, 0, 56);
        return params;
    }

    // 设置icon旋转动画
    public void setRotateAnim(View view) {
        if (view == null) return;
        view.clearAnimation();
        objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f);//最好是0f到359f，0f和360f的位置是重复的
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(5000);
        objectAnimator.start();
    }

    // 停止动画
    public void stopRotateAnim() {
        if (objectAnimator != null) objectAnimator.cancel();
        setMusicPlayState(MusicPlayState.STOP);
    }

    // 开始动画
    public void startRotateAnim() {
        if (objectAnimator != null) objectAnimator.start();
        setMusicPlayState(MusicPlayState.PLAY);
    }

    // 删除退出
    protected void removeView() {
        if (viewStateLIstener != null)
            viewStateLIstener.onRemove();
    }

    @Override
    public void updateView() {
        // 视图贴在左边
        if (getLocation() == LEFT){
            setLocation(LEFT);
            setLayoutParams(getLeftParams());
            setBackground(LEFT);
            return;
        }
        // 视图贴在右边
        if (getLocation() == RIGHT){
            setLocation(RIGHT);
            setLayoutParams(getRightParams());
            setBackground(RIGHT);
            return;
        }

        // 手指拖动中UI变成暂未实现
        if (getLocation() == TOUCH){
            setBackground(TOUCH);
            return;
        }
    }

    // 切换背景
    private void setBackground(int orientation) {
        rootLayoutCL.setBackgroundResource(bgResId[orientation]);
    }

    // 处理布局 打开|折叠
    private void handleOpenOrExpansionLayout(){
        contenCL.setVisibility(contenCL.getVisibility() == VISIBLE ? GONE : VISIBLE);
        setOpen(contenCL.getVisibility() == VISIBLE ? true : false);
    }


    // 设置数据
    protected void setDataToView() {
        try {
            // 设置 icon旋转动画
            if (iconIv != null) {
                if (mData != null && mData.length > 0) {
                    String cardCover = mData[0];
                    if (!TextUtils.isEmpty(cardCover))
                        glideLoadCircleImage(getContext(), cardCover, iconIv);
                }
                setRotateAnim(iconIv);
            }
            if (mData == null || mData.length == 0 || textTv == null) return;
            String title = (mData != null && mData.length == 2) ? mData[1] : "伊相杰/谢东 - 我和我的祖国";
            textTv.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载圆形图片
    public static void glideLoadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .placeholder(R.drawable.icon_music)
                .error(R.drawable.icon_music)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(false);
        Glide.with(context).load(url).apply(mRequestOptions).into(imageView);
    }


    // 设置数据
    public void setupMusicData(String[] mData) {
        this.mData = mData;
        setDataToView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.icon || id == R.id.content || id == R.id.cl_root_layout) {
            handleOpenOrExpansionLayout();
            return;
        }
        if (id == R.id.fl_close) {
            removeView();
            return;
        }
    }

}
