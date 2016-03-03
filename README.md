# OkHttpLib
OkHttpLib 对网络请求okHttp的一个封装

okHttp优势是什么呢？
  1、支持SPDY,共享一个Socket来处理同一个服务器的所有请求
  2、如果SPDY不可用，则通过连接池来减少请求延时
  3、无缝的支持GZIP来减少数据流量
  4、缓存响应数据来减少重复的网络请求
注：SPDY（读作SPeeDy）是google开发的基于TCP的应用层协议，用以最小化网络延迟，提升网络速度，优化用户的网络使用体验。
SPDY不是一种用于替代HTTP的协议，而是对HTTP协议的增强。新协议的功能包括数据流的多路复用，请求优先级以及HTTP报头压缩。谷歌表示，
引入SPDY协议后，在实验室测试中页面加载速度比原先快64%。

封装的原因：
  okHttp使用起来不如Volley方便，okhttp的回调都是在工作线程中，所以如果在回调中操作view的话，就要自己转换到UI线程，非常不方便，
  所以要封装。
  
OkHttpLib使用
  在gradle中进行引用compile 'com.lixue.admin:okhttputils:1.0' 或者在libs下引用okhttpLib.jar

目前支持
  一般的get请求
  一般的post请求
  基于Http Post的文件上传（类似表单）
  文件下载/加载图片
  上传下载的进度回调
  支持session的保持
  支持自签名网站https的访问，提供方法设置下证书就行
  支持取消某个请求
  支持自定义Callback
  支持HEAD、DELETE、PATCH、PUT

用法示例可见samples项目里面的例子

此封装库需要注意的是，我们在平常编码中用的最多的是json转换为对象，或数组，或List对象，我们没有用Gson的，而是采用反射进行解析的
用法如下
[id]: C:\Users\Administrator\Desktop\QQ截图20160303155249.png "Title"

对应生成的users类要自己生成
[id]:C:\Users\Administrator\Desktop\QQ截图20160303155517.png



