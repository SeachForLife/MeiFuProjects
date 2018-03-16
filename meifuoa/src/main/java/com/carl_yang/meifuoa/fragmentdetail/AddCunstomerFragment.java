package com.carl_yang.meifuoa.fragmentdetail;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.carl_yang.meifuoa.R;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.tools.Common;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class AddCunstomerFragment extends Fragment {

    private TextView add_customer_currentAdr;
    private EditText add_customer_newAdr;

    //建立一个handler用来实时刷新当前页面的地址
    private Handler handler=new Handler();

    public AddCunstomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_cunstomer, container, false);
        add_customer_currentAdr= (TextView) view.findViewById(R.id.add_customer_currentAdr);
        add_customer_newAdr= (EditText) view.findViewById(R.id.add_customer_newAdr);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        handler.post(updateAdrRunnable);
    }

    private Runnable updateAdrRunnable=new Runnable() {
        @Override
        public void run() {
            add_customer_currentAdr.setText("当前地址:"+meifuoaApplication.current_customer_adr+"");
            handler.postDelayed(this,1000);//1S刷新一次当前页面
        }
    };

}
