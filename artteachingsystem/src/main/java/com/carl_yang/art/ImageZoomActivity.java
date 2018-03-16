package com.carl_yang.art;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.domain.PersonCollect;

public class ImageZoomActivity extends AppCompatActivity {


    private ImageView image_zoom_close;
    private PhotoView image_zoom_photoview;
    private TextView image_zoom_imagename;
    private TextView image_zoom_auth;
    private TextView image_zoom_desc;

    private PersonCollect pc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        ArtTeachiingApplication.getmInstance().addActivity(this);

        pc=getIntent().getParcelableExtra("image_mess");
        image_zoom_photoview= (PhotoView) findViewById(R.id.image_zoom_photoview);
        image_zoom_imagename= (TextView) findViewById(R.id.image_zoom_imagename);
        image_zoom_auth= (TextView) findViewById(R.id.image_zoom_auth);
        image_zoom_desc= (TextView) findViewById(R.id.image_zoom_desc);

//        Glide
//                .with(this)
//                .load(pc.getLarge_url())
//                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .crossFade()
//                .dontAnimate()
//                .into(image_zoom_photoview);

        image_zoom_imagename.setText(pc.getIamgename());
        image_zoom_auth.setText(pc.getAuth());
        image_zoom_desc.setText(pc.getContent());

        image_zoom_close= (ImageView) findViewById(R.id.image_zoom_close);
        image_zoom_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageZoomActivity.this.finish();
            }
        });

    }

}
