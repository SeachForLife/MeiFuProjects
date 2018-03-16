package com.carl_yang.art;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.CourseWareContentFragment;

public class CourseWareGridActivity extends Activity {

    private ImageView course_ware_icon;
    private TextView course_ware_teachername;
    private TextView course_ware_grid_titlename;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_ware_grid);

        ArtTeachiingApplication.getmInstance().addActivity(this);
        course_ware_icon= (ImageView) findViewById(R.id.course_ware_icon);
        course_ware_teachername= (TextView) findViewById(R.id.course_ware_teachername);
        course_ware_teachername.setText(ArtTeachiingApplication.teacher_name);
        course_ware_grid_titlename= (TextView) findViewById(R.id.course_ware_grid_titlename);
        String title_name=getIntent().getStringExtra("msg");
        String click_num = getIntent().getStringExtra("clicknum");
        course_ware_grid_titlename.setText(title_name);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.course_ware_grid__fragment, new CourseWareContentFragment(click_num));
        ft.commit();
    }

    public void course_ware_grid_click(View v){
        switch (v.getId()){
            case R.id.course_ware_grid_back:
                this.finish();
                break;
        }
    }

}
