package com.carl_yang.art;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.andview.refreshview.XRefreshView;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.adapter.FavoriteAdapter;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.domain.CollectClassDomain;
import com.carl_yang.domain.CollectDdomain;
import com.carl_yang.domain.PersonCollect;
import com.carl_yang.interfacepac.RecycleViewInterface;
import com.carl_yang.util.Common;
import com.carl_yang.util.MyUtils;
import com.carl_yang.view.CustomerFooter;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteActivity";
    @BindView(R.id.favorite_spinner)
    Spinner favoriteSpinner;
    @BindView(R.id.favorite_totalnum)
    TextView favoriteTotalnum;
    @BindView(R.id.favorite_linearlayout)
    LinearLayout favoriteLinearlayout;
    @BindView(R.id.favorite_bg)
    ImageView favoriteBg;
    @BindView(R.id.favorite_photoview)
    PhotoView favoritePhotoview;
    @BindView(R.id.favorite_imagename)
    TextView favoriteImagename;
    @BindView(R.id.favorite_auth)
    TextView favoriteAuth;
    @BindView(R.id.favorite_desc)
    TextView favoriteDesc;
    @BindView(R.id.favorite_framelayout)
    FrameLayout favoriteFramelayout;
    @BindView(R.id.favorite_recycler)
    RecyclerView favoriteRecycler;
    @BindView(R.id.favorite_xrefreshview)
    XRefreshView favoriteXrefreshview;
    @BindView(R.id.favorite_teacher)
    TextView favoriteTeacher;
    @BindView(R.id.favorite_temporary)
    TextView favoriteTemporary;
    @BindView(R.id.favorite_video_bg)
    ImageView favoriteVideoBg;
    @BindView(R.id.favorite_video)
    VideoView favoriteVideo;
    @BindView(R.id.favorite_video_framelayout)
    FrameLayout favoriteVideoFramelayout;
    @BindView(R.id.video_prepared)
    TextView videoPrepared;
    @BindView(R.id.favorite_recording)
    ImageView favoriteRecording;

    private List<PersonCollect> perColl_list;
    private String[] level = {};

    private int pageNum = 1;//当前获取的页码
    private String pos_type = "";//当前选择的筛选条件

    private FavoriteAdapter faAdapter;
    GridLayoutManager layoutManager;

    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        dialog = Common.initUploadingDialog(dialog, this);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        ArtTeachiingApplication.getmInstance().addActivity(this);

        initView();
        initSpinner();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(favoriteLinearlayout.getVisibility()==View.GONE) {
            perColl_list.clear();
            faAdapter.notifyDataSetChanged();
            pageNum = 1;
            getClassResuourse(ArtTeachiingApplication.user_id, pageNum + "", "9");
            faAdapter.notifyDataSetChanged();
        }
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item);
        level = getResources().getStringArray(R.array.collect_filter);//资源文件
        for (int i = 0; i < level.length; i++) {
            adapter.add(level[i]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        favoriteSpinner.setAdapter(adapter);
        favoriteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //重新获取接口数据改变当前视图，（帅选条件：）同时改变统计
                perColl_list.clear();
                pageNum = 1;
                if (position == 0) {
                    pos_type = "";
                } else {
                    pos_type = position + "";
                }
                personCollect(ArtTeachiingApplication.user_id, pos_type, "9", pageNum + "", "1", 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        perColl_list = new ArrayList<>();

        favoriteXrefreshview.setPullLoadEnable(true);
        favoriteRecycler.setHasFixedSize(true);
        faAdapter = new FavoriteAdapter(perColl_list, this);
        layoutManager = new GridLayoutManager(this, 3);
        favoriteRecycler.setLayoutManager(layoutManager);
        favoriteRecycler.setAdapter(faAdapter);
        favoriteXrefreshview.setPinnedTime(1000);
        favoriteXrefreshview.setMoveForHorizontal(true);
        CustomerFooter customerFooter = new CustomerFooter(this);
        customerFooter.setRecyclerView(favoriteRecycler);
        faAdapter.setCustomLoadMoreView(customerFooter);
        favoriteXrefreshview.setPullRefreshEnable(false);
        favoriteXrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        favoriteXrefreshview.stopRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageNum++;
                        if (favoriteLinearlayout.getVisibility() == View.VISIBLE) {
                            personCollect(ArtTeachiingApplication.user_id, pos_type, "9", pageNum + "", "1", 1);
                        } else {
                            getClassResuourse(ArtTeachiingApplication.user_id, pageNum + "", "9");
                        }
                    }
                }, 1000);
            }
        });
        faAdapter.setmOnItemOnClickListener(new RecycleViewInterface.OnItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, int postion) {
                System.out.println("Click " + postion);
                favoriteRecycler.setEnabled(true);
                if (perColl_list.get(postion).getType().equals("1")) {
                    MediaController mediaController = new MediaController(FavoriteActivity.this);
                    mediaController.show(8000);
                    favoriteVideo.setMediaController(mediaController);
                    favoriteVideo.setOnPreparedListener(new MyVideoPrepared());
                    if (perColl_list.get(postion).getGet_type() == 0) {
                        favoriteVideo.setVideoPath(ArtTeachiingApplication.imageUrlFront + perColl_list.get(postion).getV_url());
                    } else {
                        favoriteVideo.setVideoPath(ArtTeachiingApplication.localUPversion+"ART/" + perColl_list.get(postion).getV_url());
                    }
                    favoriteVideoBg.setVisibility(View.VISIBLE);
                    favoriteVideoFramelayout.setVisibility(View.VISIBLE);
                    videoPrepared.setVisibility(View.VISIBLE);
                    videoPrepared.bringToFront();
                    favoriteVideo.start();
                } else {
                    String p_url = ArtTeachiingApplication.imageUrlFront + perColl_list.get(postion).getP_url() + ".jpg";
                    Glide
                            .with(FavoriteActivity.this)
                            .load(p_url)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .crossFade()
                            .dontAnimate()
                            .into(favoritePhotoview);
                    favoriteImagename.setText(perColl_list.get(postion).getIamgename());
                    favoriteAuth.setText(perColl_list.get(postion).getAuth());
                    favoriteDesc.setText(perColl_list.get(postion).getContent());
                    favoriteBg.setVisibility(View.VISIBLE);
                    favoriteFramelayout.setVisibility(View.VISIBLE);
                }
            }
        });
        favoritePhotoview.enable();
        favoritePhotoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteFramelayout.setVisibility(View.GONE);
                favoriteRecycler.setEnabled(true);
            }
        });
    }

    public void favorite_click(View v) {
        switch (v.getId()) {
            case R.id.favorite_back:
                this.finish();
                break;
            case R.id.favorite_teacher:
                Log.d(TAG, "favorite_click: teacher");
                if (favoriteLinearlayout.getVisibility() == View.VISIBLE) return;
                favoriteTeacher.setBackground(getResources().getDrawable(R.drawable.title_bg1_h));
                favoriteTeacher.setTextColor(getResources().getColor(R.color.blue));
                favoriteTemporary.setBackground(getResources().getDrawable(R.drawable.title_bg3));
                favoriteTemporary.setTextColor(getResources().getColor(R.color.white));
                favoriteLinearlayout.setVisibility(View.VISIBLE);
                favoriteRecording.setVisibility(View.GONE);
                perColl_list.clear();
                faAdapter.notifyDataSetChanged();
                initSpinner();
                break;
            case R.id.favorite_temporary:
                if (favoriteLinearlayout.getVisibility() == View.GONE) return;
                Log.d(TAG, "favorite_click: temporary");
                favoriteTeacher.setBackground(getResources().getDrawable(R.drawable.title_bg1));
                favoriteTeacher.setTextColor(getResources().getColor(R.color.white));
                favoriteTemporary.setBackground(getResources().getDrawable(R.drawable.title_bg3_h));
                favoriteTemporary.setTextColor(getResources().getColor(R.color.blue));
                favoriteLinearlayout.setVisibility(View.GONE);
                favoriteRecording.setVisibility(View.VISIBLE);
                perColl_list.clear();
                faAdapter.notifyDataSetChanged();
                pageNum = 1;
                getClassResuourse(ArtTeachiingApplication.user_id, pageNum + "", "9");
                faAdapter.notifyDataSetChanged();
                break;
            case R.id.favorite_recording:
                Intent intetn_record = new Intent(FavoriteActivity.this, RecordVideoActivity.class);
                startActivity(intetn_record);
                break;
        }
    }

    public void personCollect(String userId, String awt_id, String limit, String currPage, String time, final int type) {
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "apppadCollection.findTeacherCollectionFolderList.html")//
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
                        System.out.println("=========" + string);
                        CollectDdomain user = new Gson().fromJson(string, CollectDdomain.class);
                        return user;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        System.out.println("访问失败");
                        favoriteXrefreshview.stopLoadMore(false);
//                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(CollectDdomain response, int id) {
                        dialog.cancel();
                        if (response.getMap().size() != 0) {
                            for (int i = 0; i < response.getMap().size(); i++) {
                                String pUrl = response.getMap().get(i).getpUrl();
                                String vUrl = response.getMap().get(i).getvUrl();
                                String wName = response.getMap().get(i).getwName();
                                String wDesc = response.getMap().get(i).getwDesc();
                                String type = response.getMap().get(i).getType();
                                System.out.println("=========" + pUrl);
                                PersonCollect pc = new PersonCollect();
                                pc.setP_url(pUrl);
                                pc.setV_url(vUrl);
                                pc.setType(type);
                                pc.setIamgename(wName);
                                pc.setAuth("");
                                pc.setGet_type(0);
                                pc.setContent(wDesc);
                                perColl_list.add(pc);
                            }
                            favoriteTotalnum.setText(Html.fromHtml("总共有" + MyUtils.color("#44abed", String.valueOf(perColl_list.size())) + "本书"));
                            faAdapter.notifyDataSetChanged();
                            if (type == 0) favoriteXrefreshview.setLoadComplete(false);
                            if (type == 1) favoriteXrefreshview.stopLoadMore();
                        } else {
                            if (type == 1) {
                                favoriteXrefreshview.setLoadComplete(true);
                            }
                        }
                    }
                });
    }

    /**
     * 获取教师的课堂资源
     *
     * @param userId
     * @param currPage
     * @param limit
     */
    public void getClassResuourse(String userId, String currPage, String limit) {
        System.out.println("---teachId----" + userId);
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/findTeacherClassRecourse")//
                .addParams("teacId", userId)//
                .addParams("currPage", currPage)//
                .addParams("limit", limit)//
                .build()//
                .execute(new Callback<CollectClassDomain>() {
                    @Override
                    public CollectClassDomain parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        System.out.println("====getClassResuourse=====" + string);
                        CollectClassDomain ccd = new Gson().fromJson(string, CollectClassDomain.class);
                        return ccd;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        System.out.println("访问失败");
                        favoriteXrefreshview.stopLoadMore(false);
                    }

                    @Override
                    public void onResponse(CollectClassDomain response, int id) {
                        dialog.cancel();
                        if(response.getList()!=null) {
                            if (response.getList().size() != 0) {
                                for (int i = 0; i < response.getList().size(); i++) {
                                    CollectClassDomain.Collect_List cur = response.getList().get(i);
                                    PersonCollect pc = new PersonCollect();
                                    pc.setP_url(cur.getTcr_pic());
                                    pc.setV_url(cur.getTcr_url());
                                    pc.setType(cur.getType());
                                    pc.setIamgename(cur.getTcr_name());
                                    pc.setAuth("");
                                    pc.setGet_type(1);
                                    pc.setContent(cur.getTcr_desc());
                                    perColl_list.add(pc);
                                }
                                faAdapter.notifyDataSetChanged();
                                favoriteXrefreshview.stopLoadMore();
                            } else {
                                favoriteXrefreshview.setLoadComplete(true);
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (favoriteFramelayout.getVisibility() == View.VISIBLE) {
            favoriteFramelayout.setVisibility(View.GONE);
            favoriteRecycler.setEnabled(true);
        } else if (favoriteVideoFramelayout.getVisibility() == View.VISIBLE) {
            favoriteVideoFramelayout.setVisibility(View.GONE);
            favoriteVideo.pause();
            favoriteVideo.setMediaController(null);
        } else {
            super.onBackPressed();
        }
    }

    class MyVideoPrepared implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("加载完成。。。。");
            videoPrepared.setVisibility(View.GONE);
        }
    }
}