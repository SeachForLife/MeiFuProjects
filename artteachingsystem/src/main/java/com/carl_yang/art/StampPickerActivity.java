package com.carl_yang.art;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.BackgroundFragment;
import com.carl_yang.art.resourcesfragment.StampPickerFragment;
import com.carl_yang.drawabout.adapter.StampPickAdapter;
import com.carl_yang.view.DrawView;

public class StampPickerActivity extends Activity {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private TextView stamp_pick_text1;
    private TextView stamp_pick_text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp_picker);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        DisplayMetrics dm=this.getResources().getDisplayMetrics();
        int w_screen=dm.widthPixels;
        int h_screen=dm.heightPixels;
        android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
        layoutParams.width=1120*w_screen/1280;
        layoutParams.height=537*h_screen/800;
        this.getWindow().setAttributes(layoutParams);

        stamp_pick_text1= (TextView) findViewById(R.id.stamp_pick_text1);
        stamp_pick_text2= (TextView) findViewById(R.id.stamp_pick_text2);
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.stamp_pick_fragment,new StampPickerFragment());
        ft.commit();
    }

    public void stampClick(View v){
        switch (v.getId()){
            case R.id.stamp_pick_text1:
                stamp_pick_text1.setTextColor(Color.parseColor("#197dc4"));
                stamp_pick_text1.setBackground(getResources().getDrawable(R.drawable.stamp_table_h));
                stamp_pick_text2.setTextColor(Color.parseColor("#555555"));
                stamp_pick_text2.setBackground(getResources().getDrawable(R.drawable.stamp_table));
                ft=fm.beginTransaction();
                ft.replace(R.id.stamp_pick_fragment,new StampPickerFragment());
                ft.commit();
                break;
            case R.id.stamp_pick_text2:
                stamp_pick_text2.setTextColor(Color.parseColor("#197dc4"));
                stamp_pick_text2.setBackground(getResources().getDrawable(R.drawable.stamp_table_h));
                stamp_pick_text1.setTextColor(Color.parseColor("#555555"));
                stamp_pick_text1.setBackground(getResources().getDrawable(R.drawable.stamp_table));
                ft=fm.beginTransaction();
                ft.replace(R.id.stamp_pick_fragment,new BackgroundFragment());
                ft.commit();
                break;
        }
    }
}
