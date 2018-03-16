package com.carl_yang.stuart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyDialog;
import com.carl_yang.common.MyUtils;
import com.carl_yang.common.SharedPreferencesUtils;
import com.carl_yang.drawfile.tools.DrawAttribute;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.httpintent.domain.StuDoamin;
import com.carl_yang.httpintent.domain.UpVersionReturn;
import com.carl_yang.permission.PermissionsChecker;
import com.carl_yang.services.UpdateVersionService;
import com.carl_yang.uplib.UpVersions;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private static final int REQUEST_CODE = 0; // 请求码
    private TextView login;
    private EditText login_editvalue;
    private MyDialog dialog;
    private long firstTime = 0;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private PermissionsChecker mPermissionsChecker; // 动态权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ArtStudentApplication.getmInstance().addActivity(this);
        initParams();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPermissionsChecker=new PermissionsChecker(this);

        login_editvalue = (EditText) findViewById(R.id.login_editvalue);
        login_editvalue.setText(getUserId());
        login = (TextView) findViewById(R.id.login);
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        login.setBackground(getResources().getDrawable(R.drawable.student_bg2_h));
                        if (login_editvalue.getText().toString().equals("") || login_editvalue.getText().toString() == null) {
                            Toast.makeText(LoginActivity.this, "学号不能为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            String login_value=login_editvalue.getText().toString();
                            if(!login_value.equals(getUserId())){
                                writeUser("", "", "");
                            }
                            stuLogin(login_editvalue.getText().toString());
                        }
//                        Intent intentdr = new Intent(LoginActivity.this, DrawActivity.class);
//                        startActivity(intentdr);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        login.setBackground(getResources().getDrawable(R.drawable.student_bg2));
                        break;
                    case MotionEvent.ACTION_UP:
                        login.setBackground(getResources().getDrawable(R.drawable.student_bg2));
                        break;
                }

                return true;
            }
        });
        updateVersionJudge();

        if (!TextUtils.isEmpty(getUserId()) && !TextUtils.isEmpty(getCtrId())&& !TextUtils.isEmpty(getStuId())) {
            ArtStudentApplication.CTR_ID = getCtrId();
            ArtStudentApplication.STU_ID = getStuId();
            ArtStudentApplication.USER_ID=getUserId();
            Intent intentdr = new Intent(LoginActivity.this, DrawActivity.class);
            startActivity(intentdr);
        }
    }

    public void login_click(View v){
        switch (v.getId()){
            case R.id.login_relativelayout:
                InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                break;
            case R.id.login_systemset:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1500) {
                    firstTime = secondTime;
                } else {
                    Intent to_urlset= new Intent(LoginActivity.this,UrlSettingActivity.class);
                    startActivity(to_urlset);
                }
                break;
        }
    }

    private void initParams() {
        Common.init(getApplication());
        dialog= Common.initUploadingDialog(dialog, LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        String now_ip= SharedPreferencesUtils.getInstance("IP_SP",LoginActivity.this)
                .read("ip_value","");
        if(!now_ip.equals("")) {
            ArtStudentApplication.commonUrl = "http://" + now_ip + ":80/";
            ArtStudentApplication.upVersionUrl = "http://" + now_ip + ":8000/";
        }
    }

    private void stuLogin(String stuno) {
        dialog.show();
        dialog.changeInfo("登录中...");
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "login/doLogin")//
                .addParams("stu_no", stuno)//
                .build()//
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
            dialog.cancel();
            Log.e("HandTask", e+"");
        }

        @Override
        public void onResponse(String response, int id) {
            dialog.cancel();
            Log.e("HandTask", response);
            //{"success":1}
            StuDoamin stu = new Gson().fromJson(response, StuDoamin.class);
            String stu_success = stu.getSuccess();
            System.out.println("success value:" + stu_success);
            if(getCtrId().equals("")) {
                if (stu_success.equals("1")) {
                    Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    ArtStudentApplication.CTR_ID = stu.getStu().getCtr_id();
                    ArtStudentApplication.STU_ID = stu.getStu().getStu_id();
                    ArtStudentApplication.USER_ID=login_editvalue.getText().toString().trim();
                    writeUser(login_editvalue.getText().toString(), ArtStudentApplication.CTR_ID, ArtStudentApplication.STU_ID);
                    Intent intent = new Intent(LoginActivity.this, DrawActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else if (stu_success.equals("2")) {
                    Toast.makeText(LoginActivity.this, "用戶已登录!", Toast.LENGTH_SHORT).show();
                } else if (stu_success.equals("0")) {
                    Toast.makeText(LoginActivity.this, "用戶不存在!", Toast.LENGTH_SHORT).show();
                }
            }else{
                if (stu_success.equals("2")) {
                    Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    ArtStudentApplication.CTR_ID = getCtrId();
                    ArtStudentApplication.STU_ID = getStuId();
                    ArtStudentApplication.USER_ID=login_editvalue.getText().toString().trim();
                    Intent intent = new Intent(LoginActivity.this, DrawActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, "教师已下课!", Toast.LENGTH_SHORT).show();
                    writeUser("", "", "");
            }
            }
        }
    }

    private void writeUser(String userid, String ctrid, String stuid) {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("user_id", userid);
        edit.putString("ctr_id", ctrid);
        edit.putString("stu_id", stuid);
        edit.commit();
    }

    private String getUserId() {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        return shared.getString("user_id", "");
    }
    private String getCtrId() {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        return shared.getString("ctr_id", "");
    }
    private String getStuId() {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        return shared.getString("stu_id", "");
    }

    //判断是否需要自动升级
    public void updateVersionJudge(){
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "open/findTeachingVersion")//
                .addParams("name", Common.getVersion())//
                .addParams("type", "1")//
                .build()//
                .execute(new Callback<UpVersionReturn>() {

                    @Override
                    public UpVersionReturn parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        UpVersionReturn ur = new Gson().fromJson(string, UpVersionReturn.class);
                        return ur;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                    }

                    @Override
                    public void onResponse(UpVersionReturn response, int id) {
                        dialog.cancel();
                        String su = response.getSuccess().toString();
                        if (su.equals("0")) {
                        } else if (su.equals("1")) {
                        } else {
                            String message = response.getDesc().toString();
                            String url = response.getUrl().toString();
                            new UpVersions()
                                    .getInstance()
                                    .setTitle("检测到新版本")
                                    .setContent(message)
                                    .setDownloadUrl(ArtStudentApplication.upVersionUrl + url)
                                    .downAndUpApp(LoginActivity.this);
//                            service.showVersionDialog(ArtTeachiingApplication.imageUrlFront + url, "检测到新版本", message);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
