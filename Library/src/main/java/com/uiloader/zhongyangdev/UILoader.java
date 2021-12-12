package com.uiloader.zhongyangdev;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @项目名称 UILoader
 * @类名 UILoader
 * @包名 com.uiloader.app
 * @创建时间 2021/12/11 15:15
 * @作者 钟阳
 * @描述 UI状态加载器
 */
abstract public class UILoader extends FrameLayout {

    private View mLoadingView, mErrorView, mEmptyView, mSuccessView;
    public UIState mCurrentState = UIState.NONE;//当前状态
    protected IRetryClickListener mIRetryClickListener;

    public UILoader(@NonNull Context context) {
        super(context, null);//重写构造方法
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);//重写构造方法
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        switchUIByState();//切换UI状态-当前为初始状态NONE
    }

    /**
     * 更新UI状态方法
     *
     * @param state UI状态
     */
    public void updState(UIState state) {
        mCurrentState = state;//将当前状态赋值
        new Handler().post(this::switchUIByState);//更新UI
    }

    /**
     * 根据当前状态切换UI
     */
    private void switchUIByState() {
        /*成功布局*/
        if (mSuccessView == null) {
            mSuccessView = loadSuccessView(this);//获取对应布局文件
            addView(mSuccessView);//添加到父布局
        }
        //设置是否显示
        mSuccessView.setVisibility(mCurrentState == UIState.SUCCESS ? VISIBLE : GONE);
        /*加载中*/
        if (mLoadingView == null) {
            mLoadingView = loadLoadingView();
            addView(mLoadingView);
        }
        mLoadingView.setVisibility(mCurrentState == UIState.LOADING ? VISIBLE : GONE);
        /*网络错误*/
        if (mErrorView == null) {
            mErrorView = loadErrorView();
            addView(mErrorView);
        }
        mErrorView.setVisibility(mCurrentState == UIState.NETWORK_ERROR ? VISIBLE : GONE);
        /*空数据*/
        if (mEmptyView == null) {
            mEmptyView = loadEmptyView();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility(mCurrentState == UIState.EMPTY ? VISIBLE : GONE);
    }

    /**
     * 加载成功布局文件，该方法子类必须实现
     *
     * @param container 父容器
     * @return
     */
    protected abstract View loadSuccessView(ViewGroup container);

    /**
     * 加载空数据布局
     *
     * @return
     */
    protected View loadEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_empty, this, false);
    }

    /**
     * 加载网络错误布局
     *
     * @return
     */
    protected View loadErrorView() {
        //加载错误布局文件
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.view_error, this, false);
        //找到重试按钮，为其设置点击事件
        errorView.findViewById(R.id.tv_errorView_retryBtn).setOnClickListener(v -> {
            if (mIRetryClickListener != null) {
                mIRetryClickListener.onRetryClick();
            }
        });
        return errorView;
    }

    /**
     * 加载加载中布局文件
     *
     * @return
     */
    protected View loadLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this, false);
    }

    /**
     * 重试点击方法
     *
     * @param listener 监听接口
     */
    public void setOnRetryClickListener(IRetryClickListener listener) {
        mIRetryClickListener = listener;
    }
}
