package com.carl_yang.art;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.domain.LoginReturn;
import com.carl_yang.domain.UpVersionReturn;
import com.carl_yang.drawabout.tools.DrawAttribute;
import com.carl_yang.permissions.PermissionsChecker;
import com.carl_yang.uplib.UpVersions;
import com.carl_yang.util.Common;
import com.carl_yang.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码
    @BindView(R.id.login_systemset)
    Button loginSystemset;
    @BindView(R.id.login_localurl)
    EditText loginLocalurl;
    @BindView(R.id.login_localurl_linear)
    LinearLayout loginLocalurlLinear;

    private TextView login;
    private EditText login_username;
    private EditText login_password;

    private MyDialog dialog;
    private long firstTime = 0;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private PermissionsChecker mPermissionsChecker; // 动态权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ArtTeachiingApplication.getmInstance().addActivity(this);
        dialog = Common.initUploadingDialog(dialog, LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        initParams();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPermissionsChecker = new PermissionsChecker(this);

        String now_ip= SharedPreferencesUtils.getInstance("IP_SP",LoginActivity.this)
                .read("ip_value","");
        if(!now_ip.equals("")){
//                ArtTeachiingApplication.localUrl="http://"+now_ip+":8080/ArtTeaching/";
            ArtTeachiingApplication.localUrl="http://"+now_ip+":80/";
            ArtTeachiingApplication.localUPversion="http://"+now_ip+":8000/";
        }

        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login = (TextView) findViewById(R.id.login);
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        login.setBackground(getResources().getDrawable(R.drawable.bg6_h));
                        if (login_username.getText().toString().equals("") || login_username.getText().toString() == null
                                || login_password.getText().toString().equals("") || login_password.getText().toString() == null) {
                            Toast.makeText(LoginActivity.this, "用户名密码不能为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            LoginJudge(login_username.getText().toString(), login_password.getText().toString());
                        }
//                        Intent intent =new Intent(LoginActivity.this,RecordVideoActivity.class);
//                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        login.setBackground(getResources().getDrawable(R.drawable.bg6));
                        break;
                    case MotionEvent.ACTION_UP:
                        login.setBackground(getResources().getDrawable(R.drawable.bg6));
                        break;
                }

                return true;
            }
        });

        updateVersionJudge();

        loginSystemset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1500) {
                    firstTime = secondTime;
                } else {
                    Intent to_urlset= new Intent(LoginActivity.this,UrlSettingActivity.class);
                    startActivity(to_urlset);
                }
            }
        });
    }

    public void login_click(View v) {
        switch (v.getId()) {
            case R.id.login_relativelayout:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                break;
            case R.id.login_updateversion:
                updateVersionJudge();
                break;
        }

    }

    private void initParams() {

        Common.init(getApplication());
        ArtTeachiingApplication.GRID_EXIST = new ArrayList<>();
    }

    public void LoginJudge(String username, String password) {
        dialog.show();
        dialog.changeInfo("登录中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "teachingLogin.login.html")//
                .addParams("ln", username)//
                .addParams("lp", password)//
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
                        System.out.println("访问失败");
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(LoginReturn response, int id) {
                        dialog.cancel();
                        System.out.println("6666" + response.getHasUser());
                        if (response.getHasUser().equals("0")) {
                            Toast.makeText(getApplicationContext(), "用户不存在!", Toast.LENGTH_SHORT).show();
                        } else if(response.getHasUser().equals("2")) {
                            Toast.makeText(getApplicationContext(), "密码错误!", Toast.LENGTH_SHORT).show();
                        }else {
                            System.out.println("================" + response.getUser().getPicurl() + "----" + response.getUser().getSchool_id().toString());
                            ArtTeachiingApplication.imageUrl = ArtTeachiingApplication.imageUrlFront + response.getUser().getPicurl().toString() + "s.jpg";
                            ArtTeachiingApplication.teacher_name = response.getUser().getCn_name().toString();
                            ArtTeachiingApplication.school_id = response.getUser().getSchool_id().toString();
                            ArtTeachiingApplication.user_id = response.getUser().getUser_id().toString();
                            Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }

                });
    }

    //判断是否需要自动升级
    public void updateVersionJudge() {
//        ArtTeachiingApplication.upVersionSta=false;
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/findTeachingVersion")//
                .addParams("name", Common.getVersion())//
                .addParams("type", "0")//
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
                                    .setDownloadUrl(ArtTeachiingApplication.localUPversion + url)
                                    .downAndUpApp(LoginActivity.this);
//                            service.showVersionDialog(ArtTeachiingApplication.imageUrlFront + url, "检测到新版本", message);
                        }
                    }
                });

//        VersionParams versionParams = null;
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_teachiing);
////        Log.e("Login", Common.getVersion());
//        HttpParams pa = new HttpParams();
//        pa.put("name", Common.getVersion());
//        pa.put("type", "0");
//        versionParams = new VersionParams()
//                .setRequestUrl(ArtTeachiingApplication.commonUrl + "teachingVersion.findTeachingVersion.html")
//                .setRequestParams(pa)
//                .setBitmapIcon(MyUtils.getBytes(bm))
//                .setRequestMethod(HttpRequestMethod.GET);
//        Intent intent = new Intent(LoginActivity.this, UpdateVersionService.class);
//        intent.putExtra(AVersionService.VERSION_PARAMS_KEY, versionParams);
//        LoginActivity.this.startService(intent);
//        while(!ArtTeachiingApplication.upVersionSta){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        dialog.cancel();
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
