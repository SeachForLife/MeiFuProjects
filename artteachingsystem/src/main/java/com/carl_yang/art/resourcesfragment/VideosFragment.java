package com.carl_yang.art.resourcesfragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.adapter.VideosGridViewAdapter;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.art.R;
import com.carl_yang.domain.CollectDdomain;
import com.carl_yang.domain.PersonCollect;
import com.carl_yang.util.Common;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment {


    private GridView videos_fragment_gridview;
    private VideosGridViewAdapter gvAdapter;

    private Spinner spinner;
    private TextView videos_fragment_totalnum;
    private List<PersonCollect> perColl_list;
    private String[] level={};

    private FrameLayout videos_fragment_framelayout;
    private LinearLayout videos_fragment_linearlayout;
    private PhotoView videos_fragment_photoview;
    private TextView videos_fragment_imagename;
    private TextView videos_fragment_auth;
    private TextView videos_fragment_desc;
    private ImageView videos_fragment_bg;

//    private Info mInfo ;

    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    private MyDialog dialog;

    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_videos, container, false);

        in.setDuration(100);
        out.setDuration(100);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                videos_fragment_bg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dialog = Common.initUploadingDialog(dialog, getActivity());
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        videos_fragment_gridview= (GridView) view.findViewById(R.id.videos_fragment_gridview);
        spinner= (Spinner) view.findViewById(R.id.videos_fragment_spinner);
        videos_fragment_totalnum= (TextView) view.findViewById(R.id.videos_fragment_totalnum);
        videos_fragment_framelayout= (FrameLayout) view.findViewById(R.id.videos_fragment_framelayout);
        videos_fragment_linearlayout= (LinearLayout) view.findViewById(R.id.videos_fragment_linearlayout);
        videos_fragment_photoview= (PhotoView) view.findViewById(R.id.videos_fragment_photoview);
        videos_fragment_imagename= (TextView) view.findViewById(R.id.videos_fragment_imagename);
        videos_fragment_auth= (TextView) view.findViewById(R.id.videos_fragment_auth);
        videos_fragment_desc= (TextView) view.findViewById(R.id.videos_fragment_desc);
        videos_fragment_bg= (ImageView) view.findViewById(R.id.videos_fragment_bg);

        videos_fragment_photoview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item);
        level = getResources().getStringArray(R.array.collect_filter);//资源文件
        for (int i = 0; i < level.length; i++) {
            adapter.add(level[i]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //重新获取接口数据改变当前视图，（帅选条件：）同时改变统计
                perColl_list.clear();
                if(position==0) {
                    personCollect(ArtTeachiingApplication.user_id, "", "50", "1", "1");
                }else{
                    personCollect(ArtTeachiingApplication.user_id, position+"", "50", "1", "1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        perColl_list=new ArrayList<>();
        gvAdapter=new VideosGridViewAdapter(getActivity(),perColl_list);
        videos_fragment_gridview.setAdapter(gvAdapter);
        videos_fragment_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index =i+1;
                System.out.println("按下了"+index);
                videos_fragment_gridview.setEnabled(false);

//                Glide
//                        .with(VideosFragment.this)
//                        .load(perColl_list.get(i).getLarge_url())
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .crossFade()
//                        .dontAnimate()
//                        .into(videos_fragment_photoview);

                videos_fragment_bg.setVisibility(View.VISIBLE);
                videos_fragment_framelayout.setVisibility(View.VISIBLE);;
            }
        });

        videos_fragment_photoview.enable();
        videos_fragment_photoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videos_fragment_framelayout.setVisibility(View.GONE);
                videos_fragment_gridview.setEnabled(true);
            }
        });

        return view;
    }

    private String color(String colorCode, String str) {
        return "<font color=\"" + colorCode + "\">" + str + "</font>";
    }

    public void personCollect(String userId, String awt_id,String limit,String currPage,String time) {
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "appteaCollection.findCollectionList.html")//
                .addParams("userId", userId)//
                .addParams("form.awt_id", awt_id)//
                .addParams("form.limit", limit)//
                .addParams("form.currPage", currPage)//
                .addParams("form.time", time)//
                .build()//
                .execute(new Callback<CollectDdomain>() {
                    @Override
                    public CollectDdomain parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        CollectDdomain user = new Gson().fromJson(string, CollectDdomain.class);
                        return user;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        System.out.println("访问失败");
                    }

                    @Override
                    public void onResponse(CollectDdomain response, int id) {
                        dialog.cancel();
                        if(response.getMap().size()!=0){
                            for(int i=0;i<response.getMap().size();i++){
//                                String aw_pic=response.getMap().get(i).getAw_pic();
//                                String aw_name=response.getMap().get(i).getAw_name();
//                                String cn_name=response.getMap().get(i).getCn_name();
//                                String aw_desc=response.getMap().get(i).getAw_desc();
//                                System.out.println("===dfa="+aw_pic+"--"+aw_name);
//                                PersonCollect pc=new PersonCollect();
//                                pc.setUrl(ArtTeachiingApplication.imageUrlFront+aw_pic+"_320.jpg");
//                                pc.setLarge_url(ArtTeachiingApplication.imageUrlFront+aw_pic+".jpg");
//                                pc.setIamgename(aw_name);
//                                pc.setAuth(cn_name);
//                                pc.setContent(aw_desc);
//                                perColl_list.add(pc);
                            }
                            videos_fragment_totalnum.setText(Html.fromHtml("总共有"+color("#44abed", String.valueOf(perColl_list.size()))+"本书"));
                            gvAdapter.notifyDataSetChanged();
                        }
                    }

                });
    }

}
