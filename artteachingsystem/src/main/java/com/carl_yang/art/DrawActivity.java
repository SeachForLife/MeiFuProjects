package com.carl_yang.art;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.carl_yang.drawabout.property.DrawProperty;
import com.carl_yang.drawabout.tools.DrawAttribute;
import com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus;
import com.carl_yang.view.DrawView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.BRUSH;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.CRAYON;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.ERASER;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.GUN;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.PEN;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.PENCIL;
import static com.carl_yang.drawabout.tools.DrawAttribute.DrawStatus.SEAL;

public class DrawActivity extends Activity {

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

    private DrawView drawView;
    private int Left_Postion=0;//定位画笔工具未选中状态的左距离

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

        initParams();
    }

    private void initParams() {

        //进入画板后，将所有状态进行初始化
        DrawProperty.BRUSH_SIZE = 30;
        DrawProperty.COLOR_CHOOSE = Color.GRAY;
        DrawProperty.BUTTON_CHOOSE = DrawStatus.PENCIL;
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
                float scale = getResources().getDisplayMetrics().density;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void draw_Click(View v) {
        switch (v.getId()) {
            case R.id.draw_back://返回
                //退出时结束广播
//                unregisterReceiver(mBroadcastReceiver);
                this.finish();
                drawView.freeBitmaps();
                handler.removeCallbacks(updateUndeAndRedo);
                break;
            case R.id.draw_sharing://屏幕共享
                break;
//            case R.id.draw_water://水彩笔
//                draw_water.setBackground(getResources().getDrawable(R.drawable.l_icon0_h));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.l_icon4));
//                drawView.setBrushBitmap(DrawStatus.CASUAL_WATER);
//                DrawProperty.BUTTON_CHOOSE = DrawStatus.CASUAL_WATER;
//                break;
//            case R.id.draw_caryon://蜡笔
//                draw_water.setBackground(getResources().getDrawable(R.drawable.l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.l_icon1_h));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.l_icon4));
//                drawView.setBrushBitmap(DrawStatus.CASUAL_CRAYON);
//                DrawProperty.BUTTON_CHOOSE = DrawStatus.CASUAL_CRAYON;
//                break;
//            case R.id.draw_brush://刷子
//                draw_water.setBackground(getResources().getDrawable(R.drawable.l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.l_icon2_h));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.l_icon3));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.l_icon4));
//                drawView.setBrushBitmap(DrawStatus.CASUAL_COLOR);
//                DrawProperty.BUTTON_CHOOSE = DrawStatus.CASUAL_COLOR;
//                break;
//            case R.id.draw_rubber://橡皮
//                draw_water.setBackground(getResources().getDrawable(R.drawable.l_icon0));
//                draw_caryon.setBackground(getResources().getDrawable(R.drawable.l_icon1));
//                draw_brush.setBackground(getResources().getDrawable(R.drawable.l_icon2));
//                draw_rubber.setBackground(getResources().getDrawable(R.drawable.l_icon3_h));
//                draw_stamp.setBackground(getResources().getDrawable(R.drawable.l_icon4));
//                drawView.setBrushBitmap(DrawStatus.ERASER);
//                DrawProperty.BUTTON_CHOOSE = DrawStatus.ERASER;
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
                Bitmap backgroundBitmap = DrawAttribute.getImageFromAssetsFile(this, "bigpaper00.jpg", true);
                drawView.setBackgroundBitmap(backgroundBitmap, true);
                break;
            case R.id.draw_tools_pencil:
                if(!DrawProperty.BUTTON_CHOOSE.equals(PENCIL)){
                    toolsAnimator("choose",PENCIL);
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = PENCIL;
                    drawView.setBrushBitmap(PENCIL);
                }
                break;
            case R.id.draw_tools_pen:
                if(!DrawProperty.BUTTON_CHOOSE.equals(PEN)){
                    toolsAnimator("choose",PEN);
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = PEN;
                    drawView.setBrushBitmap(PEN);
                }
                break;
            case R.id.draw_tools_crayon:
                if(!DrawProperty.BUTTON_CHOOSE.equals(CRAYON)){
                    toolsAnimator("choose",CRAYON);
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = CRAYON;
                    drawView.setBrushBitmap(CRAYON);
                }
                break;
            case R.id.draw_tools_brush:
                if(!DrawProperty.BUTTON_CHOOSE.equals(BRUSH)){
                    toolsAnimator("choose",BRUSH);
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = BRUSH;
                    drawView.setBrushBitmap(BRUSH);
                }
                break;
            case R.id.draw_tools_gun:
                if(!DrawProperty.BUTTON_CHOOSE.equals(GUN)){
                    toolsAnimator("choose",GUN);
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    DrawProperty.BUTTON_CHOOSE = GUN;
                    drawView.setBrushBitmap(GUN);
                }
                break;
            case R.id.draw_tools_seal:
                Intent intent_seal = new Intent(DrawActivity.this, StampPickerActivity.class);
                startActivity(intent_seal);
                break;
            case R.id.draw_tools_eraser:
                if(!DrawProperty.BUTTON_CHOOSE.equals(ERASER)){
                    toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                    toolsAnimator("choose",ERASER);
                    DrawProperty.BUTTON_CHOOSE = ERASER;
                    drawView.setBrushBitmap(ERASER);
                }
                break;
        }
    }

    private Handler handler = new Handler();
    private Runnable updateUndeAndRedo = new Runnable() {
        @Override
        public void run() {
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
            handler.postDelayed(this, 2000);
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
                    int color_num = Integer.parseInt(intent.getExtras().getString("color"));
                    DrawProperty.COLOR_CHOOSE = color_num;
                    draw_color_image.setBackground(getResources().getDrawable(samllImageIds[color_index]));
                } else if (rece_type.equals("stamp")) {
//                    draw_water.setBackground(getResources().getDrawable(R.drawable.l_icon0));
//                    draw_caryon.setBackground(getResources().getDrawable(R.drawable.l_icon1));
//                    draw_brush.setBackground(getResources().getDrawable(R.drawable.l_icon2));
//                    draw_rubber.setBackground(getResources().getDrawable(R.drawable.l_icon3));
//                    draw_stamp.setBackground(getResources().getDrawable(R.drawable.l_icon4_h));
                    int stamp_id = intent.getExtras().getInt("stamp_id");
                    DrawProperty.STAMP_ID = stamp_id;
//                    DrawProperty.BUTTON_CHOOSE = DrawStatus.STAMP;
                    if(!DrawProperty.BUTTON_CHOOSE.equals(SEAL)){
                        toolsAnimator("choose",SEAL);
                        toolsAnimator("cancel",DrawProperty.BUTTON_CHOOSE);
                        DrawProperty.BUTTON_CHOOSE = SEAL;
                    }
                    drawView.setBrushBitmap(DrawStatus.SEAL);
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
        handler.removeCallbacks(updateUndeAndRedo);
        samllImageIds = null;
        mBackgroundIds = null;
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onBackPressed() {
        this.finish();
        drawView.freeBitmaps();
        handler.removeCallbacks(updateUndeAndRedo);
    }

    private void toolsAnimator(String type,DrawStatus choose) {

        ImageView img=null;
        switch (choose){
            case PENCIL:
                img=drawToolsPencil;
                break;
            case PEN:
                img=drawToolsPen;
                break;
            case GUN:
                img=drawToolsGun;
                break;
            case CRAYON:
                img=drawToolsCrayon;
                break;
            case BRUSH:
                img=drawToolsBrush;
                break;
            case SEAL:
                img=drawToolsSeal;
                break;
            case ERASER:
                img=drawToolsEraser;
                break;
        }

        Animator ani=null;
        if(type.equals("choose")) {
            if(Left_Postion==0) Left_Postion=img.getLeft();
             ani = ObjectAnimator.ofFloat(
                     img, View.X, Left_Postion, 0);
        }else{
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
