package com.carl_yang.stuart;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyDialog;
import com.carl_yang.drawfile.tools.DrawAttribute;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.httpintent.domain.StuDoamin;
import com.carl_yang.property.DrawProperty;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.BRUSH;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.CRAYON;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.ERASER;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.GUN;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.PEN;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.PENCIL;
import static com.carl_yang.drawfile.tools.DrawAttribute.DrawStatus.SEAL;

public class DrawActivity extends Activity {

    private static final String TAG = "DrawActivity";
    @BindView(R.id.draw_notice)
    LinearLayout drawNotice;
    @BindView(R.id.draw_comment_close)
    ImageView drawCommentClose;
    @BindView(R.id.draw_comment_content)
    TextView drawCommentContent;
    @BindView(R.id.draw_comment)
    CardView drawComment;
    @BindView(R.id.draw_tools_pencil)
    ImageView drawToolsPencil;
    @BindView(R.id.draw_tools_pen)
    ImageView drawToolsPen;
    @BindView(R.id.draw_tools_crayon)
    ImageView drawToolsCrayon;
    @BindView(R.id.draw_tools_brush)
    ImageView drawToolsBrush;
    @BindView(R.id.draw_tools_gun)
    ImageView drawToolsGun;
    @BindView(R.id.draw_tools_seal)
    ImageView drawToolsSeal;
    @BindView(R.id.draw_tools_eraser)
    ImageView drawToolsEraser;
    @BindView(R.id.draw_tools_layout)
    LinearLayout drawToolsLayout;
    @BindView(R.id.draw_more_layout)
    LinearLayout drawMoreLayout;
    private ImageView draw_sharing;
    private ImageView draw_water;
    private ImageView draw_caryon;
    private ImageView draw_brush;
    private ImageView draw_rubber;
    private ImageView draw_stamp;
    private SeekBar draw_seekBar;
    private TextView test;
    private ImageView draw_color_image;
    private ImageView draw_undo_image;
    private ImageView draw_redo_image;
    private ImageView draw_del_image;

    //当前释放可以提交作业状态
    private String is_submit_works = "1";
    private int Left_Postion = 0;//定位画笔工具未选中状态的左距离

    public static DrawView drawView;
    public static Context context;

    private AnimatorSet mDrawNoticeAni;
    private AnimatorSet mCommentNoticeAni;

    private MyDialog dialog;

    private Integer[] samllImageIds = {
            R.drawable.color0, R.drawable.color1, R.drawable.color2, R.drawable.color3,
            R.drawable.color4, R.drawable.color5, R.drawable.color6, R.drawable.color7,
            R.drawable.color8, R.drawable.color9, R.drawable.color10, R.drawable.color11,
            R.drawable.color12, R.drawable.color13, R.drawable.color14, R.drawable.color15,
            R.drawable.color16, R.drawable.color17
    };

    private Integer[] mBackgroundIds = {
            R.mipmap.bg_image1, R.mipmap.bg_image2, R.mipmap.bg_image3, R.mipmap.bg_image4,
            R.mipmap.bg_image5, R.mipmap.bg_image6, R.mipmap.bg_image7, R.mipmap.bg_image8,
            R.mipmap.bg_image9, R.mipmap.bg_image10, R.mipmap.bg_image11, R.mipmap.bg_image12,
            R.mipmap.bg_image13, R.mipmap.bg_image14, R.mipmap.bg_image15, R.mipmap.bg_image16,
            R.mipmap.bg_image17, R.mipmap.bg_image18, R.mipmap.bg_image19, R.mipmap.bg_image20,
            R.mipmap.bg_image21, R.mipmap.bg_image22, R.mipmap.bg_image23, R.mipmap.bg_image24
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        DrawAttribute.screenWidth = dm.widthPixels;
        DrawAttribute.screenHeight = dm.heightPixels;
        DrawAttribute.paint.setColor(Color.LTGRAY);
        DrawAttribute.paint.setStrokeWidth(3);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);

        ArtStudentApplication.getmInstance().addActivity(this);
        dialog = Common.initUploadingDialog(dialog, DrawActivity.this);
        context = this;
        initParams();
    }

    private void initParams() {
        //进入画板后，将所有状态进行初始化
        DrawProperty.BRUSH_SIZE = 30;
        DrawProperty.COLOR_CHOOSE = Color.GRAY;
        DrawProperty.BUTTON_CHOOSE = DrawAttribute.DrawStatus.PENCIL;
        DrawProperty.STAMP_ID = R.drawable.yz1;
        DrawProperty.COLOR_CURRENT = 0;
        test = (TextView) findViewById(R.id.test);
        registerBoradcastReceiver();
        drawView = (DrawView) findViewById(R.id.draw_canvas);
        draw_sharing = (ImageView) findViewById(R.id.draw_sharing);
        draw_water = (ImageView) findViewById(R.id.draw_water);
        draw_caryon = (ImageView) findViewById(R.id.draw_caryon);
        draw_brush = (ImageView) findViewById(R.id.draw_brush);
        draw_rubber = (ImageView) findViewById(R.id.draw_rubber);
        draw_stamp = (ImageView) findViewById(R.id.draw_stamp);
        draw_seekBar = (SeekBar) findViewById(R.id.draw_seekBar);
        draw_color_image = (ImageView) findViewById(R.id.draw_color_image);
        draw_undo_image = (ImageView) findViewById(R.id.draw_undo_image);
        draw_redo_image = (ImageView) findViewById(R.id.draw_redo_image);

        draw_seekBar.setMax(40);
        draw_seekBar.setProgress(20);
        draw_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int size = seekBar.getProgress() + 10;
                DrawProperty.BRUSH_SIZE = size;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void draw_Click(View v) {
        switch (v.getId()) {
            case R.id.draw_back://退出
//                unregisterReceiver(mBroadcastReceiver);
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Exit();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.draw_handwork://提交作业
                if (is_submit_works.equals("1")) {
                    Intent intent_handtask = new Intent(DrawActivity.this, HandTaskActivity.class);
                    startActivity(intent_handtask);
                } else {
                    Toast.makeText(DrawActivity.this, "您的作业已经上传过了!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.draw_comment_button:
                drawMoreLayout.setVisibility(View.GONE);
                Intent intent_commnet = new Intent(DrawActivity.this, TeacherCommentActivity.class);
                startActivity(intent_commnet);
                break;
//            case R.id.draw_water://水彩笔
//                draw_water.setBackground(getResources().getDrawable(R.drawable.student_l_icon0_h));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.student_l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.student_l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.student_l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.student_l_icon4));
//                drawView.setBrushBitmap(DrawAttribute.DrawStatus.CASUAL_WATER);
//                DrawView.BUTTON_CHOOSE = DrawAttribute.DrawStatus.CASUAL_WATER;
//                break;
//            case R.id.draw_caryon://蜡笔
//                draw_water.setBackground(getResources().getDrawable(R.drawable.student_l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.student_l_icon1_h));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.student_l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.student_l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.student_l_icon4));
//                drawView.setBrushBitmap(DrawAttribute.DrawStatus.CASUAL_CRAYON);
//                DrawView.BUTTON_CHOOSE = DrawAttribute.DrawStatus.CASUAL_CRAYON;
//                break;
//            case R.id.draw_brush://刷子
//                draw_water.setBackground(getResources().getDrawable(R.drawable.student_l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.student_l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.student_l_icon2_h));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.student_l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.student_l_icon4));
//                drawView.setBrushBitmap(DrawAttribute.DrawStatus.CASUAL_COLOR);
//                DrawView.BUTTON_CHOOSE = DrawAttribute.DrawStatus.CASUAL_COLOR;
//                break;
//            case R.id.draw_rubber://橡皮
//                draw_water.setBackground(getResources().getDrawable(R.drawable.student_l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.student_l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.student_l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.student_l_icon3_h));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.student_l_icon4));
//                drawView.setBrushBitmap(ERASER);
//                DrawView.BUTTON_CHOOSE = ERASER;
//                break;
//            case R.id.draw_stamp://印章
//                Intent intent_stamppick = new Intent(DrawActivity.this, StampPickerActivity.class);
//                startActivity(intent_stamppick);
//                break;
            case R.id.draw_color_layout://颜色选择
                Intent intent_colorpick = new Intent(DrawActivity.this, ColorPickerActivity.class);
                startActivity(intent_colorpick);
                break;
            case R.id.draw_redo_layout://下一步
                drawView.redo();
                break;
            case R.id.draw_undo_layout://上一步
                drawView.undo();
                break;
            case R.id.draw_del_layout://删除
                drawView.cleanPaintBitmap();
                Bitmap backgroundBitmap = DrawAttribute.getImageFromAssetsFile(context, "bigpaper00.jpg", true);
                drawView.setBackgroundBitmap(backgroundBitmap, true);
                break;
            case R.id.draw_comment_close:
                commentOut();
                break;
            case R.id.draw_tools_pencil:
                if (!DrawProperty.BUTTON_CHOOSE.equals(PENCIL)) {
                    toolsAnimator("choose", PENCIL);
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = PENCIL;
                    drawView.setBrushBitmap(PENCIL);
                }
                break;
            case R.id.draw_tools_pen:
                if (!DrawProperty.BUTTON_CHOOSE.equals(PEN)) {
                    toolsAnimator("choose", PEN);
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = PEN;
                    drawView.setBrushBitmap(PEN);
                }
                break;
            case R.id.draw_tools_crayon:
                if (!DrawProperty.BUTTON_CHOOSE.equals(CRAYON)) {
                    toolsAnimator("choose", CRAYON);
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = CRAYON;
                    drawView.setBrushBitmap(CRAYON);
                }
                break;
            case R.id.draw_tools_brush:
                if (!DrawProperty.BUTTON_CHOOSE.equals(BRUSH)) {
                    toolsAnimator("choose", BRUSH);
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = BRUSH;
                    drawView.setBrushBitmap(BRUSH);
                }
                break;
            case R.id.draw_tools_gun:
                if (!DrawProperty.BUTTON_CHOOSE.equals(GUN)) {
                    toolsAnimator("choose", GUN);
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = GUN;
                    drawView.setBrushBitmap(GUN);
                }
                break;
            case R.id.draw_tools_seal:
                Intent intent_seal = new Intent(DrawActivity.this, StampPickerActivity.class);
                startActivity(intent_seal);
                break;
            case R.id.draw_tools_eraser:
                if (!DrawProperty.BUTTON_CHOOSE.equals(ERASER)) {
                    toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                    toolsAnimator("choose", ERASER);
                    DrawProperty.BUTTON_CHOOSE = ERASER;
                    drawView.setBrushBitmap(ERASER);
                }
                break;
            case R.id.draw_more:
                if(drawMoreLayout.getVisibility()!=View.VISIBLE) {
                    drawMoreLayout.setVisibility(View.VISIBLE);
                }else{
                    drawMoreLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.draw_learn_video:
                drawMoreLayout.setVisibility(View.GONE);
                Intent intent_videos = new Intent(DrawActivity.this, ClassVideosActivity.class);
                startActivity(intent_videos);
                break;
        }
    }

    private Handler handler = new Handler();
    private Runnable updateUndeAndRedo = new Runnable() {
        @Override
        public void run() {
            searchStatu();
            Drawable dra = null;
            if (!drawView.getUndoAndRedo().currentIsFirst()) {
                dra = getResources().getDrawable(R.drawable.icon_arrow_l_h);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            } else {
                dra = getResources().getDrawable(R.drawable.icon_arrow_l);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            }
            draw_undo_image.setBackground(dra);
            Drawable dra_redo = null;
            if (!drawView.getUndoAndRedo().currentIsLast()) {
                dra_redo = getResources().getDrawable(R.drawable.icon_arrow_r_h);
                dra_redo.setBounds(0, 0, dra_redo.getMinimumWidth(), dra_redo.getMinimumHeight());
            } else {
                dra_redo = getResources().getDrawable(R.drawable.icon_arrow_r);
                dra_redo.setBounds(0, 0, dra_redo.getMinimumWidth(), dra_redo.getMinimumHeight());
            }
            draw_redo_image.setBackground(dra_redo);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateUndeAndRedo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateUndeAndRedo);
    }

    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(DrawProperty.Broadcase_name);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DrawProperty.Broadcase_name)) {
                String rece_type = intent.getExtras().getString("type");
                if (rece_type.equals("color")) {
                    int color_index = Integer.parseInt(intent.getExtras().getString("index"));
                    System.out.println("广播收到的数据：" + color_index);
                    draw_color_image.setBackground(getResources().getDrawable(samllImageIds[color_index]));
                } else if (rece_type.equals("stamp")) {
//                    draw_water.setBackground(getResources().getDrawable(R.drawable.student_l_icon0));
//                    draw_caryon.setBackground(getResources().getDrawable(R.drawable.student_l_icon1));
//                    draw_brush.setBackground(getResources().getDrawable(R.drawable.student_l_icon2));
//                    draw_rubber.setBackground(getResources().getDrawable(R.drawable.student_l_icon3));
//                    draw_stamp.setBackground(getResources().getDrawable(R.drawable.student_l_icon4_h));
//                    drawView.setBrushBitmap(DrawAttribute.DrawStatus.STAMP);
//                    DrawProperty.BUTTON_CHOOSE = DrawAttribute.DrawStatus.STAMP;
                    int stamp_id = intent.getExtras().getInt("stamp_id");
                    DrawProperty.STAMP_ID = stamp_id;
                    if (!DrawProperty.BUTTON_CHOOSE.equals(SEAL)) {
                        toolsAnimator("choose", SEAL);
                        toolsAnimator("cancel", DrawProperty.BUTTON_CHOOSE);
                        DrawProperty.BUTTON_CHOOSE = SEAL;
                    }
                    drawView.setBrushBitmap(SEAL);
                } else if (rece_type.equals("background")) {
                    int bg_index = Integer.parseInt(intent.getExtras().getString("index"));
                    Bitmap bit = BitmapFactory.decodeResource(getResources(), mBackgroundIds[bg_index]);
                    drawView.setBackgroundBitmap(bit, false);
                    drawView.cleanPaintBitmap();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    //退出
    private void Exit() {
        dialog.show();
        dialog.changeInfo("退出中...");
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "login/exitLogin")//
                .addParams("stu_id", ArtStudentApplication.STU_ID)//
                .build()//
                .execute(new MyStringCallback("exit"));
    }

    //获取当前的投票状态
    private void searchStatu() {
        System.out.println("------srarchStatu------"+ArtStudentApplication.CTR_ID+":::"+ArtStudentApplication.USER_ID);
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "open/findClassRecordIsVote")//
                .addParams("ctr_id", ArtStudentApplication.CTR_ID)
                .addParams("stu_no", ArtStudentApplication.USER_ID)
                .build()//
                .execute(new MyStringCallback("statu"));
    }

    private class MyStringCallback extends StringCallback {

        String type = "";

        MyStringCallback(String type) {
            this.type = type;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            dialog.cancel();
        }

        @Override
        public void onResponse(String response, int id) {
            dialog.cancel();
            Log.e("DrawActivity", response);
            StuDoamin stu = new Gson().fromJson(response, StuDoamin.class);
            String stu_success = stu.getSuccess();
            if (type.equals("statu")) {
                is_submit_works = stu.getIs_submit_works();
            }
            if (stu.getLastComment() != null) {
                drawCommentContent.setText(stu.getLastComment().getContent());
                commentIn();
            }
            if (stu.getIsLike() != null) {
                if (stu.getIsLike().toString().equals("1")) {
                    noticeIn();
                }
            }
            if (stu.getPage_name() != null && stu.getShow_page() != null && !stu.getPage_name().equals("")) {
                System.out.println("==============" + stu.getPage_name() + "::" + stu.getShow_page());
                if (stu.getPage_name().equals("patternDraw")) {
                    Intent vote_intent = new Intent(DrawActivity.this, PatternActivity.class);
                    startActivity(vote_intent);
                } else {
                    //进入纹样、图书馆
                    Intent vote_intent = new Intent(DrawActivity.this, VoteActivity.class);
                    vote_intent.putExtra("page_name", stu.getPage_name().toString());
                    vote_intent.putExtra("show_page", stu.getShow_page().toString());
                    startActivity(vote_intent);
                }
            }
            if (stu_success.equals("1")) {
                if (type.equals("exit")) {
                    clearSp();
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else {
                    Intent vote_intent = new Intent(DrawActivity.this, VoteActivity.class);
                    vote_intent.putExtra("page_name", "");
                    vote_intent.putExtra("show_page", "");
                    startActivity(vote_intent);
                }
            } else if (stu_success.equals("0")) {
                if (type.equals("exit")) {
                    Toast.makeText(DrawActivity.this, "退出失败，请重试!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent inte = new Intent(DrawActivity.this, ExitDialogActivity.class);
                startActivity(inte);
            }
        }
    }

    private void clearSp() {
        SharedPreferences shared = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("user_id", "");
        edit.putString("ctr_id", "");
        edit.putString("stu_id", "");
        edit.commit();
    }

    @Override
    public void onBackPressed() {
        isExist();
    }

    private long firstTime = 0;

    private void isExist() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 1500) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            Exit();
            this.finish();
        }
    }

    /**
     * 教师赞赏显示动画
     */
    private void noticeIn() {
        Animator mDrawNoticeAnimator = ObjectAnimator.ofFloat(
                drawNotice, View.Y, -drawNotice.getHeight(), drawNotice.getTop());
        mDrawNoticeAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                drawNotice.bringToFront();
                drawNotice.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mDrawNoticeAni = null;
                System.gc();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                noticeOut();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        List<Animator> profileAnimators = new ArrayList<>();
        profileAnimators.add(mDrawNoticeAnimator);
        mDrawNoticeAni = new AnimatorSet();
        mDrawNoticeAni.playTogether(profileAnimators);
        mDrawNoticeAni.setDuration(500);
        mDrawNoticeAni.setInterpolator(new DecelerateInterpolator());
        mDrawNoticeAni.start();
    }

    private void noticeOut() {

        Animator mDrawNoticeAnimator_out = ObjectAnimator.ofFloat(
                drawNotice, View.Y, drawNotice.getTop(), -drawNotice.getHeight());
        List<Animator> profileAnimators = new ArrayList<>();
        profileAnimators.add(mDrawNoticeAnimator_out);
        mDrawNoticeAni = new AnimatorSet();
        mDrawNoticeAni.playTogether(profileAnimators);
        mDrawNoticeAni.setDuration(500);
        mDrawNoticeAni.setInterpolator(new DecelerateInterpolator());
        mDrawNoticeAni.start();
    }

    /**
     * 教师点评动画
     */
    private void commentOut() {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        Animator mDrawCommentAnimator_out = ObjectAnimator.ofFloat(
                drawComment, View.X, drawComment.getLeft(), w_screen + drawComment.getWidth());
        List<Animator> profileAnimators = new ArrayList<>();
        profileAnimators.add(mDrawCommentAnimator_out);
        mCommentNoticeAni = new AnimatorSet();
        mCommentNoticeAni.playTogether(profileAnimators);
        mCommentNoticeAni.setDuration(500);
        mCommentNoticeAni.setInterpolator(new DecelerateInterpolator());
        mCommentNoticeAni.start();
    }

    private void commentIn() {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        Animator mDrawCommentAnimator_in = ObjectAnimator.ofFloat(
                drawComment, View.X, w_screen + drawComment.getWidth(), drawComment.getLeft());
        mDrawCommentAnimator_in.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                drawComment.bringToFront();
                drawComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCommentNoticeAni = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        List<Animator> profileAnimators = new ArrayList<>();
        profileAnimators.add(mDrawCommentAnimator_in);
        mCommentNoticeAni = new AnimatorSet();
        mCommentNoticeAni.playTogether(profileAnimators);
        mCommentNoticeAni.setDuration(500);
        mCommentNoticeAni.setInterpolator(new DecelerateInterpolator());
        mCommentNoticeAni.start();
    }

    private void toolsAnimator(String type, DrawAttribute.DrawStatus choose) {

        ImageView img = null;
        switch (choose) {
            case PENCIL:
                img = drawToolsPencil;
                break;
            case PEN:
                img = drawToolsPen;
                break;
            case GUN:
                img = drawToolsGun;
                break;
            case CRAYON:
                img = drawToolsCrayon;
                break;
            case BRUSH:
                img = drawToolsBrush;
                break;
            case SEAL:
                img = drawToolsSeal;
                break;
            case ERASER:
                img = drawToolsEraser;
                break;
        }

        Animator ani = null;
        if (type.equals("choose")) {
            if (Left_Postion == 0) Left_Postion = img.getLeft();
            ani = ObjectAnimator.ofFloat(
                    img, View.X, Left_Postion, 0);
        } else {
            ani = ObjectAnimator.ofFloat(
                    img, View.X, 0, Left_Postion);
        }
        List<Animator> profileAnimators = new ArrayList<>();
        profileAnimators.add(ani);
        AnimatorSet toolsAniSet = new AnimatorSet();
        toolsAniSet.playTogether(profileAnimators);
        toolsAniSet.setDuration(500);
        toolsAniSet.setInterpolator(new DecelerateInterpolator());
        toolsAniSet.start();
    }
}
