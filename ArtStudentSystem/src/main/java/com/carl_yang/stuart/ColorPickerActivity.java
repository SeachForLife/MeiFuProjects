package com.carl_yang.stuart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.drawfile.adapter.ColorGridImageAdapter;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.property.DrawProperty;


public class ColorPickerActivity extends Activity {

    private GridView colr_grid;
    private ColorGridImageAdapter ColorImageViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        ArtStudentApplication.getmInstance().addActivity(this);
        DisplayMetrics dm=this.getResources().getDisplayMetrics();
        int w_screen=dm.widthPixels;
        int h_screen=dm.heightPixels;
        android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
        layoutParams.width=1108*w_screen/1280;
        layoutParams.height=335*h_screen/800;
        this.getWindow().setAttributes(layoutParams);

        colr_grid= (GridView) findViewById(R.id.colr_grid);
        ColorImageViewAdapter=new ColorGridImageAdapter(ColorPickerActivity.this);
        colr_grid.setAdapter( ColorImageViewAdapter );
        colr_grid .setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = 0;
                switch (i) {
                    case 0:
                        color=0xff000000;
                        break;
                    case 1:
                        color=0xff7e7e7e;
                        break;
                    case 2:
                        color=0xffff0000;
                        break;
                    case 3:
                        color=0xffffff02;
                        break;
                    case 4:
                        color=0xffff6602;
                        break;
                    case 5:
                        color=0xff1ba4e6;
                        break;
                    case 6:
                        color=0xff33ccff;
                        break;
                    case 7:
                        color=0xff59d55a;
                        break;
                    case 8:
                        color=0xffff00ff;
                        break;
                    case 9:
                        color=0xff0000ff;
                        break;
                    case 10:
                        color=0xff006400;
                        break;
                    case 11:
                        color=0xff660066;
                        break;
                    case 12:
                        color=0xffcc9902;
                        break;
                    case 13:
                        color=0xff993300;
                        break;
                    case 14:
                        color=0xff7093bf;
                        break;
                    case 15:
                        color=0xffc9bfe7;
                        break;
                    case 16:
                        color=0xffc3c3c3;
                        break;
                    case 17:
                        color=0xff3a1d00;
                        break;
                }
                DrawProperty.COLOR_CURRENT=i;
                DrawProperty.COLOR_CHOOSE=color;
                Intent mIntent = new Intent(DrawProperty.Broadcase_name);
                mIntent.putExtra("type","color");
                mIntent.putExtra("index",i+"");
                mIntent.putExtra("color",color+"");
                sendBroadcast(mIntent);
                finish();
            }
        });

    }

}
