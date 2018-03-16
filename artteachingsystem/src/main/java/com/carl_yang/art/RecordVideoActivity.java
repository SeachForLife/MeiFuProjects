package com.carl_yang.art;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.domain.CommonDomain;
import com.carl_yang.recordvideoabout.RecordUtils;
import com.carl_yang.recordvideoabout.widget.ButtonMoveRelativeLayout;
import com.carl_yang.recordvideoabout.widget.VideoCircleProgressView;
import com.carl_yang.util.Common;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class RecordVideoActivity extends AppCompatActivity {

    @BindView(R.id.recorda_surface_view)
    SurfaceView recordaSurfaceView;
    @BindView(R.id.recorda_switch)
    ImageView recordaSwitch;
    @BindView(R.id.recorda_buttonmove)
    ButtonMoveRelativeLayout recordaButtonmove;
    @BindView(R.id.recorda_progress)
    VideoCircleProgressView recordaProgress;
    @BindView(R.id.recorda_press_control)
    TextView recordaPressControl;
    @BindView(R.id.recorda_close)
    RelativeLayout recordaClose;
    @BindView(R.id.record_layout)
    RelativeLayout recordLayout;

    private RecordUtils mRecordUtils;
    private int mProgress;

    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        ButterKnife.bind(this);

        dialog= Common.initUploadingDialog(dialog, RecordVideoActivity.this);

        mRecordUtils=RecordUtils.Builder(this)
                .setRecorderType(RecordUtils.MEDIA_VIDEO)
                .setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))
                .setTargetName("test.mp4")
                .build();
        mRecordUtils.setSurfaceView(recordaSurfaceView);

        recordaButtonmove.getCancelLayout().setOnClickListener(cancelClick);
        recordaButtonmove.getSureLayout().setOnClickListener(sureClick);

        recordaProgress.setOnProgressEndListener(listener);
        recordaProgress.setCancel(true);
    }

    public void RecordVideo_Click(View v){
        switch (v.getId()){
            case R.id.recorda_switch:
                mRecordUtils.switchCamera();
                break;
            case R.id.recorda_close:
                this.finish();
                break;
            case R.id.recorda_press_control:
                if(recordaPressControl.getText().equals("开始")){
                    recordaPressControl.setText("停止");
                    mRecordUtils.record();
                    startView();
                }else{
                    recordaPressControl.setText("开始");
                    if (mProgress == 0) {
                        stopView(false);
                        return;
                    }
                    if (mProgress < 10) {
                        //时间太短不保存
                        mRecordUtils.stopRecordOrSave(false);
                        Toast.makeText(RecordVideoActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                        stopView(false);
                        return;
                    }
                    //停止录制
                    mRecordUtils.stopRecordOrSave(true);
                    stopView(true);
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        recordaProgress.setCancel(true);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            mRecordUtils.setAngle(0);
        }else{
            mRecordUtils.setAngle(90);
        }
    }

    VideoCircleProgressView.OnProgressEndListener listener = new VideoCircleProgressView.OnProgressEndListener() {
        @Override
        public void onProgressEndListener() {
            recordaPressControl.setText("开始");
            recordaProgress.setCancel(true);
            mRecordUtils.stopRecordOrSave(true);
            stopView(true);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    recordaProgress.setProgress(mProgress);
                    if (mRecordUtils.isRecording()) {
                        mProgress = mProgress + 1;
                        sendMessageDelayed(handler.obtainMessage(0), 900);
                    }
                    break;
            }
        }
    };

    private void startView(){
        startAnim();
        mProgress = 0;
        handler.removeMessages(0);
        handler.sendMessage(handler.obtainMessage(0));
    }

    private void stopView(boolean isSave){
        startAnim();
        recordaProgress.setCancel(true);
        mProgress = 0;
        handler.removeMessages(0);
        if(isSave) {
            recordLayout.setVisibility(View.GONE);
            recordaButtonmove.startAnim();
        }
    }

    private void startAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(recordaPressControl,"scaleX",1,0.5f),
                ObjectAnimator.ofFloat(recordaPressControl,"scaleY",1,0.5f),
                ObjectAnimator.ofFloat(recordaProgress,"scaleX",1,1.3f),
                ObjectAnimator.ofFloat(recordaProgress,"scaleY",1,1.3f)
        );
        set.setDuration(250).start();
        stopAnim();
    }

    private void stopAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(recordaPressControl,"scaleX",0.5f,1f),
                ObjectAnimator.ofFloat(recordaPressControl,"scaleY",0.5f,1f),
                ObjectAnimator.ofFloat(recordaProgress,"scaleX",1.3f,1f),
                ObjectAnimator.ofFloat(recordaProgress,"scaleY",1.3f,1f)
        );
        set.setDuration(250).start();
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recordaButtonmove.stopAnim();
            recordLayout.setVisibility(View.VISIBLE);
            mRecordUtils.deleteTargetFile();
        }
    };

    private View.OnClickListener sureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String path = mRecordUtils.getTargetFilePath();
//            Toast.makeText(RecordVideoActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
            recordaButtonmove.stopAnim();
            recordLayout.setVisibility(View.VISIBLE);
            upVideo();
        }
    };

    private void upVideo(){
        dialog.show();
        dialog.changeInfo("稍等...");
        File file=new File(mRecordUtils.getTargetFilePath());
        if(!file.exists()){
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("teacId",ArtTeachiingApplication.user_id);
        params.put("ctrId",ArtTeachiingApplication.CTR_ID);
        OkHttpUtils.post()
                .addFile("file","test.mp4",file)
                .url(ArtTeachiingApplication.localUrl + "open/uploadTeachingVideo")
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
            dialog.cancel();
//            Log.e("HandTask",e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            dialog.cancel();
            System.out.println("---------"+response);
            CommonDomain cd = new Gson().fromJson(response, CommonDomain.class);
            String stu_success = cd.getSuccess();
            if(stu_success.equals("1")){
                Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
                RecordVideoActivity.this.finish();
            }else{
                Toast.makeText(getApplicationContext(), "上传失败,请重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
