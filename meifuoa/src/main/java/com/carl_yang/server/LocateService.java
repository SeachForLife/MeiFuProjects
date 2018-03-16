package com.carl_yang.server;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.carl_yang.baidulocation.BaiduLocation;
import com.carl_yang.baidulocation.LocateCallBack;
import com.carl_yang.domain.CommonDomain;
import com.carl_yang.domain.PostionBean;
import com.carl_yang.domain.PostionHistoryBean;
import com.carl_yang.meifuoa.R;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.tools.Common;
import com.carl_yang.tools.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LocateService extends Service implements LocateCallBack {

    private static final int NOTIFICATION_ID = 1017;
    private MyBinder mBinder = new MyBinder();

    private BaiduLocation baiduLocation = null;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    private List<PostionHistoryBean> posList;
    private boolean sendStatu = true;//判断历史数据是否发送成功，来判断是否需要发送下一条数据
    private boolean sendWhile = false;//循环锁

    private List<PostionBean> postion_list;//记录在1MIN的时候的5个点
    private int count_num = 1;
    PostionBean pbs;

    public class MyBinder extends Binder {
        public LocateService getLocateService() {
            return LocateService.this;
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if (posList == null) posList = new ArrayList<>();
        postion_list = new ArrayList<>();
        count_num = 1;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        if (posList == null) posList = new ArrayList<>();
        //创建PowerManager对象
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        wakeLock.acquire();
        startForegroundCompat();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (baiduLocation == null) {
            baiduLocation = new BaiduLocation(getApplicationContext(),
                    LocateService.this, 1000);//一分钟上传一次GPS数据
        }
        baiduLocation.locationRequest();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        baiduLocation.stopLocation();
        baiduLocation = null;
        wakeLock.release();
        stopForeground(true);
        super.onDestroy();
    }

    private void startForegroundCompat() {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            startService(new Intent(this, InnerService.class));
        }
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            stopSelf();
        }

        @Override
        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    private static Notification fadeNotification(Context context) {
        Notification notification = new Notification();
        // 随便给一个icon，反正不会显示，只是假装自己是合法的Notification而已
        notification.icon = R.drawable.abc_ab_share_pack_mtrl_alpha;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        return notification;
    }

    @Override
    public void saveLocation(final BDLocation location) {
        if (count_num %12==0) {
            if (!(location.getLatitude() + "").equals("4.9E-324")) {
                PostionBean pb = new PostionBean();
                pb.setLatitude(location.getLatitude());
                pb.setLongitude(location.getLongitude());
                postion_list.add(pb);
            }
        }
        count_num++;
        if (count_num > 60) {
            count_num=1;
            double latSum = 0;
            double longSum = 0;
            for (int i = 0; i < postion_list.size(); i++) {
                System.out.println("----原始坐标-----"+postion_list.get(i).getLatitude()+":::"+postion_list.get(i).getLongitude());
                latSum += postion_list.get(i).getLatitude();
                longSum += postion_list.get(i).getLongitude();
            }
            double averLat = latSum / postion_list.size();
            double averLong = longSum / postion_list.size();
            List<Double> doList = new ArrayList<>();
            for (int i = 0; i < postion_list.size(); i++) {
                double a = Math.pow(Math.abs(postion_list.get(i).getLatitude() - averLat), 2);
                double b = Math.pow(Math.abs(postion_list.get(i).getLongitude() - averLong), 2);
                doList.add(a + b);
            }
            double min = 0;
            for (int i = 0; i < doList.size(); i++) {
                if (min == 0) {
                    min = doList.get(i);
                } else {
                    min = min < doList.get(i) ? min : doList.get(i);
                }
            }
            for (int i = 0; i < doList.size(); i++) {
                if (doList.get(i) == min) pbs = postion_list.get(i);
            }
            postion_list.clear();
            System.out.println("----最终计算的坐标-----"+pbs.getLatitude()+":::"+pbs.getLongitude());
            new Thread() {
                @Override
                public void run() {
                    SignedOn("0", pbs.getLongitude() + "", pbs.getLatitude() + "");
                }
            }.start();
        }

//        Log.i("location", location.getLatitude() + ":::" + location.getLongitude());
    }

    public synchronized void SignedOn(String type, String longitude, String latitude) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long curtime = System.currentTimeMillis();
        String time1 = sdf.format(new Date(curtime));
        PostionHistoryBean phb = new PostionHistoryBean();
        phb.setType(type);
        phb.setCustomer_id("");
        phb.setLongitude(longitude);
        phb.setLatitude(latitude);
        phb.setEmp_id(SharedPreferencesUtils.getInstance(getApplicationContext()).read("emp_id", ""));
        phb.setTime(time1);
        System.out.println("=========当前的list size=========：" + posList.size() + "::" + time1 + "::" + Common.getSimNo(getApplicationContext()));
        if (posList.size() == 0) {
            OkHttpUtils.post()
                    .url(meifuoaApplication.TMurl + "open/appTrajectory.addTrajectory.html")
                    .addParams("trajectory.type", type)
                    .addParams("trajectory.customer_id", "")
                    .addParams("trajectory.longitude", longitude)
                    .addParams("trajectory.latitude", latitude)
                    .addParams("trajectory.emp_id", SharedPreferencesUtils.getInstance(getApplicationContext()).read("emp_id", ""))
                    .addParams("time", time1)
                    .addParams("phoneCode", Common.getSimNo(getApplicationContext()))
                    .build()//
                    .execute(new MyStringCallback(phb));
        } else {
            sendStatu = true;
            sendWhile = true;
            if (posList.size() < 5) {
                posList.add(phb);
            } else {
                posList.remove(0);
                posList.add(phb);
            }
            while (posList.size() != 0) {
                if (sendWhile) {
                    sendWhile = false;
                    if (sendStatu) {
                        if(posList.size() != 0) {
                            OkHttpUtils.post()
                                    .url(meifuoaApplication.TMurl + "open/appTrajectory.addTrajectory.html")
                                    .addParams("trajectory.type", posList.get(0).getType())
                                    .addParams("trajectory.customer_id", "")
                                    .addParams("trajectory.longitude", posList.get(0).getLongitude())
                                    .addParams("trajectory.latitude", posList.get(0).getLatitude())
                                    .addParams("trajectory.emp_id", posList.get(0).getEmp_id())
                                    .addParams("time", posList.get(0).getTime())
                                    .addParams("phoneCode", Common.getSimNo(getApplicationContext()))
                                    .build()//
                                    .execute(new MyStringCallback(posList.get(0)));
                        }
                    } else {
                        //如果网格出问题直接退出大循环
                        break;
                    }
                }
            }
        }
    }

    private class MyStringCallback extends StringCallback {

        PostionHistoryBean phb;

        public MyStringCallback(PostionHistoryBean phb) {
            this.phb = phb;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (posList != null) {
                if (!posList.contains(phb)) {
                    if (posList.size() < 5) {
                        posList.add(phb);
                    } else {
                        posList.remove(0);
                        posList.add(phb);
                    }
                }
            }
            sendWhile = true;
            sendStatu = false;
            System.out.println("=========onError=========：");
            if(e!=null) {
                System.out.println("=========当前的onError=========："+e.getMessage()+posList.size());
            }
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("meifu_up", "back:" + response);
            CommonDomain cd = new Gson().fromJson(response, CommonDomain.class);
            String recess_su = cd.getSuccess();
            if (recess_su.equals("1")) {
                if (posList != null) {
                    if (posList.contains(phb)) {
                        posList.remove(phb);
                    }
                }
                sendStatu = true;
                if(!cd.getOnline_time().equals("")) {
                    Intent mIntent = new Intent(meifuoaApplication.Broadcase_name);
                    mIntent.putExtra("online_time", cd.getOnline_time().toString());
                    getApplicationContext().sendBroadcast(mIntent);
                }
                System.out.println("GPS数据上传");
            } else {
                String msg = cd.getMsg();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                if (posList != null) {
                    if (!posList.contains(phb)) {
                        if (posList.size() < 5) {
                            posList.add(phb);
                        } else {
                            posList.remove(0);
                            posList.add(phb);
                        }
                    }
                }
                sendStatu = false;
                System.out.println("=========当前的onResponse=========：" + posList.size());
            }
            sendWhile = true;
        }
    }

}
