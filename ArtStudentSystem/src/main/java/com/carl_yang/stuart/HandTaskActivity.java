package com.carl_yang.stuart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyDialog;
import com.carl_yang.drawfile.tools.DrawAttribute;
import com.carl_yang.drawfile.tools.StorageInSDCard;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.httpintent.domain.StuDoamin;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class HandTaskActivity extends Activity {

    private String imagePaht = Environment.getExternalStorageDirectory()+"/test.jpg";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_task);

        ArtStudentApplication.getmInstance().addActivity(this);
        DisplayMetrics dm=this.getResources().getDisplayMetrics();
        int w_screen=dm.widthPixels;
        int h_screen=dm.heightPixels;
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = 418*w_screen/1280;
        layoutParams.height = 239*h_screen/800;
        layoutParams.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(layoutParams);
        dialog= Common.initUploadingDialog(dialog, HandTaskActivity.this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onHandTaskClick(View v) {
        switch (v.getId()) {
            case R.id.handtask_camera:
                useCamera();
                break;
            case R.id.handtask_paint:
                DrawActivity.drawView.saveBitmap();
                upImageJPG(ArtStudentApplication.STU_ID,"1");
                break;
            case R.id.handtask_cancel:
                this.finish();
                break;
        }
    }

    /**
     * 使用相机
     */
    private void useCamera() {
        File temp = new File(imagePaht);
        Uri imageUri = Uri.fromFile(temp);
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==Activity.RESULT_OK){
                System.out.println("拍照完成");
                upImageJPG(ArtStudentApplication.STU_ID,"0");
            }
        }
    }

    private void upImageJPG(String stuid,String scwtype){
        dialog.show();
        dialog.changeInfo("稍等...");
        File file=new File(imagePaht);
        if(!file.exists()){
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("stu_id",stuid);
        params.put("scw_type",scwtype);
        OkHttpUtils.post()
                .addFile("file","test.jpg",file)
                .url(ArtStudentApplication.commonUrl+"open/uploadWorks")
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback{

        @Override
        public void onError(Call call, Exception e, int id) {
            dialog.cancel();
//            Log.e("HandTask",e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            dialog.cancel();
            Log.e("HandTask",response);
            //{"success":1}
            StuDoamin stu = new Gson().fromJson(response, StuDoamin.class);
            String stu_success = stu.getSuccess();
            if(stu_success.equals("1")){
                Toast.makeText(getApplicationContext(), "作品上传成功!", Toast.LENGTH_SHORT).show();
                HandTaskActivity.this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "上传失败,请重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    }
