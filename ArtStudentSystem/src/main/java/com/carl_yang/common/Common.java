package com.carl_yang.common;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;

import com.carl_yang.stuart.R;


/**
 * Created by carl_yang
 */
public class Common {

    private static Application CONTENT = null;

    /**
     * @param context
     */
    public static void init(Application context) {
        if (context != null && CONTENT == null)
            CONTENT = context;
    }

    //dialog设置
    public static MyDialog initUploadingDialog(MyDialog dialog, Activity ac) {
        dialog = new MyDialog(ac, R.style.MyDialog);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        || keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                } else {
                    return false; // 默认返回 false
                }
            }
        });
        return dialog;
    }

    public static String getVersionName() {

        PackageManager packageManager = CONTENT.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(CONTENT.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException swallow) {
            return "";
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = CONTENT.getPackageManager();
            PackageInfo info = manager.getPackageInfo(CONTENT.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.1";
        }
    }
}
