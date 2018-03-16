package com.carl_yang.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.carl_yang.drawfile.adapter.StampPickAdapter;
import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.property.DrawProperty;
import com.carl_yang.stuart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StampPickerFragment extends Fragment {

    private GridView stamp_fragment_gridview;
    private StampPickAdapter stampPickAdapter;

    private Integer[] mStampIds = {
            R.drawable.yz1 , R.drawable.yz2 ,R.drawable.yz3 , R.drawable.yz4 ,
            R.drawable.yz5 , R.drawable.yz6 ,R.drawable.yz7 , R.drawable.yz8 ,
            R.drawable.yz9 , R.drawable.yz10 ,R.drawable.yz11,  R.drawable.yz12,
            R.drawable.yz13,  R.drawable.yz14, R.drawable.yz15,  R.drawable.yz16,
            R.drawable.yz17,  R.drawable.yz18,R.drawable.yz19,  R.drawable.yz20,
            R.drawable.yz21
    };

    public StampPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_stamp_picker, container, false);
        stamp_fragment_gridview= (GridView) view.findViewById(R.id.stamp_fragment_gridview);
        stampPickAdapter=new StampPickAdapter(getActivity());
        stamp_fragment_gridview.setAdapter( stampPickAdapter );
        stamp_fragment_gridview .setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println("印章："+i);
                DrawProperty.STAMP_ID=mStampIds[i];
                Intent mIntent = new Intent(DrawProperty.Broadcase_name);
                mIntent.putExtra("type","stamp");
                mIntent.putExtra("index",i+"");
                mIntent.putExtra("stamp_id",mStampIds[i]);
                getActivity().sendBroadcast(mIntent);
                getActivity().finish();
            }
        });
        return view;
    }

}
