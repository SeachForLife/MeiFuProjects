package com.carl_yang.art;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.VideosFragment;

public class VideosActivity extends Activity {

    private ImageView videos_icon;
    private TextView videos_teachername;
    private Activity activity;

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        activity = this;

        ArtTeachiingApplication.getmInstance().addActivity(this);
        videos_icon= (ImageView) findViewById(R.id.videos_icon);
//        videos_icon.setImageBitmap(ArtTeachiingApplication.bitmap);
        videos_teachername= (TextView) findViewById(R.id.videos_teachername);
        videos_teachername.setText(ArtTeachiingApplication.teacher_name);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.videos_grid__fragment, new VideosFragment());
        ft.commit();
    }

    public void famous_click(View v) {
        switch (v.getId()) {
            case R.id.videos_mainback:
                this.finish();
                break;
        }
    }


}
