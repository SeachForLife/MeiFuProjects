package com.carl_yang.art;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.adapter.ImageAdapter;
import com.carl_yang.domain.CommonDomain;
import com.carl_yang.drawabout.property.DrawProperty;
import com.carl_yang.drawabout.tools.DrawAttribute;
import com.carl_yang.util.PatternBitmapUtils;
import com.carl_yang.view.DrawView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class PatternActivity extends AppCompatActivity {

    @BindView(R.id.pattern_canvas)
    DrawView patternCanvas;
    @BindView(R.id.pattern_imagelist)
    ListView patternImagelist;
    @BindView(R.id.pattern_bg)
    ImageView patternBg;
    @BindView(R.id.pattern_photoview)
    PhotoView patternPhotoview;
    @BindView(R.id.pattern_framelayout)
    FrameLayout patternFramelayout;
    @BindView(R.id.pattern_seekBar)
    SeekBar patternSeekBar;
    @BindView(R.id.pattern_color_image)
    ImageView patternColorImage;
    @BindView(R.id.pattern_undo_image)
    ImageView patternUndoImage;
    @BindView(R.id.pattern_redo_image)
    ImageView patternRedoImage;

    private ImageAdapter imAdapter;
    List<Drawable> list;
    private List<Drawable> list_copy;
    Bitmap bt;
    private int current_postion = 0;
    private String localUrl="";
    private String ctrId="";

    Integer[] single_pattern = {
            R.drawable.pattern_part1, R.drawable.pattern_part2, R.drawable.pattern_part3, R.drawable.pattern_part4,
            R.drawable.pattern_part5, R.drawable.pattern_part6, R.drawable.pattern_part7, R.drawable.pattern_part8,
            R.drawable.pattern_part9, R.drawable.pattern_part10, R.drawable.pattern_part11, R.drawable.pattern_part12
    };

    private Integer[] samllImageIds = {
            R.drawable.color0 , R.drawable.color1 ,R.drawable.color2 , R.drawable.color3 ,
            R.drawable.color4 , R.drawable.color5 ,R.drawable.color6 , R.drawable.color7 ,
            R.drawable.color8 , R.drawable.color9 ,R.drawable.color10,  R.drawable.color11,
            R.drawable.color12,  R.drawable.color13, R.drawable.color14,  R.drawable.color15,
            R.drawable.color16,  R.drawable.color17
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        setContentView(R.layout.activity_pattern);

        ButterKnife.bind(this);
        localUrl=getIntent().getStringExtra("url");
        ctrId=getIntent().getStringExtra("ctr_id");
        //默认第一个图片
        Bitmap sour = BitmapFactory.decodeResource(getResources(), single_pattern[0]);
        Bitmap m=PatternBitmapUtils.clipRectFromTopLeft(sour, sour.getWidth() / 6, sour.getHeight());
        patternCanvas.setBackgroundBitmapPattern(m,"rect");
        sour.recycle();
        m.recycle();
        System.gc();

        registerBoradcastReceiver();
        patternSeekBar.setMax(30);
        patternSeekBar.setProgress(15);
        patternSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        list = new ArrayList<>();
        list.add(getResources().getDrawable(R.drawable.pattern_sw1));
        list.add(getResources().getDrawable(R.drawable.pattern_sw2));
        list.add(getResources().getDrawable(R.drawable.pattern_sw3));
        list.add(getResources().getDrawable(R.drawable.pattern_sw4));
        list.add(getResources().getDrawable(R.drawable.pattern_sw5));
        list.add(getResources().getDrawable(R.drawable.pattern_sw6));
        list.add(getResources().getDrawable(R.drawable.pattern_sw7));
        list.add(getResources().getDrawable(R.drawable.pattern_sw8));
        list.add(getResources().getDrawable(R.drawable.pattern_sw9));
        list.add(getResources().getDrawable(R.drawable.pattern_sw10));
        list.add(getResources().getDrawable(R.drawable.pattern_sw11));
        list.add(getResources().getDrawable(R.drawable.pattern_sw12));

        list_copy = new ArrayList<>();
        for (Drawable d : list) {
            list_copy.add(d);
        }

        imAdapter = new ImageAdapter(list, this);
        patternImagelist.setAdapter(imAdapter);
        patternImagelist.setSelection(0);
        patternImagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patternCanvas.cleanPaintBitmap();
                current_postion = position;
                Bitmap bit_old = BitmapFactory.decodeResource(getResources(), single_pattern[position]);
                Bitmap new_bg = null;
                String type = "";
                switch (position) {
                    case 3:
                        new_bg = createPlateBitmap(bit_old, 10, 150);
                        type = "plate";
                        break;
                    case 4:
                        new_bg = createPlateBitmap(bit_old, 10, 120);
                        type = "plate";
                        break;
                    case 5:
                        new_bg = createPlateBitmap(bit_old, 40, 140);
                        type = "plate";
                        break;
                    case 9:
                        new_bg = PatternBitmapUtils.clipRectFromTopLeft(bit_old, bit_old.getWidth() / 8, bit_old.getHeight());
                        type = "rect";
                        break;
                    case 10:
                        new_bg = PatternBitmapUtils.clipRectFromTopLeft(bit_old, bit_old.getWidth() / 4, bit_old.getHeight());
                        type = "rect";
                        break;
                    case 11:
                        new_bg = PatternBitmapUtils.clipRectFromTopLeft(bit_old, bit_old.getWidth() / 4, bit_old.getHeight());
                        type = "rect";
                        break;
                    default:
                        new_bg = PatternBitmapUtils.clipRectFromTopLeft(bit_old, bit_old.getWidth() / 6, bit_old.getHeight());
                        type = "rect";
                        break;
                }
                patternCanvas.setBackgroundBitmapPattern(new_bg, type);
                bit_old.recycle();bit_old=null;
                new_bg.recycle();new_bg=null;
                System.gc();
            }
        });
        patternImagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bt = PatternBitmapUtils.drawableToBitmap(list.get(position));
                patternPhotoview.setImageBitmap(bt);
                patternFramelayout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        patternPhotoview.enable();
        patternPhotoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternFramelayout.setVisibility(View.GONE);
                patternPhotoview.setImageBitmap(null);
            }
        });
    }

    private Bitmap createPlateBitmap(Bitmap bit_old, int ring_out, int ring_in) {
        Bitmap bit = PatternBitmapUtils.PicZoom(bit_old, DrawAttribute.screenHeight, DrawAttribute.screenHeight);
        Bitmap bit_new = PatternBitmapUtils.clipRectFromTopLeft(bit, bit.getHeight(), bit.getHeight());
        Bitmap new_bg = PatternBitmapUtils.clipRingBitmapFirst(bit_new, ring_out, ring_in);
        bit.recycle();
        bit_new.recycle();
        return new_bg;
    }

    private void initParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        DrawAttribute.screenWidth = getResources().getDimensionPixelSize(R.dimen.dimen_414_dip);
        DrawAttribute.screenHeight = getResources().getDimensionPixelSize(R.dimen.dimen_244_dip);
        DrawAttribute.paint.setColor(Color.LTGRAY);
        DrawAttribute.paint.setStrokeWidth(3);

        //进入画板后，将所有状态进行初始化
        DrawProperty.BRUSH_SIZE = 25;
        DrawProperty.COLOR_CHOOSE = Color.BLACK;
        DrawProperty.BUTTON_CHOOSE = DrawAttribute.DrawStatus.PENCIL;
        DrawProperty.STAMP_ID = R.drawable.yz1;
        DrawProperty.COLOR_CURRENT = 0;
    }

    public void pattern_Click(View v) {
        switch (v.getId()) {
            case R.id.pattern_back:
                this.finish();
                System.gc();
                break;
            case R.id.pattern_send:
                intentPatternIssued("patternDraw");
                break;
            case R.id.pattern_create:
                Bitmap bit = null;
                int startX;
                int startY;
                int endX;
                int endY;
                switch (current_postion) {
                    case 0:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 136 / 650;
                        startY = bit.getHeight() * 581 / 650;
                        endX = bit.getWidth() * 530 / 650;
                        endY = bit.getHeight() * 609 / 650;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 1:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 169 / 650;
                        startY = bit.getHeight() * 572 / 650;
                        endX = bit.getWidth() * 493 / 650;
                        endY = bit.getHeight() * 606 / 650;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 2:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 150 / 1358;
                        startY = bit.getHeight() * 124 / 600;
                        endX = bit.getWidth() * 1208 / 1358;
                        endY = bit.getHeight() * 232 / 600;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 3:
                        plateBitmap(10, 150);
                        break;
                    case 4:
                        plateBitmap(10, 120);
                        break;
                    case 5:
                        plateBitmap(40, 140);
                        break;
                    case 6:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = 0;
                        startY = bit.getHeight() * 218 / 600;
                        endX = bit.getWidth();
                        endY = bit.getHeight() * 305 / 600;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 7:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = 0;
                        startY = bit.getHeight() * 162 / 600;
                        endX = bit.getWidth();
                        endY = bit.getHeight() * 250 / 600;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 8:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = 0;
                        startY = bit.getHeight() * 60 / 600;
                        endX = bit.getWidth();
                        endY = bit.getHeight() * 207 / 600;
                        rectBitmap(bit, 6, startX, startY, endX, endY);
                        break;
                    case 9:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 100 / 1358;
                        startY = bit.getHeight() * 86 / 600;
                        endX = bit.getWidth() * 1250 / 1358;
                        endY = bit.getHeight() * 150 / 600;
                        rectBitmap(bit, 8, startX, startY, endX, endY);
                        break;
                    case 10:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 134 / 650;
                        startY = bit.getHeight() * 594 / 650;
                        endX = bit.getWidth() * 510 / 650;
                        endY = bit.getHeight() * 644 / 650;
                        rectBitmap(bit, 4, startX, startY, endX, endY);
                        break;
                    case 11:
                        bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
                        startX = bit.getWidth() * 107 / 650;
                        startY = bit.getHeight() * 526 / 650;
                        endX = bit.getWidth() * 547 / 650;
                        endY = bit.getHeight() * 598 / 650;
                        rectBitmap(bit, 4, startX, startY, endX, endY);
                        break;
                }
                break;
            case R.id.pattern_color_layout://颜色选择
                Intent intent_colorpick = new Intent(PatternActivity.this, ColorPickerActivity.class);
                startActivity(intent_colorpick);
                break;
            case R.id.pattern_redo_layout://下一步
                patternCanvas.redo();
                break;
            case R.id.pattern_undo_layout://上一步
                patternCanvas.undo();
                break;
            case R.id.pattern_del_layout:
                patternCanvas.cleanPaintBitmap();
                break;
        }
    }

    private void rectBitmap(Bitmap bit, int width_num, int startX, int startY, int endX, int endY) {
        //拿到绘画区的图片
        Bitmap new_bit = patternCanvas.patternBitmap();
        Bitmap bit_old = BitmapFactory.decodeResource(getResources(), single_pattern[current_postion]);
        float scale = bit_old.getHeight() * 1.0f / (DrawAttribute.screenHeight / 2);
        int marginTop = (DrawAttribute.screenHeight - (int) (bit_old.getHeight() / scale)) / 2;
        //将需要的位置拿取出来
        Bitmap bt1 = PatternBitmapUtils.takeRect(new_bit, 0, marginTop,
                (int) (bit_old.getWidth() / width_num / scale), (int) (bit_old.getHeight() / scale));
        Bitmap link_bitmap = PatternBitmapUtils.linkBitmap(bt1, width_num);
        Bitmap result = PatternBitmapUtils.setBitmapIn(bit, link_bitmap, startX, startY, endX, endY);
        list.set(current_postion, new BitmapDrawable(result));
        imAdapter.notifyDataSetChanged();
        bit.recycle();
        new_bit.recycle();
        bit_old.recycle();
        bt1.recycle();
        link_bitmap.recycle();
        System.gc();
    }

    private void plateBitmap(int ring_out, int ring_in) {
        Bitmap new_bit = patternCanvas.patternBitmap();
        Bitmap bt1 = PatternBitmapUtils.clipRectFromTopLeft(new_bit, new_bit.getHeight(), new_bit.getHeight());
        Bitmap b = PatternBitmapUtils.clipRingBitmapFirst(bt1, ring_out, ring_in);
        Bitmap result_bitmap = PatternBitmapUtils.rotateAndLinkToCircle(b, 6);
        Bitmap bit = PatternBitmapUtils.drawableToBitmap(list_copy.get(current_postion));
        if(current_postion==5) ring_out=10;
        Bitmap result = PatternBitmapUtils.setBitmapIn(bit, result_bitmap, ring_out, ring_out, bit.getWidth() - ring_out, bit.getHeight() - ring_out);
        list.set(current_postion, new BitmapDrawable(result));
        imAdapter.notifyDataSetChanged();
        new_bit.recycle();
        bt1.recycle();
        b.recycle();
        result_bitmap.recycle();
        System.gc();
    }

    private Handler handler = new Handler();
    private Runnable updateUndeAndRedo = new Runnable() {
        @Override
        public void run() {
            Drawable dra = null;
            if (!patternCanvas.getUndoAndRedo().currentIsFirst()) {
                dra = getResources().getDrawable(R.drawable.icon_arrow_l_h);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            } else {
                dra = getResources().getDrawable(R.drawable.icon_arrow_l);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            }
            patternUndoImage.setBackground(dra);
            Drawable dra_redo = null;
            if (!patternCanvas.getUndoAndRedo().currentIsLast()) {
                dra_redo = getResources().getDrawable(R.drawable.icon_arrow_r_h);
                dra_redo.setBounds(0, 0, dra_redo.getMinimumWidth(), dra_redo.getMinimumHeight());
            } else {
                dra_redo = getResources().getDrawable(R.drawable.icon_arrow_r);
                dra_redo.setBounds(0, 0, dra_redo.getMinimumWidth(), dra_redo.getMinimumHeight());
            }
            patternRedoImage.setBackground(dra_redo);
            handler.postDelayed(this, 2000);
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(DrawProperty.Broadcase_name)){
                String rece_type=intent.getExtras().getString("type");
                if(rece_type.equals("color")) {
                    int color_index = Integer.parseInt(intent.getExtras().getString("index"));
                    int color_num=Integer.parseInt(intent.getExtras().getString("color"));
                    DrawProperty.COLOR_CHOOSE=color_num;
                    patternColorImage.setBackground(getResources().getDrawable(samllImageIds[color_index]));
                }
            }
        }
    };

    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(DrawProperty.Broadcase_name);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onBackPressed() {
        if (patternFramelayout.getVisibility() == View.VISIBLE) {
            patternFramelayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateUndeAndRedo);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("PatternActivity 进入销毁阶段");
        unregisterReceiver(mBroadcastReceiver);
        handler.removeCallbacks(updateUndeAndRedo);
        single_pattern=null;
        samllImageIds=null;
        patternColorImage.setBackground(null);
        patternUndoImage.setBackground(null);
        patternRedoImage.setBackground(null);
        if(bt!=null&&!bt.isRecycled()){
            bt.recycle();
            bt=null;
        }
        patternCanvas=null;
        System.gc();
        Process.killProcess(Process.myPid());
    }

    /**
     * 下发接口
     * @param pageName
     */
    public void intentPatternIssued(String pageName) {
        System.out.println("教师点击了，纹样下发按钮!");
        OkHttpUtils
                .post()//
                .url(localUrl + "open/updateShowPage")//
                .addParams("ctr_id", ctrId)//
                .addParams("page_name", pageName)//
                .build()//
                .execute(new Callback<CommonDomain>() {

                    @Override
                    public CommonDomain parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        CommonDomain ur = new Gson().fromJson(string, CommonDomain.class);
                        return ur;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PatternActivity.this,R.string.pattern_issued_faild,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(CommonDomain response, int id) {
                        String su = response.getSuccess().toString();
                        if(su.equals("1")){
                            Toast.makeText(PatternActivity.this,R.string.pattern_issued_success,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(PatternActivity.this,R.string.pattern_issued_faild,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
