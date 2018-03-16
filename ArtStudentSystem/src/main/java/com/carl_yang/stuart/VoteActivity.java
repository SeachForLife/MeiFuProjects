package com.carl_yang.stuart;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyUtils;
import com.carl_yang.fragment.WebViewFragment;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

public class VoteActivity extends Activity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private WebViewFragment wvFra;

    private TextView vote_timer;
    private LinearLayout vote_title;

    private int count_time=300;
    Timer timer=null;
    private Handler handler =new Handler();

    private String pageName;
    private String showPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        ArtStudentApplication.getmInstance().addActivity(this);
        vote_timer= (TextView) findViewById(R.id.vote_timer);
        vote_title= (LinearLayout) findViewById(R.id.vote_title);

        pageName=getIntent().getStringExtra("page_name");
        showPage=getIntent().getStringExtra("show_page");
        if(pageName.equals("")){
            vote_title.setVisibility(View.VISIBLE);
            wvFra=new WebViewFragment(ArtStudentApplication.commonUrl + "android/student-works-vote.html?ctr_id="
                    +ArtStudentApplication.CTR_ID+"&stu_id="+ArtStudentApplication.STU_ID);
        }else{
            vote_title.setVisibility(View.GONE);
            //http://192.168.1.88:8080/ArtTeaching/android/wenyang.html?shareBtn=0
            wvFra=new WebViewFragment(ArtStudentApplication.commonUrl + pageName+"?shareBtn=0");
        }


        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.vote_fragment, wvFra);
        ft.commit();



        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count_time--;
            }
        },1000,1000);
    }

    private Runnable countRunable=new Runnable() {
        @Override
        public void run() {
            if(count_time==0) VoteActivity.this.finish();
            if(count_time>0) vote_timer.setText("倒计时:"+ MyUtils.parseTime(count_time));
            handler.postDelayed(this,1000);
        }
    };

    public void vote_Click(View v){
        switch (v.getId()){
            case R.id.vote_ok:
                this.finish();
                break;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.post(countRunable);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        if(timer!=null) {
            timer.cancel();
            timer=null;
        }
        handler.removeCallbacks(countRunable);
    }
}
