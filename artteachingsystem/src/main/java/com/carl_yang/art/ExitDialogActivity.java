package com.carl_yang.art;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.domain.addClassRoomStudent;
import com.carl_yang.service.CountTimeService;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Timer;

import okhttp3.Call;
import okhttp3.Response;

public class ExitDialogActivity extends Activity {

    private ImageView ex_dialog_imageview;
    private TextView  ex_dialog_button;

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
                        sta_result=true;
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(addClassRoomStudent response, int id) {
                        sta_result=true;
                        if(response.getSuccess().equals("1")){
                            ExitDialogActivity.this.finish();
                            ArtTeachiingApplication.getmInstance().onTerminate();
                        }else{
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
        }
        return true;
    }
}
