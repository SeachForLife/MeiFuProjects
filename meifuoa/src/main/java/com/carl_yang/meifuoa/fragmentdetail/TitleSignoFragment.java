package com.carl_yang.meifuoa.fragmentdetail;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carl_yang.meifuoa.R;
import com.carl_yang.tools.SharedPreferencesUtils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TitleSignoFragment extends Fragment {

    private ImageView signo_add_image;
    private LinearLayout signo_add;
    private Context context;

    public TitleSignoFragment(){

    }

    public TitleSignoFragment(Context cx) {
        this.context=cx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_title_signo, container, false);
        signo_add = (LinearLayout) view.findViewById(R.id.signo_add);
        signo_add_image= (ImageView) view.findViewById(R.id.signo_add_image);
        String DEPARTMENT_ID= SharedPreferencesUtils.getInstance(context).read("add_customer","");
        if(DEPARTMENT_ID.equals("1")){
            signo_add.setClickable(true);
            signo_add_image.setVisibility(View.VISIBLE);
        }else{
            signo_add.setClickable(false);
            signo_add_image.setVisibility(View.GONE);
        }
        return view;

    }

}
