package com.carl_yang.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.carl_yang.drawfile.adapter.BackgroundAdapter;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.property.DrawProperty;
import com.carl_yang.stuart.DrawActivity;
import com.carl_yang.stuart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackgroundFragment extends Fragment {

    private GridView background_fragment_gridview;
    private BackgroundAdapter bgAdapter;

    public BackgroundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_background, container, false);
        background_fragment_gridview= (GridView) view.findViewById(R.id.background_fragment_gridview);
        bgAdapter=new BackgroundAdapter(getActivity(),getActivity());
        background_fragment_gridview.setAdapter( bgAdapter );
        background_fragment_gridview .setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println("背景："+i);
                Intent mIntent = new Intent(DrawProperty.Broadcase_name);
                mIntent.putExtra("type","background");
                mIntent.putExtra("index",i+"");
                getActivity().sendBroadcast(mIntent);
                getActivity().finish();
            }
        });
        return view;
    }

}
