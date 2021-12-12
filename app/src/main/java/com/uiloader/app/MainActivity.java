package com.uiloader.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.uiloader.zhongyangdev.IRetryClickListener;
import com.uiloader.zhongyangdev.UILoader;
import com.uiloader.zhongyangdev.UIState;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private UILoader mUiLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*初始化UI加载器*/
        //1. 找到布局容器
        FrameLayout layoutContent = findViewById(R.id.layout_content);
        //2. 创建UILoader
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View loadSuccessView(ViewGroup container) {
                    //因为默认实现成功布局，此处需返回成功布局文件
                    return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_success, null);
                }

                @Override
                protected View loadLoadingView() {
                    return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_diy_loading, null);
                }
            };
            //3. 为避免重复添加，需要在此处进行判断
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
            //4. 最后添加到布局容器中
            layoutContent.addView(mUiLoader);
        }
        /* -- 测试 -- 设置默认UI状态为成功*/
        if (mUiLoader != null) {
            mUiLoader.updState(UIState.SUCCESS);
        }
        /*网络错误重试按钮点击事件*/
        mUiLoader.setOnRetryClickListener(new IRetryClickListener() {
            @Override
            public void onRetryClick() {
                Log.d(TAG, "点击网络错误重试按钮");
            }
        });
        /*设置按钮点击事件*/
        //成功按钮点击事件
        findViewById(R.id.btn_success).setOnClickListener(v -> {
            if (mUiLoader != null) {
                mUiLoader.updState(UIState.SUCCESS);
            }
        });
        //加载中按钮点击事件
        findViewById(R.id.btn_loading).setOnClickListener(v -> {
            if (mUiLoader != null) {
                mUiLoader.updState(UIState.LOADING);
            }
        });
        //网络错误按钮点击事件
        findViewById(R.id.btn_error).setOnClickListener(v -> {
            if (mUiLoader != null) {
                mUiLoader.updState(UIState.NETWORK_ERROR);
            }
        });
        //空数据按钮点击事件
        findViewById(R.id.btn_empty).setOnClickListener(v -> {
            if (mUiLoader != null) {
                mUiLoader.updState(UIState.EMPTY);
            }
        });
    }
}