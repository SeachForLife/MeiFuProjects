package com.carl_yang.meifuoa;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.carl_yang.baidulocation.BaiduLocation;
import com.carl_yang.baidulocation.LocateCallBack;
import com.carl_yang.domain.CommonDomain;
import com.carl_yang.domain.CustomerMessage;
import com.carl_yang.domain.SearchCustomerDoamin;
import com.carl_yang.domain.TrajcectoryStartAndEndMessage;
import com.carl_yang.domain.TrajectoryStartAndEndDomain;
import com.carl_yang.meifuoa.fragmentdetail.AddCunstomerFragment;
import com.carl_yang.meifuoa.fragmentdetail.SelectCustomerFragment;
import com.carl_yang.meifuoa.fragmentdetail.SignedOnFragment;
import com.carl_yang.meifuoa.fragmentdetail.TitleAddFragment;
import com.carl_yang.meifuoa.fragmentdetail.TitleSignoFragment;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.server.LocateService;
import com.carl_yang.tools.Common;
import com.carl_yang.tools.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;

public class MainMapActivity extends MeifuActivity implements LocateCallBack {

    private static final String TAG = "MainMapActivity";
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private BaiduLocation baiduLocation = null;
    private boolean isFirstLoc = true;//是否第一次定位判断
    private String signoed_type="0";//点击签到按钮状态 0未点击 1签到 2签退
    private String signoed_click = "0";//获取定位和签到并合锁
    private String add_customer_status="0";//点击保存按钮状态 0未点击 1点击
    private String add_customer_name="";//添加客户页面的企业名字
    private Intent service=null;


    private FragmentManager fm;
    private FragmentTransaction ft;
    private AddCunstomerFragment tafr;
    private SignedOnFragment soF;

    private AlertDialog dialog;

    private final static int LOCATION_PERMISSION=100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main_map);

        meifuoaApplication.getmInstance().addActivity(this);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                boolean result=true;
                Log.i(TAG,Common.isAllowMockLocation(MainMapActivity.this)+"------"+Common.isRoot());
                if(Common.isAllowMockLocation(MainMapActivity.this)||Common.isRoot()){
                    result=false;
                    try {
                        Thread.sleep(120*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    result=true;
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    service = new Intent(MainMapActivity.this, LocateService.class);
                    startService(service);
                    File mLogFile = new File(Environment.getExternalStorageDirectory() + "/Crash");
                    if(mLogFile.exists()&&mLogFile.isDirectory()){
                        System.out.println("文件夹存在");
                        if(mLogFile.listFiles().length!=0){
                            System.out.println("文件夹不为空");
                            File file_crash=mLogFile.listFiles()[0];
                            upCrashLog(file_crash);
                        }
                    }else{
                        System.out.println("文件夹NO存在");
                    }
                }
                return result;
            }

            protected void onPostExecute(Boolean re) {
                if(re){
                    Toast.makeText(getApplicationContext(), "定位服务已开启", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    if(!MainMapActivity.this.isFinishing()) {
                        dialog = new AlertDialog.Builder(MainMapActivity.this)
                                .setTitle("提示")
                                .setMessage("该设备开启模拟定位或ROOT权限，无法登陆该OA系统")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainMapActivity.this.finish();
                                    }
                                }).create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    }
                }
            }
        }.execute();

        mMapView = (MapView) findViewById(R.id.main_mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
        mMapView.showZoomControls(false);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        baiduLocation = new BaiduLocation(this,
                MainMapActivity.this, 0);
        baiduLocation.locationRequest();

        initParams();
    }

    private void initParams() {

        fm = getFragmentManager();
        ft = fm.beginTransaction();

        if(soF==null) {
            soF = new SignedOnFragment();
        }else{
            soF.refushUI();
        }
        ft.replace(R.id.main_map_fragment, soF);
        ft.replace(R.id.main_map_title, new TitleSignoFragment(MainMapActivity.this));
        ft.commitAllowingStateLoss();
        String customer_id=SharedPreferencesUtils.getInstance(MainMapActivity.this).read("customer_id","");
        if (!TextUtils.isEmpty(customer_id)) {
            GetCustomerStatus();
        }
    }


    public void main_map_click(View v) {
        switch (v.getId()) {
            case R.id.signoed_chooselayout:
                SearchCustomer();
                break;
            case R.id.signo_out:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (service != null) {
                                    stopService(service);
                                    service = null;
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setCancelable(false)
                        .show();

                break;
            case R.id.signo_add:
                baiduLocation.locationRequest();
                tafr=new AddCunstomerFragment();
                ft = fm.beginTransaction();
                ft.replace(R.id.main_map_fragment, tafr);
                ft.replace(R.id.main_map_title, new TitleAddFragment());
                ft.commitAllowingStateLoss();
                break;
            case R.id.title_add_back:
                ft = fm.beginTransaction();
                if(soF==null) {
                    soF = new SignedOnFragment();
                }else{
                    soF.refushUI();
                }
                ft.replace(R.id.main_map_fragment, soF);
                ft.replace(R.id.main_map_title, new TitleSignoFragment(MainMapActivity.this));
                ft.commitAllowingStateLoss();
                break;
            case R.id.select_ok:
                String customerid=SharedPreferencesUtils.getInstance(MainMapActivity.this).read("customer_id","");
                if(customerid.equals("")){
                    Toast.makeText(MainMapActivity.this, "必须选择一个企业!", Toast.LENGTH_SHORT).show();
                    break;
                }
                GetCustomerStatus();
                break;
            case R.id.signo_button:
                if (signoed_type.equals("0")) {
                    showProDialog();
                    Button sign_button = (Button) v.findViewById(R.id.signo_button);
                    String sign_button_value = sign_button.getText().toString();
                    if (sign_button_value.equals("签到")) {
                        signoed_type = "1";
                    } else {
                        signoed_type = "2";
                    }
                    baiduLocation.locationRequest();
                }
                break;
            case R.id.add_customer_save:
                EditText add_customer_newAdr= (EditText) tafr.getView().findViewById(R.id.add_customer_newAdr);
                if(add_customer_newAdr.getText().toString().equals("") || add_customer_newAdr.getText().toString() == null){
                    Toast.makeText(MainMapActivity.this, "企业名称不能为空!", Toast.LENGTH_SHORT).show();
                }else{
                    add_customer_status="1";
                    add_customer_name=add_customer_newAdr.getText().toString().trim();
                    baiduLocation.locationRequest();
                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        baiduLocation.stopLocation();
        if (service != null) {
            stopService(service);
            service = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        //每次Actiity 的变动 都要判断一次，emp_id的状态
        if (TextUtils.isEmpty(SharedPreferencesUtils.getInstance(MainMapActivity.this).read("emp_id",""))){
            Toast.makeText(getApplicationContext(), "数据异常，请重新登录!", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void saveLocation(BDLocation location) {
//        Log.e("-----------------", "成功定位:::" + location.getAddrStr());
        meifuoaApplication.current_customer_adr=location.getAddrStr();
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng point_first = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate up = MapStatusUpdateFactory.newLatLng(point_first);
            mBaiduMap.animateMapStatus(up);
        }

        if (!signoed_type.equals("0") && signoed_click.equals("0")) {
            signoed_click = "1";
            SignedOn(signoed_type,location.getLongitude()+"",location.getLatitude()+"");
        }

        if(add_customer_status.equals("1")) AddCustomer(add_customer_name,meifuoaApplication.current_customer_adr,
                location.getLongitude()+"",location.getLatitude()+"");
    }

    //查找客户列表
    public void SearchCustomer(){
        showProDialog();
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl+"open/appTrajectory.findCustomerList.html")
                .addParams("customer.emp_id", SharedPreferencesUtils.getInstance(MainMapActivity.this).read("emp_id",""))//
                .build()//
                .execute(new MyStringCallback("search"));
    }

    //获取当前客户的签到签退状态时间等
    public void GetCustomerStatus(){
        showProDialog();
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl+"open/appTrajectory.findTrajectoryStartAndEnd.html")
                .addParams("trajectory.emp_id", SharedPreferencesUtils.getInstance(MainMapActivity.this).read("emp_id",""))
                .addParams("trajectory.customer_id",SharedPreferencesUtils.getInstance(MainMapActivity.this).read("customer_id",""))
                .build()//
                .execute(new MyStringCallback("get"));
    }

    //签到
    public void SignedOn(String type,String longitude,String latitude){
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl+"open/appTrajectory.addTrajectory.html")
                .addParams("trajectory.type", type)
                .addParams("trajectory.customer_id",SharedPreferencesUtils.getInstance(MainMapActivity.this).read("customer_id",""))
                .addParams("trajectory.longitude", longitude)
                .addParams("trajectory.latitude",latitude)
                .addParams("trajectory.emp_id",SharedPreferencesUtils.getInstance(MainMapActivity.this).read("emp_id",""))
                .addParams("phoneCode", Common.getSimNo(this))
                .build()//
                .execute(new MyStringCallback("signoed"));
    }

    //添加客户
    public void AddCustomer(String CustomerName,String CustomerAdr,String longitude,String latitude){
        showProDialog();
//        Log.e("meifu_main", "SignedOn:"+meifuoaApplication.emp_id+"::"+meifuoaApplication.customer_id);
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl+"open/appTrajectory.addCustomer.html")
                .addParams("customer.customer_name", CustomerName)
                .addParams("customer.customer_address",CustomerAdr)
                .addParams("customer.longitude", longitude)
                .addParams("customer.latitude",latitude)
                .addParams("customer.emp_id",SharedPreferencesUtils.getInstance(MainMapActivity.this).read("emp_id",""))
                .build()//
                .execute(new MyStringCallback("addCustomer"));
    }
    //上传ANR日志
    public void upCrashLog(File file){
        OkHttpUtils.post()
                .addFile("file",file.getName(),file)
                .url(meifuoaApplication.TMurl+"open/logreport.uploadLog.html")
                .build()
                .execute(new MyStringCallback("anr"));
    }

    private class MyStringCallback extends StringCallback {

        String type;

        public MyStringCallback(String back_type){
            this.type=back_type;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            cancelProDialog();
            signoed_type = "0";
            signoed_click = "0";
        }

        @Override
        public void onResponse(String response, int id) {
            cancelProDialog();
            Log.i("meifu_login", "back:"+response);
            //{"success":1}
            if(type.equals("search")) {//获取用户列表处理
                SearchCustomerDoamin ld = new Gson().fromJson(response, SearchCustomerDoamin.class);
                String stu_success = ld.getSuccess();
                System.out.println("success value:" + stu_success);

                if (stu_success.equals("1")) {
                    meifuoaApplication.customer_list = new ArrayList<>();
                    for (int i = 0; i < ld.getList().size(); i++) {
                        CustomerMessage cm = new CustomerMessage();
                        cm.setCustomer_address(ld.getList().get(i).getCustomer_address().toString());
                        cm.setCustomer_name(ld.getList().get(i).getCustomer_name().toString());
                        cm.setCustomer_id(ld.getList().get(i).getCustomer_id().toString());
                        meifuoaApplication.customer_list.add(cm);
                    }
                    Toast.makeText(MainMapActivity.this, "获取信息成功!", Toast.LENGTH_SHORT).show();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.main_map_fragment, new SelectCustomerFragment());
                    ft.commitAllowingStateLoss();
                } else if (stu_success.equals("0")) {
                    String msg = ld.getMsg();
                    Toast.makeText(MainMapActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }else if(type.equals("get")){
                TrajectoryStartAndEndDomain tsd=new Gson().fromJson(response,TrajectoryStartAndEndDomain.class);
                String recevie_success=tsd.getSuccess();
                if(recevie_success.equals("0")){
                    Toast.makeText(MainMapActivity.this, "获取当前客户信息失败,请重试!", Toast.LENGTH_SHORT).show();
                }else{
                    meifuoaApplication.trajcetMessage_list=new ArrayList<>();
                    for(TrajcectoryStartAndEndMessage tsm:tsd.getList()){
                        meifuoaApplication.trajcetMessage_list.add(tsm);
                    }
                    ft = fm.beginTransaction();
                    if(soF==null) {
                        soF = new SignedOnFragment();
                    }else{
                        soF.refushUI();
                    }
                    ft.replace(R.id.main_map_fragment,soF );
                    ft.commitAllowingStateLoss();
                }
            }else if(type.equals("addCustomer")){
                add_customer_status="0";
                CommonDomain cd=new Gson().fromJson(response,CommonDomain.class);
                String recess_su=cd.getSuccess();
                if(recess_su.equals("0")){
                    Toast.makeText(MainMapActivity.this, "客户添加失败,请重试!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainMapActivity.this, "客户添加成功!", Toast.LENGTH_SHORT).show();
                    ft = fm.beginTransaction();
                    if(soF==null) {
                        soF = new SignedOnFragment();
                    }else{
                        soF.refushUI();
                    }
                    ft.replace(R.id.main_map_fragment, soF);
                    ft.replace(R.id.main_map_title, new TitleSignoFragment(MainMapActivity.this));
                    ft.commitAllowingStateLoss();
                }
            }else if(type.equals("anr")) {
                CommonDomain cd=new Gson().fromJson(response,CommonDomain.class);
                String recess_su=cd.getSuccess();
                if(recess_su.equals("1")){
                    Toast.makeText(MainMapActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    deleteFile();
                }else{
                    Toast.makeText(MainMapActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }else{
                Log.i("meifu_login", "signo back:"+response);
                CommonDomain cd=new Gson().fromJson(response,CommonDomain.class);
                String recess_su=cd.getSuccess();
                if(recess_su.equals("0")){
                    cancelProDialog();
                    String msg=cd.getMsg();
                    Toast.makeText(MainMapActivity.this, msg, Toast.LENGTH_SHORT).show();
                }else if(recess_su.equals("1")){
                    Toast.makeText(MainMapActivity.this, "打卡成功!", Toast.LENGTH_SHORT).show();
                    GetCustomerStatus();//成功后刷新页面
                }
                signoed_type = "0";
                signoed_click = "0";
            }
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

    /**
     * 删除ANR日志文件
     */
    private void deleteFile(){
        File mLogFile = new File(Environment.getExternalStorageDirectory() + "/Crash");
        if(mLogFile.exists()&&mLogFile.isDirectory()){
            File[] files = mLogFile.listFiles();
            for(File file:files){
                file.delete();
            }
        }
    }

}
