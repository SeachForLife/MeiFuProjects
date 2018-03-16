package com.carl_yang.art;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.util.MyUtils;
import com.carl_yang.util.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UrlSettingActivity extends Activity {

    @BindView(R.id.urlset_value)
    EditText urlsetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = 500 * w_screen / 1280;
        layoutParams.height = 239 * h_screen / 800;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        this.getWindow().setAttributes(layoutParams);

        String now_ip=SharedPreferencesUtils.getInstance("IP_SP",UrlSettingActivity.this)
                .read("ip_value","");
        if(!now_ip.equals("")) urlsetValue.setText(now_ip);
    }

    public void onSettingClick(View v) {
        switch (v.getId()) {
            case R.id.urlset_sure:
                if (urlsetValue.getText().toString().equals("") || urlsetValue.getText() == null){
                    Toast.makeText(UrlSettingActivity.this, R.string.url_null, Toast.LENGTH_SHORT).show();
                }else{
                    String url_value=urlsetValue.getText().toString().trim();
                    if(MyUtils.ipMatch(url_value)){
                        SharedPreferencesUtils.getInstance("IP_SP",UrlSettingActivity.this)
                                .write("ip_value",url_value);
                        ArtTeachiingApplication.localUrl="http://"+url_value+":80/";
                        ArtTeachiingApplication.localUPversion="http://"+url_value+":8000/";
                        Toast.makeText(UrlSettingActivity.this, "设定成功"+ArtTeachiingApplication.localUrl, Toast.LENGTH_SHORT).show();
                        this.finish();
                    }else{
                        Toast.makeText(UrlSettingActivity.this, R.string.url_error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
