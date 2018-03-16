package com.carl_yang.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by carl_yang on 2017/3/30.
 */

public class Common {

    /**
     * 判断当前网络状况
     * @param context
     * @return
     */
    public static boolean isNetAvilable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context // (不能定义成static的)
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable(); // 网络已打开
        }
        return false; // 网络未打开
    }

    /**
     * 获取本机手机号
     */
    public static String getPhoneNum(Context context){
        TelephonyManager tm= (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String pnum=tm.getLine1Number();
        return pnum;
    }

    /**
     * 获取设备的IMEI
     */
    public static String getSimNo(Context context){
        TelephonyManager tm= (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        return  tm.getDeviceId();
    }

    /**
     * 系统自带提示框
     */
    public static void AlertDialogShow(final Activity context, String title_str, String Message_str){
        new AlertDialog.Builder(context)
                .setTitle(title_str)
                .setMessage(Message_str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 判断手机是否root
     * @return
     */
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isCanExecute(binPath)) {
            return true;
            }
        if (new File(xBinPath).exists() && isCanExecute(xBinPath)) {
            return true;
            }
        return false;
    }
    private static boolean isCanExecute(String filePath) {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                return true;
                }
            } catch (IOException e) {
            e.printStackTrace();
            } finally {
            if (process != null) {
                process.destroy();
                }
            }
        return false;
    }

    /**
     * 判断手机是否开启了虚拟定位
     */
    public static boolean isAllowMockLocation(final Activity context) {
        boolean isOpen = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        Log.i("Common",Build.VERSION.SDK_INT+":::"+isOpen);
        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
    * 忽略电池优化
    */
    @TargetApi(Build.VERSION_CODES.M)
    public static void ignoreBatteryOptimization(Activity activity) {
        PowerManager powerManager = (PowerManager) activity.getSystemService(activity.POWER_SERVICE);
        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
        Log.w("XXXXXXX","yyyyyyyyyy:"+hasIgnored);
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if (!hasIgnored) {
            try {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }catch (ActivityNotFoundException a){

            }

        }
    }

}
