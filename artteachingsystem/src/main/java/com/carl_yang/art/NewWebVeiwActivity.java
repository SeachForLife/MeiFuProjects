package com.carl_yang.art;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.art.resourcesfragment.BackHandledFragment;
import com.carl_yang.art.resourcesfragment.WebViewFragment;
import com.carl_yang.interfacepac.BackHandledInterface;

public class NewWebVeiwActivity extends FragmentActivity implements BackHandledInterface {

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_web_veiw);
        ArtTeachiingApplication.getmInstance().addActivity(this);
        //添加硬件渲染
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        ArtTeachiingApplication.localUrl=getIntent().getStringExtra("url");
        String urlend=getIntent().getStringExtra("endUrl");
        ArtTeachiingApplication.CTR_ID=getIntent().getStringExtra("ctr_id");
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.newwebview_framelayout, new WebViewFragment(ArtTeachiingApplication.localUrl+urlend));
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

    @Override
    protected void onDestroy(){
        System.gc();
        Process.killProcess(Process.myPid());
    }
}
