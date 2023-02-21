# [gyf-dev/ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

> fork 1.8K star 10.7K

```
// 基础依赖包，必须要依赖
implementation 'com.geyifeng.immersionbar:immersionbar:3.2.2'
// kotlin扩展（可选）
implementation 'com.geyifeng.immersionbar:immersionbar-ktx:3.2.2'
```

## Api详解

1. 基础用法

```
ImmersionBar.with(this).init();
```

2. 高级用法(每个参数的意义)

```
ImmersionBar.with(this)
.transparentStatusBar()  //透明状态栏，不写默认透明色
.transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
.transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
.statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
.navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
.barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
.statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
.navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
.barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
.statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
.navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
.autoDarkModeEnable(true) //自动状态栏字体和导航栏图标变色，必须指定状态栏颜色和导航栏颜色才可以自动变色哦
.autoStatusBarDarkModeEnable(true,0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
.autoNavigationBarDarkModeEnable(true,0.2f) //自动导航栏图标变色，必须指定导航栏颜色才可以自动变色哦
.flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
.fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
.hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
.addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
.titleBar(view)    //解决状态栏和布局重叠问题，任选其一
.titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
.statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
.fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
.supportActionBar(true) //支持ActionBar使用
.statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
.navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
.barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
.removeSupportView(toolbar)  //移除指定view支持
.removeSupportAllView() //移除全部view支持
.navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
.navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.x手机导航栏颜色，默认为true
.navigationBarWithEMUI3Enable(true) //是否可以修改emui3.x手机导航栏颜色，默认为true
.keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
.setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调，keyboardEnable为true才会回调此方法
      @Override
      public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
          LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
      }
 })
.setOnNavigationBarListener(onNavigationBarListener) //导航栏显示隐藏监听，目前只支持华为和小米手机
.setOnBarListener(OnBarListener) //第一次调用和横竖屏切换都会触发，可以用来做刘海屏遮挡布局控件的问题
.addTag("tag")  //给以上设置的参数打标记
.getTag("tag")  //根据tag获得沉浸式参数
.reset()  //重置所以沉浸式参数
.init();  //必须调用方可应用以上所配置的参数
```

### 在Activity中实现沉浸式

java用法

```
ImmersionBar.with(this).init();
```

kotlin用法

```
 immersionBar {
     statusBarColor(R.color.colorPrimary) 
     navigationBarColor(R.color.colorPrimary)
 }
```

### 在Fragment中实现沉浸式

#### 在Fragment使用ImmersionBar

第一种，fragment如果配合viewpager2使用的话，并且使用了Behavior指定了BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT，
直接在fragment的onResume里直接实现沉浸式

第二种，当使用show()和hide()来控制Fragment显示隐藏的时候，
直接在fragment的onResume与onHiddenChanged（参数hidden为false）方法里实现沉浸式

#### 在Activity使用ImmersionBar

第一种，当结合viewpager2使用的时候，请使用viewpager2的registerOnPageChangeCallback的方法监听沉浸式
第二种，当结合viewpager使用的时候，请使用viewpager的addOnPageChangeListener的方法监听沉浸式，参考demo中FragmentThreeActivity这个类
第三种，当使用show()和hide()来控制Fragment显示隐藏的时候，请在tab切换的时候使用ImmersionBar，参考demo中FragmentFourActivity这个类

### 在Dialog中实现沉浸式，具体实现参考demo

1. 结合dialogFragment使用，可以参考demo中的BaseDialogFragment这个类

```
ImmersionBar.with(this).init();
```

2. 其他dialog，关闭dialog的时候必须调用销毁方法

```
ImmersionBar.with(this, dialog).init();
ImmersionBar.destroy(this, dialog);
```

### 在PopupWindow中实现沉浸式，具体实现参考demo

重点是调用以下方法，但是此方法会导致有导航栏的手机底部布局会被导航栏覆盖，还有底部输入框无法根据软键盘弹出而弹出，具体适配请参考demo。
```
popupWindow.setClippingEnabled(false);
```

更多细节问题直接看文档吧

## 混淆规则(proguard-rules.pro)

3.1.1以上版本无需混淆
3.0.0以下版本混淆规制
```
-keep class com.gyf.immersionbar.* {*;} 
-dontwarn com.gyf.immersionbar.**
```