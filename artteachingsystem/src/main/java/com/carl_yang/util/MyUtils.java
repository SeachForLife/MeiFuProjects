package com.carl_yang.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;

import com.carl_yang.art.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtils {
    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }


    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author jiangqq
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络 2：wap网络3：net网络
     * @param context
     * @return
     */
    public static final int CMNET=3;
    public static final int CMWAP=2;
    public static final int WIFI=1;
    public static int getAPNType(Context context){
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType = CMNET;
            }
            else{
                netType = CMWAP;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }
        return netType;
    }

    public static int countTime(String ctr_time,String cur_time){
        int result=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1=df.parse(ctr_time);
            Date date2=df.parse(cur_time);
            long dif=date2.getTime()-date1.getTime();
            System.out.println("时间dif:"+dif);
            if(dif<=0){
                result=0;
            }else{
                result= (int) (dif/1000);
            }
        } catch (ParseException e) {
            System.out.println("时间异常"+e);
            e.printStackTrace();
        }
        return result;
    }

    //图片转换成byte[]
    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
    }

    /**
     * 改变字符串中的颜色
     * @param colorCode
     * @param str
     * @return
     */
    public static String color(String colorCode, String str) {
        return "<font color=\"" + colorCode + "\">" + str + "</font>";
    }

    /**
     * 正则判断IP
     */
    public static boolean ipMatch(String str){
        String url_ip_format = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//限定输入格式
        Pattern p=  Pattern.compile(url_ip_format);
        Matcher m=p.matcher(str);
        boolean b=m.matches();
        return b;
    }
}
