package com.carl_yang.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.CatalogActivity;
import com.carl_yang.art.CourseContentActivity;
import com.carl_yang.art.DrawActivity;
import com.carl_yang.art.NewWebVeiwActivity;
import com.carl_yang.art.PatternActivity;
import com.carl_yang.art.R;
import com.carl_yang.art.TasksActivity;
import com.carl_yang.art.TasksContentActivity;
import com.carl_yang.domain.CommonDomain;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by carl_yang on 2017/3/29.
 */

public class JavaScriptinterface {

    Context context;
    Activity activity;

    public JavaScriptinterface(Context c,Activity activity){
        this.context =c;
        this.activity=activity;
    }

    @JavascriptInterface
    public void showToast(String str){
        System.out.println("H5调用了toast");
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void closeCourseWare(){
        System.out.println("关闭当当前页面");
        activity.finish();
    }

    @JavascriptInterface
    public void switchToDraw(){
        Intent intent =new Intent(activity,DrawActivity.class);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void switchToTasks(){
        Intent intent =new Intent(activity,TasksActivity.class);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void switchToTasksStudent(String ctr_id,String ctr_name,String is_vote){
//        Log.e("javaScriptinterface:","获取到的值:"+ctr_id+":::"+ctr_name+"::"+is_vote);
        Intent intent =new Intent(activity,TasksContentActivity.class);
        intent.putExtra("ctr_id",ctr_id);
        intent.putExtra("ctr_name",ctr_name);
        intent.putExtra("is_vote",is_vote);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void noUpTaskStudent(String num){
        Intent mIntent = new Intent("update_tasknum");
        mIntent.putExtra("num",num);
        activity.sendBroadcast(mIntent);
    }

    @JavascriptInterface
    public void switchToPattern(){
        Intent intent =new Intent(activity,PatternActivity.class);
        intent.putExtra("url",ArtTeachiingApplication.localUrl);
        intent.putExtra("ctr_id",ArtTeachiingApplication.CTR_ID);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void switchH5ToOther(String url_end){
        System.out.println("--get the rul end--"+url_end);
        Intent intent =new Intent(activity,NewWebVeiwActivity.class);
        intent.putExtra("url",ArtTeachiingApplication.localUrl);
        intent.putExtra("ctr_id",ArtTeachiingApplication.CTR_ID);
        intent.putExtra("endUrl",url_end);
        activity.startActivity(intent);
    }

    /**
     * 博物馆和纹样JS接口
     * @param pageName
     */
    @JavascriptInterface
    public void patternIssued(String pageName){
        System.out.println("获取到额pagename"+"::"+pageName);
        intentPatternIssued(pageName);
    }

    /**
     * 访问纹样、博物馆下发接口
     * @param pageName
     */
    public void intentPatternIssued(String pageName) {
        System.out.println("教师点击了，纹样下发按钮!");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/updateShowPage")//
                .addParams("ctr_id", ArtTeachiingApplication.CTR_ID)//
                .addParams("page_name", pageName)//
                .build()//
                .execute(new Callback<CommonDomain>() {

                    @Override
                    public CommonDomain parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        CommonDomain ur = new Gson().fromJson(string, CommonDomain.class);
                        return ur;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context,R.string.pattern_issued_faild,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(CommonDomain response, int id) {
                        String su = response.getSuccess().toString();
                        if(su.equals("1")){
                            Toast.makeText(context,R.string.pattern_issued_success,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context,R.string.pattern_issued_faild,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}


