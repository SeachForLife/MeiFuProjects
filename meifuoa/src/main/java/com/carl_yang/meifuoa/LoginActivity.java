package com.carl_yang.meifuoa;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.domain.LoginDoamin;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.permissions.PermissionsChecker;
import com.carl_yang.server.LocateService;
import com.carl_yang.tools.Common;
import com.carl_yang.tools.SharedPreferencesUtils;
import com.carl_yang.uplib.UpVersions;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author carl_yang
 */
public class LoginActivity extends MeifuActivity {

    /**请求码**/
    private static final int REQUEST_CODE = 0;

    private String TAG = "loginactivity";

    private EditText login_username;
    private EditText login_password;
    private TextView login_version;
    private UpVersions upVersions;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    /**动态权限检测器**/
    private PermissionsChecker mPermissionsChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        meifuoaApplication.getmInstance().addActivity(this);
        initParams();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPermissionsChecker=new PermissionsChecker(this);
    }

    /**
     *  构建控件
     */
    private void initParams() {

        login_version= (TextView) findViewById(R.id.login_version);
        login_version.setText("V"+Common.getVersion(getApplicationContext()));

        login_username = (EditText) findViewById(R.id.login_username);
        String username = getUserName();
        login_username.setText(username);
        login_password = (EditText) findViewById(R.id.login_password);
        login_password.setText(getPassWord());

        String empid = getEmpID();
        String dep = getDepartmentID();
        if (!TextUtils.isEmpty(empid) && !TextUtils.isEmpty(dep)) {
            // 给全局变量的userId 和userName赋值
            loginJudge(login_username.getText().toString(), login_password.getText().toString(),1);
        }
    }

    public void login_click(View v) {
        switch (v.getId()) {
            case R.id.login_enter:
                if (login_username.getText().toString().equals("") || login_password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    if (isNetAvilable()) {
                        loginJudge(login_username.getText().toString(), login_password.getText().toString(),0);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void writeUser(String userName, String passWord, String empId, String departmentId, String addCustomer) {
        SharedPreferencesUtils.getInstance(LoginActivity.this)
                .write("userName",userName)
                .write("passWord", passWord)
                .write("emp_id", empId)
                .write("department_id", departmentId)
                .write("add_customer",addCustomer);
    }

    private void writeCustomerSta(){
        SharedPreferencesUtils.getInstance(LoginActivity.this)
                .write("customer_id", "")
                .write("current_choose_customername", "");
    }

    private String getEmpID() {
        return SharedPreferencesUtils.getInstance(LoginActivity.this)
                .read("emp_id", "");
    }

    private String getDepartmentID() {
        return SharedPreferencesUtils.getInstance(LoginActivity.this)
                .read("department_id", "");
    }

    /**
     * 得到userName
     *
     * @return
     */
    private String getUserName() {
        return SharedPreferencesUtils.getInstance(LoginActivity.this)
                .read("userName", "");
    }

    /**
     * 得到passWord
     *
     * @return
     */
    private String getPassWord() {
        return SharedPreferencesUtils.getInstance(LoginActivity.this)
                .read("passWord", "");
    }

    public void loginJudge(String username, String password,int loginSta) {
        System.out.println("获取的SIMNNO:" + Common.getSimNo(this));
        showProDialog();
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl + "open/appLogin.login.html")
                .addParams("loginName", username)
                .addParams("loginPwd", password)
                .addParams("phoneCode", Common.getSimNo(this))
                .addParams("appVersion",Common.getVersion(this))
                .build()//
                .execute(new MyStringCallback(loginSta));
    }

    private class MyStringCallback extends StringCallback {

        int loginSta;

        private MyStringCallback(int loginSta){
            super();
            this.loginSta=loginSta;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            cancelProDialog();
            Toast.makeText(LoginActivity.this, "登录失败，请联系管理人员!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            cancelProDialog();
            Log.e("meifu_login", "back:" + response);
            //{"success":1}
            LoginDoamin ld = new Gson().fromJson(response, LoginDoamin.class);
            String stu_success = ld.getSuccess();
            System.out.println("success value:" + stu_success);
            if ("1".equals(stu_success)) {
                //初始化状态，改变到登录成功之后
                meifuoaApplication.trajcetMessage_list = null;
                meifuoaApplication.current_customer_adr = "";
                meifuoaApplication.customer_list = null;
                if(loginSta==0) {
                    writeCustomerSta();
                }
                writeUser(login_username.getText().toString(), login_password.getText().toString(), ld.getUser().getEmp_id()
                        , ld.getUser().getDepartment_id(), ld.getUser().getAdd_customer());
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                Intent intentMap = new Intent(LoginActivity.this, MainMapActivity.class);
                startActivity(intentMap);
            } else if (stu_success.equals("0")) {
                if(ld.getVersion_status()!=null){
                    if(ld.getVersion_status().equals("0")){
                        String last_version=ld.getVersion_code();
                        upVersions=new UpVersions()
                                .getInstance()
                                .setTitle("提示")
                                .setContent("有新版本需要更新！")
                                .setDownloadUrl(meifuoaApplication.TMurl+"APP/"+last_version+"/mfoa.apk")
                                .downAndUpApp(LoginActivity.this);
                    }else{
                        String msg = ld.getMsg();
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String msg = ld.getMsg();
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            try {
                Common.ignoreBatteryOptimization(LoginActivity.this);
            }catch(Exception e){
                Log.w(TAG,"电池优化白名单异常:"+e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    }
