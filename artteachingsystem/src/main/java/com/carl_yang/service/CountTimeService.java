package com.carl_yang.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.ExitDialogActivity;
import com.carl_yang.art.LoginActivity;
import com.carl_yang.domain.addClassRoomStudent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class CountTimeService extends Service {

    private int dif_time=0;
    private int sta=0;
    Timer timer=null;

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("启动计时服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        dif_time= Integer.parseInt(intent.getStringExtra("dif_time").toString());
        System.out.println("获取的传值为:"+dif_time);
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(sta==0) {
                    dif_time--;
                    if (dif_time <= 0) {
                        sta=1;
                        Intent intent=new Intent(getBaseContext(),ExitDialogActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        stopSelf();
                        sta=0;
                    }
                }
            }
        },1000,1000);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(timer!=null) {
            timer.cancel();
            timer=null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void LoginOut(){
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "login/teacherExitLogin")//
                .addParams("ctr_id", ArtTeachiingApplication.CTR_ID)
                .build()
                .execute(new Callback<addClassRoomStudent>() {
                    @Override
                    public addClassRoomStudent parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        addClassRoomStudent addClass = new Gson().fromJson(string, addClassRoomStudent.class);
                        return addClass;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(addClassRoomStudent response, int id) {
                        if(response.getSuccess().equals("1")){
                            CountTimeService.this.onDestroy();
                            System.exit(1);
                        }else{
                            Toast.makeText(getApplicationContext(), "退出失败，请重试!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
