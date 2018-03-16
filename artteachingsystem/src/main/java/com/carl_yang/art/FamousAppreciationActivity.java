package com.carl_yang.art;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.interfacepac.BackHandledInterface;
import com.carl_yang.art.resourcesfragment.BackHandledFragment;
import com.carl_yang.art.resourcesfragment.CourseWareFragment;
import com.carl_yang.art.resourcesfragment.WebViewFragment;
import com.carl_yang.progress.HProgressBarLoading;

public class FamousAppreciationActivity extends FragmentActivity implements BackHandledInterface {

//    private WebView webview;
    private Activity activity;

    private TextView famous_world;
    private TextView famous_china;
    private TextView famous_teacher;
    private TextView famous_appreciation_teachername;
    private ImageView famous_appreciation_images;

    private HProgressBarLoading famous_top_progress;
    private TextView famous_center_badnet;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private WebViewFragment wvFra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_appreciation);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        initParms();
    }

    private void initParms() {
        famous_appreciation_images= (ImageView) findViewById(R.id.famous_appreciation_images);
        famous_appreciation_teachername= (TextView) findViewById(R.id.famous_appreciation_teachername);
        famous_appreciation_teachername.setText(ArtTeachiingApplication.teacher_name);
        famous_world = (TextView) findViewById(R.id.famous_world);
        famous_china = (TextView) findViewById(R.id.famous_china);
        famous_teacher= (TextView) findViewById(R.id.famous_teacher);
        activity = this;
        famous_top_progress = (HProgressBarLoading) findViewById(R.id.famous_top_progress);
        famous_center_badnet = (TextView) findViewById(R.id.famous_center_badnet);


        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.famous_appreciation_fragment, new CourseWareFragment());
        ft.commit();
    }

    public void famous_click(View v) {
        switch (v.getId()) {
            case R.id.famous_mainback:
                this.finish();
                break;
            case R.id.famous_teacher:
                famous_teacher.setBackground(getResources().getDrawable(R.drawable.title_bg1_h));
                famous_teacher.setTextColor(getResources().getColor(R.color.blue));
                famous_china.setBackground(getResources().getDrawable(R.drawable.title_bg2));
                famous_china.setTextColor(getResources().getColor(R.color.white));
                famous_world.setBackground(getResources().getDrawable(R.drawable.title_bg3));
                famous_world.setTextColor(getResources().getColor(R.color.white));
                ft = fm.beginTransaction();
                ft.replace(R.id.famous_appreciation_fragment, new CourseWareFragment());
                ft.commit();
                break;
            case R.id.famous_china:
                famous_teacher.setBackground(getResources().getDrawable(R.drawable.title_bg1));
                famous_teacher.setTextColor(getResources().getColor(R.color.white));
                famous_china.setTextColor(getResources().getColor(R.color.blue));
                famous_china.setBackground(getResources().getDrawable(R.drawable.title_bg2_h));
                famous_world.setBackground(getResources().getDrawable(R.drawable.title_bg3));
                famous_world.setTextColor(getResources().getColor(R.color.white));
                wvFra=new WebViewFragment(ArtTeachiingApplication.localUrl+"android/history.html?type=1");
                ft = fm.beginTransaction();
                ft.replace(R.id.famous_appreciation_fragment, wvFra);
                ft.commit();
                break;
            case R.id.famous_world:
                famous_teacher.setBackground(getResources().getDrawable(R.drawable.title_bg1));
                famous_teacher.setTextColor(getResources().getColor(R.color.white));
                famous_china.setBackground(getResources().getDrawable(R.drawable.title_bg2));
                famous_china.setTextColor(getResources().getColor(R.color.white));
                famous_world.setBackground(getResources().getDrawable(R.drawable.title_bg3_h));
                famous_world.setTextColor(getResources().getColor(R.color.blue));
                wvFra=new WebViewFragment(ArtTeachiingApplication.localUrl+"android/history.html?type=0");
                ft = fm.beginTransaction();
                ft.replace(R.id.famous_appreciation_fragment, wvFra);
                ft.commit();
                break;
        }
    }

    private BackHandledFragment mBackHandedFragment;

    @Override
    public void onBackPressed() {
        if(mBackHandedFragment == null||!mBackHandedFragment.onBackPressed()){
//            System.out.println("Famous back Pressed=====");
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
}
