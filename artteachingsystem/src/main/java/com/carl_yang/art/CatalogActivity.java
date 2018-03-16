package com.carl_yang.art;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.domain.GradeCombo;
import com.carl_yang.domain.addClassRoomStudent;
import com.carl_yang.domain.findNoEndClassRecord;
import com.carl_yang.service.CountTimeService;
import com.carl_yang.util.Common;
import com.carl_yang.util.MyUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class CatalogActivity extends AppCompatActivity {

    private ImageView catalog_image;
    private TextView catalog_teacher_name;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ArtTeachiingApplication.getmInstance().addActivity(this);

        dialog= Common.initUploadingDialog(dialog, CatalogActivity.this);
        catalog_image= (ImageView) findViewById(R.id.catalog_image);
        catalog_teacher_name= (TextView) findViewById(R.id.catalog_teacher_name);
        catalog_teacher_name.setText(ArtTeachiingApplication.teacher_name);

        findNoEndClassRecord();
    }

    public void Click_Catalog(View v){
        switch (v.getId()){
            case R.id.catalog_draw:
                Intent intent =new Intent(CatalogActivity.this,DrawActivity.class);
                startActivity(intent);
                break;
            case R.id.catalog_look:
                Intent intentlook =new Intent(CatalogActivity.this,FamousAppreciationActivity.class);
                startActivity(intentlook);
                break;
            case R.id.catalog_favorite:
                Intent intent_fa =new Intent(CatalogActivity.this,FavoriteActivity.class);
                startActivity(intent_fa);
                break;
            case R.id.catalog_tasks:
                Intent intenttasks =new Intent(CatalogActivity.this,TasksActivity.class);
                startActivity(intenttasks);
                break;
            case R.id.catalog_exit:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginOut();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        isExist();
    }

    private long firstTime = 0;

    private void isExist() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 1500) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            this.finish();
        }

    }

    private void LoginOut(){
        dialog.show();
        dialog.changeInfo("退出...");
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
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(addClassRoomStudent response, int id) {
                        dialog.cancel();
                        if(response.getSuccess().equals("1")){
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }else{
                            Toast.makeText(getApplicationContext(), "退出失败，请重试!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void findNoEndClassRecord(){
        Log.d("=======-------======", "findNoEndClassRecord: "+ArtTeachiingApplication.user_id);
        dialog.show();
        dialog.changeInfo("稍等...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/findNoEndClassRecord")//
                .addParams("user_id", ArtTeachiingApplication.user_id)
                .build()
                .execute(new Callback<findNoEndClassRecord>() {
                    @Override
                    public findNoEndClassRecord parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        System.out.println("----"+string);
                        findNoEndClassRecord findNoClass = new Gson().fromJson(string, findNoEndClassRecord.class);
                        return findNoClass;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        CatalogActivity.this.finish();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(findNoEndClassRecord response, int id) {
                        dialog.cancel();
                        if(response.getIs_record().equals("1")){
                            if(response.getCrmap()!=null) {
                                ArtTeachiingApplication.CTR_ID = response.getCrmap().getCtr_id();
                                ArtTeachiingApplication.CLASS_CHOOSE_ID = response.getCrmap().getClass_id();
                                int dif_time = MyUtils.countTime(response.getCrmap().getCtr_date().toString(), response.getCrmap().getCurr_date().toString());
                                System.out.println("计算的值为:" + dif_time);
                                if (dif_time >= ArtTeachiingApplication.CTR_ALLTIME) {
                                    Toast.makeText(getApplicationContext(), "上课时间已经结束!", Toast.LENGTH_LONG).show();
                                    LoginOut();
                                } else {
                                    Intent service = new Intent(CatalogActivity.this, CountTimeService.class);
                                    service.setPackage(getPackageName());
                                    service.putExtra("dif_time", (ArtTeachiingApplication.CTR_ALLTIME - dif_time) + "");
                                    startService(service);
                                    findGradeCombo();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                            }
                        }else if(response.getIs_record().equals("2")){
                            Toast.makeText(getApplicationContext(), "服务异常!", Toast.LENGTH_LONG).show();
                            CatalogActivity.this.finish();
                        }else{
                            Intent intent=new Intent(CatalogActivity.this,ChooseGradeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void findGradeCombo() {
        dialog.show();
        dialog.changeInfo("年级获取中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "teachingCombo.findGradeCombo.html")//
                .addParams("school_id", ArtTeachiingApplication.school_id)//
                .build()//
                .execute(new Callback<GradeCombo>() {
                    @Override
                    public GradeCombo parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        System.out.println("=============="+string);
                        GradeCombo gradeCombo = new Gson().fromJson(string, GradeCombo.class);
                        return gradeCombo;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(GradeCombo response, int id) {
                        dialog.cancel();
//                        System.out.println("6666" + response.getList().get(0).getGrade_name());
                        ArtTeachiingApplication.GRID_EXIST.clear();
                        for(int i=0;i<response.getList().size();i++){
                            String gradeid=response.getList().get(i).getGrade_id().toString();
                            System.out.println("获取到的年级ID："+gradeid);
                            ArtTeachiingApplication.GRID_EXIST.add(Integer.valueOf(gradeid));
                        }
                    }
                });
    }
}
