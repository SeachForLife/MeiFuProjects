package com.carl_yang.art.resourcesfragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.carl_yang.adapter.GridViewAdapter;
import com.carl_yang.art.CourseContentActivity;
import com.carl_yang.art.R;
import com.carl_yang.domain.courseWareProperty;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseWareContentFragment extends Fragment {

    private GridView course_ware_content_gridview;
    private GridViewAdapter gvAdapter;

    private String choose_grade;

    public CourseWareContentFragment(){

    }

    @SuppressLint("ValidFragment")
    public CourseWareContentFragment(String choose_grade) {
        this.choose_grade=choose_grade;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_ware, container, false);
        course_ware_content_gridview= (GridView) view.findViewById(R.id.course_ware_gridview);

        courseWareProperty cp=courseWareProperty.getmInstance();
        gvAdapter = new GridViewAdapter(getActivity(), choose_grade,cp);
        if(gvAdapter.getCount()==0) Toast.makeText(getActivity(),"当前学期还未有课件资源~",Toast.LENGTH_LONG).show();
        course_ware_content_gridview.setAdapter(gvAdapter);
        course_ware_content_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index =i+1;
                System.out.println("按下了"+index);
                Intent intent=new Intent(getActivity(), CourseContentActivity.class);
                intent.putExtra("choose_grade", choose_grade);
                intent.putExtra("choose_courseware", index + "");
                startActivity(intent);
            }
        });

        return view;
    }

}
