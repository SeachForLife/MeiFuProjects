package com.carl_yang.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by carl_yang on 2017/5/27.
 */

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils mInstance;
    private Context mContext;
    private SharedPreferences Sp;
    private String Sp_name="Login";

    private SharedPreferencesUtils(Context context){
        this.mContext=context;
        Sp = context.getSharedPreferences(Sp_name,
                Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtils getInstance(Context context){
        if(mInstance==null){
            synchronized (SharedPreferencesUtils.class){
                if (mInstance == null){
                    mInstance = new SharedPreferencesUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    //往SP中写入数据
    public SharedPreferencesUtils write(String keyName,String value){
        SharedPreferences.Editor edit = Sp.edit();
        edit.putString(keyName, value);
        edit.commit();
        return this;
    }

    //SP读取数据
    public String read(String keyName,String nullBack){
        return Sp.getString(keyName, nullBack);
    }

}
