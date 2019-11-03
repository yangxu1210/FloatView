# FloatView

# FloatView 是一个应用内浮动按钮
* 应用内显示，无需申请任何权限
TimerTextview 用于倒计时显示，控件本身实现了时间的更新显示，如果TimerTextview使用在Listview item中倒计时结束后触发一个回调，来处理刷新数据更新列表 

# 简单使用
  ```java  
  TimerTextView tv = new TimerTextView(mContext);
  tv.removeRun();
  // tv.setRefreshDataCallback(new ParentRefreshDataCallback()) // 需要刷新时设置
  tv.setTime(countDownTime);//秒，可以自己扩展需要的时间处理    
  ```
  ![effect](https://raw.githubusercontent.com/yangxu1210/TimerTextview/master/TimerView/screenshot/effect.gif)
