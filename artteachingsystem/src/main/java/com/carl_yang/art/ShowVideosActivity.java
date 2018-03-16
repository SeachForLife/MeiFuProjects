package com.carl_yang.art;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.carl_yang.ArtTeachiingApplication;

import java.io.File;

public class ShowVideosActivity extends Activity {

    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        video= (VideoView) findViewById(R.id.video);
        MediaController controller = new MediaController(this);
        this.video.setMediaController(controller);
//        File file=new File(Environment.getExternalStorageDirectory()+"/Download/video1.mp4");
//        String videoPath=file.getAbsolutePath();
//        System.out.println("----"+videoPath);
//        video.setVideoPath(videoPath);
//        video.start();
//        System.out.println("======"+Uri.parse("Android.resource://"+ getPackageName() + "/"+R.raw.video1));
        video.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/"+R.raw.video1));
        video.start();
    }

    public void SV_Click(View v){
        switch (v.getId()){
            case R.id.show_video_back:
                this.finish();
                break;
        }
    }
}
