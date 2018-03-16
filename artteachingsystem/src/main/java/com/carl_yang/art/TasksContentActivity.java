package com.carl_yang.art;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.art.resourcesfragment.BackHandledFragment;
import com.carl_yang.art.resourcesfragment.WebViewFragment;
import com.carl_yang.domain.LoginReturn;
import com.carl_yang.interfacepac.BackHandledInterface;
import com.carl_yang.util.Common;
import com.carl_yang.view.DrawView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class TasksContentActivity extends FragmentActivity implements BackHandledInterface {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private WebViewFragment wvFra;

    private TextView tasks_content_back;
    private TextView tasks_content_title;
    private TextView tasks_content_nouptask;
    private LinearLayout student_up_layout;

    String titleValue="";
    String ctr_ID="";
    String is_vote="";
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_content);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        dialog= Common.initUploadingDialog(dialog, TasksContentActivity.this);
        titleValue=getIntent().getStringExtra("ctr_name");
        ctr_ID=getIntent().getStringExtra("ctr_id");
        is_vote=getIntent().getStringExtra("is_vote");
        initParams();

        wvFra=new WebViewFragment(ArtTeachiingApplication.localUrl+"android/student-works.html?ctr_id="+ctr_ID);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.tasks_content_fragment, wvFra);
        ft.commit();
    }

    private void initParams(){
        tasks_content_back= (TextView) findViewById(R.id.tasks_content_back);
        tasks_content_title= (TextView) findViewById(R.id.tasks_content_title);
        tasks_content_nouptask= (TextView) findViewById(R.id.tasks_content_nouptask);
        student_up_layout= (LinearLayout) findViewById(R.id.student_up_layout);

        tasks_content_title.setText(titleValue);

        tasks_content_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wvFra.clearRealtimeRefresh();
                TasksContentActivity.this.finish();
            }
        });

        tasks_content_nouptask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wvFra.showJsFunction();
            }
        });

        student_up_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuVotes();
            }
        });
        registerBoradcastReceiver();
        if(is_vote.equals("0")){
            student_up_layout.setVisibility(View.VISIBLE);
        }else{
            student_up_layout.setVisibility(View.GONE);
        }
    }

    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_tasknum");
        registerReceiver(bBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver bBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String num=intent.getExtras().getString("num");
            tasks_content_nouptask.setText("未交作业学生("+num+")");
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(bBroadcastReceiver);
    }

    public void stuVotes() {
        dialog.show();
        dialog.changeInfo("稍等...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/updateClassRecordIsVote")//
                .addParams("ctr_id", ctr_ID)//
                .build()//
                .execute(new Callback<LoginReturn>() {
                    @Override
                    public LoginReturn parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        LoginReturn user = new Gson().fromJson(string, LoginReturn.class);
                        return user;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(LoginReturn response, int id) {
                        dialog.cancel();
                        if (response.getSuccess().equals("0")) {
                            Toast.makeText(getApplicationContext(), "哎呀，失败了!", Toast.LENGTH_SHORT).show();
                        } else {
                            student_up_layout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "学生们可以开始投票咯!", Toast.LENGTH_SHORT).show();
//                            student_up_layout.setClickable(false);
                        }
                    }

                });
    }

    private BackHandledFragment mBackHandedFragment;

    @Override
    public void onBackPressed() {
        wvFra.clearRealtimeRefresh();
        if(mBackHandedFragment == null||!mBackHandedFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
}
