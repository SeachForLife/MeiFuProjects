package com.carl_yang.js;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by carl_yang on 2017/3/29.
 */

public class JavaScriptinterface {

    Context context;
    Activity activity;

    public JavaScriptinterface(Context c, Activity activity){
        this.context =c;
        this.activity=activity;
    }

    @JavascriptInterface
    public void showToast(String str){
//        System.out.println("H5调用了toast");
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void closeCourseWare(){
//        System.out.println("关闭当当前页面");
        activity.finish();
    }
}


