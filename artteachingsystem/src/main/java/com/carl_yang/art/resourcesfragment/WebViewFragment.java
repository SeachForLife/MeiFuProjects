package com.carl_yang.art.resourcesfragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.carl_yang.art.R;
import com.carl_yang.progress.HProgressBarLoading;
import com.carl_yang.webview.JavaScriptinterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends BackHandledFragment {

    private String URL;
    private WebView fragment_webview;
    private TextView fragment_center_badnet;
    private HProgressBarLoading fragment_top_progress;

    private boolean isContinue = false;
    private boolean isShow = true;

    /** 视频全屏参数 */
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    public WebViewFragment(){

    }

    @SuppressLint("ValidFragment")
    public WebViewFragment(String url) {
        // Required empty public constructor
        this.URL = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        fragment_webview = (WebView) view.findViewById(R.id.fragment_webview);
        fragment_center_badnet = (TextView) view.findViewById(R.id.fragment_center_badnet);
        fragment_top_progress = (HProgressBarLoading) view.findViewById(R.id.fragment_top_progress);

        initWebViewSetting();
        return view;
    }

    public void showJsFunction() {
        fragment_webview.loadUrl("javascript:stuWorks.showOrHide()");
    }

    //clear taskscontent js
    public void clearRealtimeRefresh() {
        fragment_webview.loadUrl("javascript:stuWorks.clearRealtimeRefresh()");
    }

    //clear taskjs
    public void clearTaskJs(){
        fragment_webview.loadUrl("javascript:classRecord.clearRealtimeRefresh()");
    }

    private void initWebViewSetting() {
        WebSettings s = fragment_webview.getSettings();
        s.setJavaScriptEnabled(true);

        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);

        s.setBuiltInZoomControls(false);
        s.setDomStorageEnabled(true);

        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        fragment_webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        s.setBlockNetworkImage(true);//提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
//        s.setAppCacheEnabled(true);
        fragment_webview.addJavascriptInterface(new JavaScriptinterface(getActivity(), getActivity()), "android");
        //正常网络流程
        fragment_webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                //如果没有网络直接跳出方法
//                if (!MyUtils.isNetworkAvailable(getActivity())) {
//                    return;
//                }
                //如果进度条隐藏则让它显示
                if (View.INVISIBLE == fragment_top_progress.getVisibility()) {
                    fragment_top_progress.setVisibility(View.VISIBLE);
                }
                //大于80的进度的时候,放慢速度加载,否则交给自己加载
                if (newProgress >= 80) {
                    //拦截webView自己的处理方式
                    if (isContinue) {
                        return;
                    }
                    fragment_top_progress.setCurProgress(100, 3000, new HProgressBarLoading.OnEndListener() {
                        @Override
                        public void onEnd() {
                            finishOperation(true);
                            isContinue = false;
                        }
                    });

                    isContinue = true;
                } else {
                    fragment_top_progress.setNormalProgress(newProgress);
                }
            }

            /**
             * 处理视频
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        });
        fragment_webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url_Turntable) {
                System.out.println("-------------" + url_Turntable);
                view.loadUrl(url_Turntable);
                return true;

            }

            //https的处理方式
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError
                    error) {
                System.out.println("-----https--");
                handler.proceed();
            }

            //错误页面的逻辑处理
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                System.out.println("-----error--");
                errorOperation();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                fragment_webview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("停止加载：" + url);
                fragment_webview.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                fragment_webview.setVisibility(View.VISIBLE);
            }
        });
        //开始加载
        fragment_webview.loadUrl(URL);
    }

    /**
     * 错误的时候进行的操作
     */

    private void errorOperation() {
        //隐藏webview
//        fragment_webview.setVisibility(View.INVISIBLE);

        isShow = false;
        if (View.INVISIBLE == fragment_top_progress.getVisibility()) {
            fragment_top_progress.setVisibility(View.VISIBLE);
        }
        //3.5s 加载 0->80 进度的加载 为了实现,特意调节长了事件
        fragment_top_progress.setCurProgress(80, 3500, new HProgressBarLoading.OnEndListener() {
            @Override
            public void onEnd() {
                //3.5s 加载 80->100 进度的加载
                fragment_top_progress.setCurProgress(100, 3500, new HProgressBarLoading.OnEndListener() {
                    @Override
                    public void onEnd() {
                        finishOperation(false);
                    }
                });
            }
        });
    }

    /**
     * 结束进行的操作
     */
    private void finishOperation(boolean flag) {
        //最后加载设置100进度
        fragment_top_progress.setNormalProgress(100);
        //显示网络异常布局
        fragment_center_badnet.setVisibility(flag ? View.INVISIBLE : View.VISIBLE);
        //点击重新连接网络
        fragment_center_badnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_center_badnet.setVisibility(View.INVISIBLE);
                //重新加载网页
                fragment_webview.reload();
            }
        });
        try {
            hideProgressWithAnim();
        } catch (Exception e) {

        }
    }

    /**
     * 隐藏加载对话框
     */
    private void hideProgressWithAnim() {
        AnimationSet animation = getDismissAnim(getActivity());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fragment_top_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fragment_top_progress.startAnimation(animation);
    }

    /**
     * 获取消失的动画
     *
     * @param context
     * @return
     */
    private AnimationSet getDismissAnim(Context context) {
        AnimationSet dismiss = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        dismiss.addAnimation(alpha);
        return dismiss;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment_webview.stopLoading();
        fragment_webview.destroy();
    }

    private int mOriginalSystemUiVisibility;
    private int mOriginalOrientation;

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        customView = view;
        mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        mOriginalOrientation = getActivity().getRequestedOrientation();

        customViewCallback = callback;

        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        decor.addView(customView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        decor.removeView(customView);
        customView = null;

        getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(mOriginalSystemUiVisibility);
        getActivity().setRequestedOrientation(mOriginalOrientation);
        customViewCallback.onCustomViewHidden();
        customViewCallback=null;
    }

    @Override
    public boolean onBackPressed() {
        System.out.println("调用webview中的back");
        if (customView != null) {
            hideCustomView();
        }else if (fragment_webview.canGoBack()) {
            fragment_webview.goBack();
            fragment_webview.destroy();
            return true;
        }
        return false;
    }
}
