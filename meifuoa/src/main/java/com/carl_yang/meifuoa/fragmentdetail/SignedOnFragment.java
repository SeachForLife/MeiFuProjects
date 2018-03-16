package com.carl_yang.meifuoa.fragmentdetail;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.baidulocation.BaiduLocation;
import com.carl_yang.meifuoa.R;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.tools.SharedPreferencesUtils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SignedOnFragment extends Fragment {

    private TextView signo_selectedcustomer;
    private TextView signo_in_time_day;
    private TextView signo_in_time_hour;
    private TextView signo_out_time_day;
    private TextView signo_out_time_hour;
    private TextView signo_out_time_online;
    private Button signo_button;

    public SignedOnFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signed_on, container, false);
        registerBoradcastReceiver();
        signo_selectedcustomer= (TextView) view.findViewById(R.id.signo_selectedcustomer);
        signo_in_time_day= (TextView) view.findViewById(R.id.signo_in_time_day);
        signo_in_time_hour= (TextView) view.findViewById(R.id.signo_in_time_hour);
        signo_out_time_day= (TextView) view.findViewById(R.id.signo_out_time_day);
        signo_out_time_hour= (TextView) view.findViewById(R.id.signo_out_time_hour);
        signo_out_time_online= (TextView) view.findViewById(R.id.signo_out_time_online);
        signo_button= (Button) view.findViewById(R.id.signo_button);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        refushUI();
    }

    public void refushUI(){
        String current_customername= SharedPreferencesUtils.getInstance(getActivity()).read("current_choose_customername","");
        if(current_customername.equals("")||meifuoaApplication.trajcetMessage_list==null){
            signo_selectedcustomer.setText("");
            signo_in_time_day.setText("");
            signo_in_time_hour.setText("");
            signo_out_time_day.setText("");
            signo_out_time_hour.setText("");
            signo_button.setClickable(false);
        }else{
            signo_selectedcustomer.setText(current_customername);
            signo_button.setClickable(true);
            if(meifuoaApplication.trajcetMessage_list.size()==0){
                signo_button.setText("签到");
                signo_in_time_day.setText("");
                signo_in_time_hour.setText("");
                signo_out_time_day.setText("");
                signo_out_time_hour.setText("");
            }else{
                if(meifuoaApplication.trajcetMessage_list.size()==1){
                    signo_button.setText("签退");
                    signo_in_time_hour.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_date());
                    signo_in_time_day.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_time());
                    signo_out_time_day.setText("");
                    signo_out_time_hour.setText("");
                }else{
                    if(meifuoaApplication.trajcetMessage_list.get(1).getType().toString().equals("2")){
                        signo_button.setText("签退");
                        signo_in_time_hour.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_date());
                        signo_in_time_day.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_time());
                        signo_out_time_day.setText("");
                        signo_out_time_hour.setText("");
                    }else{
                        signo_button.setText("签到");
                        signo_in_time_hour.setText(meifuoaApplication.trajcetMessage_list.get(1).getT_date());
                        signo_in_time_day.setText(meifuoaApplication.trajcetMessage_list.get(1).getT_time());
                        signo_out_time_hour.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_date());
                        signo_out_time_day.setText(meifuoaApplication.trajcetMessage_list.get(0).getT_time());
                    }
                }
            }
        }
    }

    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(meifuoaApplication.Broadcase_name);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(meifuoaApplication.Broadcase_name)){
                String rece_type=intent.getExtras().getString("online_time");
                signo_out_time_online.setText(rece_type);
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
