package com.carl_yang.art;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.BackHandledFragment;
import com.carl_yang.art.resourcesfragment.WebViewFragment;
import com.carl_yang.interfacepac.BackHandledInterface;


public class TasksActivity extends FragmentActivity implements BackHandledInterface {

    private Activity activity;
    private ImageView tasks_images;
    private TextView tasks_teachername;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private WebViewFragment wvFra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        activity = this;
        tasks_images= (ImageView) findViewById(R.id.tasks_images);
        tasks_teachername= (TextView) findViewById(R.id.tasks_teachername);
        tasks_teachername.setText(ArtTeachiingApplication.teacher_name);
//        tasks_images.setImageBitmap(ArtTeachiingApplication.bitmap);
        double  a=Math.random();
        wvFra=new WebViewFragment(ArtTeachiingApplication.localUrl+"android/class-record.html?class_id="+ArtTeachiingApplication.CLASS_CHOOSE_ID);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.tasks_fragment, wvFra);
        ft.commit();
    }

    public void famous_click(View v) {
        switch (v.getId()) {
            case R.id.tasks_mainback:
                wvFra.clearTaskJs();
                this.finish();
                break;
        }
    }

    private BackHandledFragment mBackHandedFragment;

    @Override
    public void onBackPressed() {
        wvFra.clearTaskJs();
        if(mBackHandedFragment == null||!mBackHandedFragment.onBackPressed()){
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
