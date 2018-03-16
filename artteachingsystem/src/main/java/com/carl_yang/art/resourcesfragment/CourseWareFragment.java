package com.carl_yang.art.resourcesfragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.adapter.GridViewAdapter;
import com.carl_yang.art.CourseWareGridActivity;
import com.carl_yang.art.R;
import com.carl_yang.domain.courseWareProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseWareFragment extends Fragment {

    private GridView course_ware_gridview;
    private GridViewAdapter gvAdapter;

    courseWareProperty cp;

    public CourseWareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_ware, container, false);
        course_ware_gridview= (GridView) view.findViewById(R.id.course_ware_gridview);

         cp=courseWareProperty.getmInstance();
        List<String> mCoverStringNameslist=new ArrayList<>();
        List<Integer> mCoverImageIdslist=new ArrayList<>();

        if(ArtTeachiingApplication.GRID_EXIST!=null) {
//            ArtTeachiingApplication.GRID_EXIST.add(7);
//            ArtTeachiingApplication.GRID_EXIST.add(8);
//            ArtTeachiingApplication.GRID_EXIST.add(9);
            for (int i = 0; i < ArtTeachiingApplication.GRID_EXIST.size(); i++) {
                mCoverStringNameslist.add(cp.getmCoverNameMap().get((2 * ArtTeachiingApplication.GRID_EXIST.get(i) - 1)));
                mCoverStringNameslist.add(cp.getmCoverNameMap().get(2 * ArtTeachiingApplication.GRID_EXIST.get(i)));
                mCoverImageIdslist.add(cp.getmCoverIdMap().get((2 * ArtTeachiingApplication.GRID_EXIST.get(i) - 1)));
                mCoverImageIdslist.add(cp.getmCoverIdMap().get(2 * ArtTeachiingApplication.GRID_EXIST.get(i)));
            }
        }
        String[] mCoverStringNames=new String[mCoverStringNameslist.size()];
        Integer[] mCoverImageIds=new Integer[mCoverImageIdslist.size()];
        for(int m=0;m<mCoverStringNameslist.size();m++){
            mCoverStringNames[m]=mCoverStringNameslist.get(m);
            mCoverImageIds[m]=mCoverImageIdslist.get(m);
        }
        cp.setmCoverStringNames(mCoverStringNames);
        cp.setmCoverImageIds(mCoverImageIds);

        gvAdapter = new GridViewAdapter(getActivity(), "cover",cp);
        course_ware_gridview.setAdapter(gvAdapter);
        course_ware_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index =i+1;
//                System.out.println("按下了"+index+"::"+(ArtTeachiingApplication.GRID_EXIST.get(i/2)*2-2+i%2+1));
//                if (index < 8) {
                    Intent intent = new Intent(getActivity(), CourseWareGridActivity.class);
                    intent.putExtra("msg", cp.getmCoverStringNames()[i]);
                    intent.putExtra("clicknum", (ArtTeachiingApplication.GRID_EXIST.get(i/2)*2-2+i%2+1) + "");
                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), "当前课程还没有内容!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        return view;
    }

}
