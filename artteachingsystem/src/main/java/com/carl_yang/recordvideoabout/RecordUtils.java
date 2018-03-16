package com.carl_yang.recordvideoabout;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.carl_yang.recordvideoabout.camerahelper.CameraHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Loren Yang on 2018/1/9.
 */

public class RecordUtils implements SurfaceHolder.Callback {

    public static final int MEDIA_AUDIO = 0;
    public static final int MEDIA_VIDEO = 1;
    private Camera mCamera;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头(默认)
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private int previewWidth, previewHeight;
    private CamcorderProfile profile;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording;
    private File targetFile;
    private int angle ;//需要转动的角度

    private Context context;
    private int recorderType;
    private File targetDir;
    private String targetName;

    public int getAngle() {
        return angle;
    }

    public RecordUtils setAngle(int angle) {
        this.angle = angle;
        return this;
    }

    public RecordUtils(RecordUtilsBuilder rBuilder){
        this.context=rBuilder.context;
        this.recorderType=rBuilder.recorderType;
        this.targetDir=rBuilder.targetDir;
        this.targetName=rBuilder.targetName;
    }

    public static RecordUtilsBuilder Builder(Context context){
        return new RecordUtilsBuilder(context);
    }

    public static class RecordUtilsBuilder{

        private Context context;
        private int recorderType;
        private File targetDir;
        private String targetName;

        public RecordUtilsBuilder(Context context){
            this.context=context;
        }

        public RecordUtils build(){
            return new RecordUtils(this);
        }

        public RecordUtilsBuilder setRecorderType(int recorderType) {
            this.recorderType = recorderType;
            return this;
        }

        public RecordUtilsBuilder setTargetDir(File targetDir) {
            this.targetDir = targetDir;
            return this;
        }

        public RecordUtilsBuilder setTargetName(String targetName) {
            this.targetName = targetName;
            return this;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            releaseCamera();
        }
        if (mMediaRecorder != null) {
            releaseMediaRecorder();
        }
    }

    public void setSurfaceView(SurfaceView view) {
        this.mSurfaceView = view;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFixedSize(previewWidth, previewHeight);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    /**
     *  相机切换
     */
    public void switchCamera() {
        mCamera.stopPreview();//停掉原来摄像头的预览
        mCamera.release();//释放资源
        mCamera = null;//取消原来摄像头
        if(cameraPosition==1) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            cameraPosition=0;
        }else{
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            cameraPosition=1;
        }
        startPreView(mSurfaceHolder);
    }

    private void startPreView(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            cameraPosition=1;
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(angle);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
                List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
                Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                        mSupportedPreviewSizes, mSurfaceView.getWidth(), mSurfaceView.getHeight());
                previewWidth = optimalSize.width;
                previewHeight = optimalSize.height;
                parameters.setPreviewSize(previewWidth, previewHeight);
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                // 这里是重点，分辨率和比特率
                // 分辨率越大视频大小越大，比特率越大视频越清晰
                // 清晰度由比特率决定，视频尺寸和像素量由分辨率决定
                // 比特率越高越清晰（前提是分辨率保持不变），分辨率越大视频尺寸越大。
                profile.videoFrameWidth = optimalSize.width;
                profile.videoFrameHeight = optimalSize.height;
                // 这样设置 1080p的视频 大小在5M , 可根据自己需求调节
                profile.videoBitRate = 2 * optimalSize.width * optimalSize.height;
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
//                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startRecordThread() {
        if (prepareRecord()) {
            try {
                mMediaRecorder.start();
                isRecording = true;
            } catch (RuntimeException r) {
                releaseMediaRecorder();
            }
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getTargetFilePath() {
        return targetFile.getPath();
    }

    public void record() {
        if (isRecording) {
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                targetFile.delete();
            }
            releaseMediaRecorder();
            mCamera.lock();
            isRecording = false;
        } else {
            startRecordThread();
        }
    }

    private boolean prepareRecord() {
        try {
            mMediaRecorder = new MediaRecorder();
            if (recorderType == MEDIA_VIDEO) {
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mMediaRecorder.setProfile(profile);
                // 实际视屏录制后的方向
                if(cameraPosition == 0){
                    mMediaRecorder.setOrientationHint(270+angle);
                }else {
                    mMediaRecorder.setOrientationHint(angle);
                }

            } else if (recorderType == MEDIA_AUDIO) {
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            targetFile = new File(targetDir, targetName);
            mMediaRecorder.setOutputFile(targetFile.getPath());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MediaRecorder", "Exception prepareRecord: ");
            releaseMediaRecorder();
            return false;
        }
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("MediaRecorder", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("MediaRecorder", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     *   停止录制并确认是否保存
     */
    public void stopRecordOrSave(boolean save) {
        Log.d("Recorder", "stopRecordUnSave");
        if (isRecording) {
            isRecording = false;
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException r) {
                Log.d("Recorder", "RuntimeException: stop() is called immediately after start()");
                if(!save) deleteTargetFile();
            } finally {
                releaseMediaRecorder();
            }
            if(!save) deleteTargetFile();
        }
    }

    /**
     *  删除视频文件
     */
    public boolean deleteTargetFile() {
        if (targetFile.exists()) {
            return targetFile.delete();
        } else {
            return false;
        }
    }
}
