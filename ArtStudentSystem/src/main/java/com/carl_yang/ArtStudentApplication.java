package com.carl_yang;

import android.app.Activity;
import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class ArtStudentApplication extends Application {

    //    public static String commonUrl="http://192.168.1.181:8080/ArtTeaching1.0/";
//    public static String upVersionUrl="http://192.168.1.181:8080/ART1.0/open/";
//    public static String commonUrl = "http://192.168.1.114:8080/ArtTeaching1.0/";
//    public static String commonUrl="http://10.221.203.68:8080/ArtTeaching/";//本地
    public static String commonUrl = "http://192.168.1.88:80/";
//    public static String upVersionUrl = "http://art.sa360.com.cn/open/";
    public static String upVersionUrl = "http://192.168.1.88:8000/";
    public static String STU_ID = "";
    public static String CTR_ID = "";
    public static String USER_ID = "";
    public static boolean upVersionSta = false;

    private List<Activity> activities = new ArrayList<Activity>();
    private static ArtStudentApplication mInstance;


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

    }

    public static ArtStudentApplication getmInstance() {
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
}
