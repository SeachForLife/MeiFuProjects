package com.carl_yang.art;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.BackHandledFragment;
import com.carl_yang.art.resourcesfragment.WebViewFragment;
import com.carl_yang.interfacepac.BackHandledInterface;

public class CourseContentActivity extends FragmentActivity implements BackHandledInterface {

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        ArtTeachiingApplication.getmInstance().addActivity(this);
        //添加硬件渲染
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        String choose_grade=getIntent().getStringExtra("choose_grade");
        String choose_courseware=getIntent().getStringExtra("choose_courseware");
        int grade=(Integer.parseInt(choose_grade)+1)/2;
        int book=Integer.parseInt(choose_grade)%2==1?1:2;
        System.out.println("当前选择的课程名称是："+grade+"-"+book+"-"+choose_courseware);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.course_content_framelayout, new WebViewFragment(ArtTeachiingApplication.localUPversion+
        "android/courseware/courseware"+grade+"-"+book+"-"+choose_courseware+"/course-ware-detail.html"));
        ft.commit();
    }

    private BackHandledFragment mBackHandedFragment;

    @Override
    public void onBackPressed() {
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
