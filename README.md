# Higher-Ui
网易云 Android 高级UI Demo，包含 ui 高级绘制、屏幕适配，高级动画


## · NeRippleAnimator（仿网易云音乐：水波纹动画）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/NeRippleAnimator.gif)

### 实现步骤总结
1. 定义红色圆圈自定义 View
2. 定义水波纹动画类，定义属性红圈集合，给红圈 View 加入 scaleX、scaleY、alpha 属性动画集 AnimatorSet
3. 点击开启水波纹动画类

## · NetEaseDisc（仿网易云音乐：打碟界面）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/NetEaseDisc.gif)

### 实现步骤总结
1. 使用 ImageSwitcher 控件作为切换背景，加入高斯模糊效果，继承 ViewFlipper 的类作为碟片切换的控件
2. ViewFlipper 继承类中判断屏幕滑动距离，超过半宽切换上一首（或者下一首）
3. 通过接口回调、广播与服务进行播放、切换、暂停歌曲

## · NeMusicScreenAdapt（屏幕适配方案）
### 方案
1. 像素标准缩放：设定标准值（设计稿，如 1080P 的分辨率），根据横竖屏，获取实际屏幕与标准值的比例。然后有两种使用方法 —— ①比乘上给出的控件的大小则是适配后的大小，②自定义布局 onMeasure 里设置 LayoutParams 属性乘上比例
2. 像素密度缩放：设定标准值（设计稿，如 360dp ），再根据实际的屏幕像素密度与标准值计算出比例，获取程序的 DisplayMetrics 更改新的属性，即修改适配好的像素密度
3. 自定义百分比布局，重写 onMeasure 方法，对子 View 进行修改

## · NEMusicList（仿网易云音乐：音乐列表界面）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/NEMusicList.gif)

### 实现步骤总结
1. ListView 使用 addHeaderView 添加歌单信息的 View
2. 背景图片与 ToolBar 背景、播放栏添加高斯模糊与遮罩效果
3. ListView 滑动监听获取控件滑动高度差，计算高度比，更改相应控件的透明度与歌单标题

## · Vlayout（阿里 Vlayout）
阿里的 Vlayout demo，电商界面

## · SVGMap（SVG 使用 demo）
使用 SVG 解析中国地图，打造个性控件

## · MyRecyclerView（根据 RecyclerView 源码手写 RecyclerView）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/MyRecyclerView.gif)

### 实现步骤总结
1. 定义回收池类，保存 item 的 view
2. 定义 RecyclerView 类，继承 ViewGroup，摆放第一屏的 item
3. 判断滑动事件，当滑动距离超过一个 item 高度时，使用回收池去回收\获取 item 的 view ，刷新加载到屏幕上，永远保证屏幕显示一个屏幕高度的 item 数

## · ProgressBoat（自定义加载 View）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/ProgressBoat.gif)

### 实现步骤总结
1. 自定义 View 类，Path 画循环正弦曲线，然后闭合回路，Canvas 平移错位画出两个回路，形成波浪，invalidate() 刷新形成动画
2. 使用 PathMeasure 的 getPosTan() 方法获取波浪上的点，相对该点绘制小船，根据 getPosTan() 方法的参数计算点的切线值更改小船的角度，绘制进度文字
3. 在 View 的 TouchListener 里 MOVE 事件计算滑动更改进度值

## · Immersive（沉浸式 Demo）
实现沉浸式的 Demo，隐藏状态栏或状态栏透明、状态栏颜色

## · CustomRecyclerView（自定义 RecyclerView）
![示例动态图（加载失败需 VPN）](https://github.com/JackenLiu/Higher-Ui/blob/master/CustomRecyclerView.gif)

### 实现步骤总结
1. 定义 item 布局，在 activity 中定义和 item 布局中悬浮条一样的 view 布局
2. 监听 RecyclerView 滑动，当滑动下一个 item 到顶的高度小于悬浮条的高度时，移动悬浮条位置进行推进效果，而当下一个 item 到顶的高度大于悬浮条的高度时，悬浮条位置保持不变，即位置归 0 

## · Material_Design（Material Design Demo）
Material Design 使用例子

## · NetEaseAnimatorLib（动画框架 Demo）
模仿源码手写动画框架代码

## · NetEaseEventLib（事件分发 Demo）
模仿源码手写事件分发代码

## · app（高级 UI 各种知识点 Demo）
高级 UI 各种知识点，Paint、Canvas、Path、滤镜、贝塞尔曲线等等

