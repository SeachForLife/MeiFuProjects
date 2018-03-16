package com.carl_yang;

import android.app.Activity;
import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class ArtTeachiingApplication extends Application {

//    public static String commonUrl="http://192.168.1.181:8080/ART1.0/open/";//云
//    public static String localUrl="http://192.168.1.181:8080/ArtTeaching1.0/";//本地
//    public static String imageUrlFront="http://192.168.1.85:8000/SA360";//云图片地址
    public static String commonUrl="http://art.sa360.com.cn/open/";//云
//    public static String localUrl="http://10.221.203.68:8080/ArtTeaching/";//本地
//    public static String localUrl="http://192.168.1.114:8080/ArtTeaching1.0/";//本地
    public static String localUrl="http://192.168.1.88:80/";//本地
    public static String localUPversion="http://192.168.1.88:8000/";
    public static String imageUrlFront="http://file.sa360.com.cn:8000/ART";//云图片地址
    public static String imageUrl="";
    public static String teacher_name="";
    public static String school_id="";
    public static String user_id="";
    public static String CLASS_CHOOSE_ID="";//当前选择的班级ID
    public static String GRADE_CHOOSE_ID="";//当前选择的年级ID
    public static String CTR_ID="";//课堂主键
    public static int CTR_ALLTIME=50*60;//一堂课上课的时间规定 单位S
    public static boolean upVersionSta=false;
    public static List<Integer> GRID_EXIST=null;//当前教师可以看到的哪些年级的课件


    private List<Activity> activities = new ArrayList<Activity>();
    private static ArtTeachiingApplication mInstance;
    private RefWatcher refWatcher;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance=this;
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

//        refWatcher=LeakCanary.install(this);
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher() {
//        ArtTeachiingApplication application = (ArtTeachiingApplication) getApplicationContext();
        return mInstance.refWatcher;
    }

    public static ArtTeachiingApplication getmInstance() {
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
