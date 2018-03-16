package com.carl_yang.stuart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.httpintent.domain.StuDoamin;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class ExitDialogActivity extends Activity {

    private ImageView ex_dialog_imageview;
    private TextView ex_dialog_button;

    private int count_time=10;
    private boolean sta_result=true;
    private Handler handler =new Handler();

    private Integer[] timeId={
            R.drawable.ex_dialog_tm1,
            R.drawable.ex_dialog_tm1,R.drawable.ex_dialog_tm2,R.drawable.ex_dialog_tm3,
            R.drawable.ex_dialog_tm4,R.drawable.ex_dialog_tm5,R.drawable.ex_dialog_tm6,
            R.drawable.ex_dialog_tm7,R.drawable.ex_dialog_tm8,R.drawable.ex_dialog_tm9,
            R.drawable.ex_dialog_tm10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_dialog);

        DisplayMetrics dm=this.getResources().getDisplayMetrics();
        int w_screen=dm.widthPixels;
        int h_screen=dm.heightPixels;
        android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
        layoutParams.width=533*w_screen/1280;
        layoutParams.height=426*h_screen/800;
        this.getWindow().setAttributes(layoutParams);

        ex_dialog_imageview= (ImageView) findViewById(R.id.ex_dialog_imageview);
        ex_dialog_button= (TextView) findViewById(R.id.ex_dialog_button);
        ex_dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginOut();
            }
        });
    }

    private Runnable countRunable=new Runnable() {
        @Override
        public void run() {
            ex_dialog_imageview.setImageResource(timeId[count_time]);
            if(sta_result && count_time==0){
                sta_result=false;
                LoginOut();
            }
            if(count_time>0) count_time--;
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        handler.post(countRunable);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(countRunable);
    }

    private void LoginOut(){
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "login/exitLogin")//
                .addParams("stu_id", ArtStudentApplication.STU_ID)//
                .build()//
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("DrawActivity", response);
            //{"success":1}
            StuDoamin stu = new Gson().fromJson(response, StuDoamin.class);
            String stu_success = stu.getSuccess();
            if (stu_success.equals("1")) {
                clearSp();
                ExitDialogActivity.this.finish();
                ArtStudentApplication.getmInstance().onTerminate();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
        }
        return true;
    }

    private void clearSp() {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("user_id", "");
        edit.putString("ctr_id", "");
        edit.putString("stu_id", "");
        edit.commit();
    }
}
