# floatView

# 交流qq群：123965382
floatView 是一个应用内全局浮动菜单按钮，支持

# 简单使用
  ```java  
  XuFloatManager.get().data("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2357722536,1561223771&fm=11&gp=0.jpg"
  ,"血色冰封 - Novasonic - 单曲 - 网易云音乐")
                .init()
                .listener(mListener);
  // 监听事件、可不设置
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
  ```
  ![effect](https://raw.githubusercontent.com/yangxu1210/TimerTextview/master/TimerView/screenshot/effect.gif)
