# [StateLayout](https://liangjingkanji.github.io/StateLayout/)

## 生命周期

  函数	                    描述
onEmpty	            showEmpty 时回调
onError	            showError 时回调
onContent	        showContent 时回调
onLoading	        showLoading 时回调
onRefresh	        showLoading 时回调, 一般在其中执行加载网络或异步任务的逻辑, 而不是加载视图
stateChangedHandler	完全接管缺省页状态变更时处理

每个StateLayout实例都可以设置单独的回调监听, 同时StateConfig可以设置全局的回调监听

> 你对缺省页有任何自定义的需求, 点击事件? 开始播放动画? 通过参数展示不同的错误页或者空页面? 都可以在这里判断!
show*()函数可以通过其参数Any传递任何对象到on*()生命周期回调中

监听缺省页显示
```
state.onRefresh {
    // 每次showLoading都会执行该回调
}
state.showLoading()

//链式调用
state.onRefresh {
    // 每次showLoading都会执行该回调
}.showLoading()

// 监听缺省页显示
state.onEmpty {
    findViewById<TextView>(R.id.msg).text = "空布局信息"
}
```

onRefresh和onLoading触发的条件一样, 但是他们的函数参数接收者不一样, 他们所代表的的作用也不同
- onRefresh 中常见处理异步任务(例如网络请求)
- onLoading 中常见处理的是加载视图/动


## 全局配置
```
StateConfig.apply {
    emptyLayout = R.layout.layout_empty // 配置全局的空布局
    errorLayout = R.layout.layout_error // 配置全局的错误布局
    loadingLayout = R.layout.layout_loading // 配置全局的加载中布局

    setRetryIds(R.id.msg) // 全局的重试Id

    onError {

    }
}
```


## 传递差异

```
state.showError(ResponseException(code = 403, msg = "请求参数错误"))
state.onError {
    if (it is ResponseException) {
        findViewById<TextView>.text = "错误码: ${it.code}, 错误信息: ${it.msg}"
    }
}
```


## 点击重试

点击重试原理是点击你传入的控件ID后自动调用showLoading(), 支持ERROR/EMPTY两种状态
```
state.setRetryIds(R.id.msg) // 会触发[onRefresh]
```

如果你对默认的点击重试不满意, 可以在生命周期方法中自己setOnClickListener来配置自动重试


## 新增状态

如果我们需要新增一种状态(例如错误分为网络/接口错误), 我们可以根据tag来在视图回调中进行处理(比如替换图片)

```
StateConfig.apply {
    onError {
        if (it is NetNetworkingException) {
            // 这里是为无网络异常展示不同图片, 当然你可以做其他处理, 比如addView或者setText
            findViewById<View>(R.id.iv_error).setImageResource(R.drawable.ic_networking_error)
        }
    }
}
```