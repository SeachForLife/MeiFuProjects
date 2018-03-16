package com.carl_yang.meifuoa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CrashActivity extends AppCompatActivity {

    private static final String TAG = "CrashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.i("TAG", "enter the carsh UI");
        AlertDialog dl = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("Sorry，程序开了个小差。\n您可点击继续来使用！")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CrashActivity.this.finish();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }).create();
        dl.setCancelable(false);
        dl.show();
    }
}
