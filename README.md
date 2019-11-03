###  FloatView 是一个应用内浮动按钮，本项目在[EnFloatingView](https://github.com/leotyndale/EnFloatingView) 的基础上扩展而来，算是EnFloatingView v2.0吧，使用方式一样。最近项目有这个需求，并且已经在项目中使用。 分享出来给有需要的朋友使用和学习。
* 应用内显示，无需申请任何权限
* 没有适配问题、对付产品狗的逆天要求神器(WindowManger 有权限问题、或者产品就不想让用户去授权 这种逆天操作)
* 支持拖拽
* 超出屏幕限制移动
* 可自动吸附到屏幕边缘

# 优化：
* 支持 拖拽事件、子view的点击事件区分
* 支持 自定义布局 收缩|展开 Menu菜单
* 增加状态监听
* 增加旋转动画

# 待补充
* 增加 拖拽过程中的布局更改样式
* 增加 播放音乐模块(后续重点)
* ...让这个项目更加有使用价值

# demo下载


# 使用方式
1. 在基类Activity（注意必须是基类Activity）中的onResume和onStop（或者安卓原生ActivityLifeCycle监听）中添加如下代码
  ```java  
  @Override
    protected void onResume() {
        super.onResume();
        XuFloatManager.get().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XuFloatManager.get().detach(this);
    }    
  ```
2. 显示浮动按钮
   
```java 

XuFloatManager.get().data("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2357722536,1561223771&fm=11&gp=0.jpg","血色冰封 - Novasonic - 单曲 - 网易云音乐")
                .init()
                .listener(mListener);
                
private FloatViewStateListener mListener = new FloatViewStateListener() {
        @Override
        public void onRemove() {
           // 删除 浮动按钮
            XuFloatManager.get().remove();
        }

        @Override
        public void onMusicPlayState(MusicPlayState state) {
            // TODO 
        }
    };
```
