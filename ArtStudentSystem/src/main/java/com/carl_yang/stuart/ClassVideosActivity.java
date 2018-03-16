package com.carl_yang.stuart;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.ArtStudentApplication;
import com.carl_yang.adapters.FavoriteAdapter;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyDialog;
import com.carl_yang.drawfile.view.CustomerFooter;
import com.carl_yang.httpintent.domain.CollectClassDomain;
import com.carl_yang.httpintent.domain.VideoShowDomain;
import com.carl_yang.interfaces.RecycleViewInterface;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ClassVideosActivity extends AppCompatActivity {

    @BindView(R.id.favorite_back)
    LinearLayout favoriteBack;
    @BindView(R.id.favorite_temporary)
    TextView favoriteTemporary;
    @BindView(R.id.favorite_recycler)
    RecyclerView favoriteRecycler;
    @BindView(R.id.favorite_xrefreshview)
    XRefreshView favoriteXrefreshview;
    @BindView(R.id.favorite_video_bg)
    ImageView favoriteVideoBg;
    @BindView(R.id.video_prepared)
    TextView videoPrepared;
    @BindView(R.id.favorite_video)
    VideoView favoriteVideo;
    @BindView(R.id.favorite_video_framelayout)
    FrameLayout favoriteVideoFramelayout;
    private MyDialog dialog;

    private List<VideoShowDomain> perColl_list;
    private FavoriteAdapter faAdapter;
    GridLayoutManager layoutManager;
    private int pageNum = 1;//当前获取的页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_videos);
        ButterKnife.bind(this);

        dialog = Common.initUploadingDialog(dialog, this);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

        initView();
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
        favoriteXrefreshview.setPullLoadEnable(false);
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
                        getClassResuourse();
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
                    MediaController mediaController = new MediaController(ClassVideosActivity.this);
                    mediaController.show(8000);
                    favoriteVideo.setMediaController(mediaController);
                    favoriteVideo.setOnPreparedListener(new MyVideoPrepared());
                    favoriteVideo.setVideoPath(ArtStudentApplication.upVersionUrl+"ART/" + perColl_list.get(postion).getV_url());
                    favoriteVideoBg.setVisibility(View.VISIBLE);
                    favoriteVideoFramelayout.setVisibility(View.VISIBLE);
                    videoPrepared.setVisibility(View.VISIBLE);
                    videoPrepared.bringToFront();
                    favoriteVideo.start();
                }
            }
        });

        favoriteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getClassResuourse();
    }

    class MyVideoPrepared implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("加载完成。。。。");
            videoPrepared.setVisibility(View.GONE);
        }
    }

    public void getClassResuourse() {
        System.out.println("---teachId----" + ArtStudentApplication.CTR_ID);
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "open/findCurrClassroomRecourse")//
                .addParams("ctrId", ArtStudentApplication.CTR_ID)//
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
                                    VideoShowDomain pc = new VideoShowDomain();
                                    pc.setP_url(cur.getTcr_pic());
                                    pc.setV_url(cur.getTcr_url());
                                    pc.setType(cur.getType());
                                    pc.setIamgename(cur.getTcr_name());
                                    pc.setAuth("");
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
        if (favoriteVideoFramelayout.getVisibility() == View.VISIBLE) {
            favoriteVideoFramelayout.setVisibility(View.GONE);
            favoriteVideo.pause();
            favoriteVideo.setMediaController(null);
            favoriteRecycler.setEnabled(true);
        } else {
            super.onBackPressed();
        }
    }
}
