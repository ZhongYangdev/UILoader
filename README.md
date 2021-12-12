# UILoader

**一个安卓端的UI状态加载器**

**效果预览**
![example](gif\example.gif)

**添加依赖**

1. 在Project的build.gradle文件中添加maven仓库

```
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        ......
    }
}
```

2. 在settings.gradle文件中添加maven仓库

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
        jcenter() // Warning: this repository is going to shut down soon
    }
}
```

3. 在app的build.gradle文件中添加依赖

```
dependencies {
    ......
    
    //引用UILoader库
    implementation 'com.github.ZhongYangdev:UILoader:1.0.1'
}
```

**使用步骤**

1. 设置布局容器

```
<FrameLayout
    android:id="@+id/layout_content"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

2. 在*onCreate*方法中创建UILoader

```
//1. 找到布局容器
FrameLayout layoutContent = findViewById(R.id.layout_content);
//2. 创建UILoader
if (mUiLoader == null) {
    mUiLoader = new UILoader(this) {
        @Override
        protected View loadSuccessView(ViewGroup container) {
            //因为默认实现成功布局，此处需返回成功布局文件
            //此处需注意from中所传参数
            return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_success, null);
        }
    };
    //3. 为避免重复添加，需要在此处进行判断
    if (mUiLoader.getParent() instanceof ViewGroup) {
        ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
    }
    //4. 最后添加到布局容器中
    layoutContent.addView(mUiLoader);
}
```

3. 在对应位置改变UI状态

```
if (mUiLoader != null) {
    mUiLoader.updState(UIState.SUCCESS);
}
```

**重试按钮点击事件**

当请求数据网络错误时，会返回*NETWORK_ERROR*状态，此时需要设置重试按钮点击事件

```
mUiLoader.setOnRetryClickListener(new IRetryClickListener() {
    @Override
    public void onRetryClick() {
        Log.d(TAG, "点击网络错误重试按钮");
    }
});
```

**替换默认布局文件**

在创建UILoader时，复写对应的布局文件加载方法，返回自定义的布局文件即可。此处以加载中文件为例，其他状态依葫芦画瓢即可

```
mUiLoader = new UILoader(this) {
    @Override
    protected View loadSuccessView(ViewGroup container) {
    
        ......
        
        @Override
        protected View loadLoadingView() {
            return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_diy_loading, null);
        }
    }
};
```
