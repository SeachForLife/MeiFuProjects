package com.carl_yang;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.carl_yang.domain.CustomerMessage;
import com.carl_yang.domain.TrajcectoryStartAndEndMessage;
import com.carl_yang.meifuoa.CrashActivity;
import com.carl_yang.server.LocateService;
import com.carl_yang.tools.Common;
import com.carl_yang.tools.SharedPreferencesUtils;
import com.squareup.leakcanary.LeakCanary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

import static okhttp3.internal.Util.closeQuietly;

/**
 * Created by carl_yang on 2017/3/31.
 */

public class meifuoaApplication extends Application {

//        public static String TMurl = "http://192.168.1.88:8080/removework/";
//    public static String TMurl = "http://192.168.30.142:8080/removework/";
    public final static String TMurl = "http://115.28.37.10:8090/";

    private static final String TAG = "meifuoaApplication";
    private List<Activity> activities = new ArrayList<Activity>();
    private static meifuoaApplication mInstance;

    public static List<CustomerMessage> customer_list = null;//用来存储当前用户下有哪些客户
    public static List<TrajcectoryStartAndEndMessage> trajcetMessage_list = null;//用来存放当前用户下的状态
    public static String current_customer_adr = "";

    public static String Broadcase_name="onLineBroadcase";

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS",
            Locale.getDefault());
    private static final SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());

    private boolean ServiceSta=false;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        initHook();

        //注册一个系统级的广播，每隔1分钟监听下服务的状态
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);

//        LeakCanary.install(this);
    }

    private void initHook() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e("============", "程序崩溃" + e);
                String time1 = timeFormat1.format(Calendar.getInstance().getTime());
                String empid = SharedPreferencesUtils.getInstance(getApplicationContext())
                        .read("emp_id", "");
                File mLogFile = new File(Environment.getExternalStorageDirectory() + "/Crash/Crash-" + empid + "-" + time1 + ".txt");
                writeLog(mLogFile, "CrashHandler", e.getMessage(), e);
                Intent intent = new Intent(getApplicationContext(), CrashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onTerminate();
            }
        });
    }

    public synchronized void writeLog(File logFile, String tag, String message, Throwable tr) {

        logFile.getParentFile().mkdirs();

        if (!logFile.exists()) {

            try {
                logFile.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        String time = timeFormat.format(Calendar.getInstance().getTime());

        synchronized (logFile) {

            FileWriter fileWriter = null;

            BufferedWriter bufdWriter = null;

            PrintWriter printWriter = null;

            try {

                fileWriter = new FileWriter(logFile, true);

                bufdWriter = new BufferedWriter(fileWriter);

                printWriter = new PrintWriter(fileWriter);

                bufdWriter.append(time).append(" ").append("E").append('/').append(tag).append(" ").append("\n")
                        .append("IMEI:" + Common.getSimNo(getApplicationContext())).append("\n")
                        .append("Version:" + Common.getVersion(getApplicationContext())).append("\n")
                        .append(message).append('\n');

                bufdWriter.flush();

                tr.printStackTrace(printWriter);

                printWriter.flush();

                fileWriter.flush();

            } catch (IOException e) {

                closeQuietly(fileWriter);

                closeQuietly(bufdWriter);

                closeQuietly(printWriter);

            }

        }

    }

    public static meifuoaApplication getmInstance() {
        return mInstance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (serviceList == null || serviceList.size() == 0)
            return false;
        for (ActivityManager.RunningServiceInfo info : serviceList) {
            if (info.service.getClassName().equals(serviceClass.getName()))
                return true;
        }
        return false;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                if(!isServiceRunning(LocateService.class)){
                    Log.i(TAG,"服务关闭！");
                    if(ServiceSta) {
                        Intent mIntent = new Intent(context, LocateService.class);
                        context.startService(mIntent);
                    }
                }else{
                    ServiceSta=true;
                    Log.i(TAG,"服务正在运行！");
                }
            }
        }
    };
}
