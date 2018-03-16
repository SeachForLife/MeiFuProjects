package com.carl_yang.stuart;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtStudentApplication;
import com.carl_yang.common.Common;
import com.carl_yang.common.MyDialog;
import com.carl_yang.drawfile.adapter.CommentAdapter;
import com.carl_yang.httpintent.domain.CommentDomain;
import com.carl_yang.httpintent.domain.UpVersionReturn;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class TeacherCommentActivity extends Activity {

    private static final String TAG = "TeacherCommentActivity";
    @BindView(R.id.teacher_comment_num)
    TextView teacherCommentNum;
    @BindView(R.id.teacher_comment_listview)
    ListView teacherCommentListview;

    private MyDialog dialog;
    private CommentAdapter adapter;
    private List<CommentDomain.Comment_content> cList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_comment);
        ButterKnife.bind(this);

        dialog= Common.initUploadingDialog(dialog, TeacherCommentActivity.this);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        ArtStudentApplication.getmInstance().addActivity(this);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = 800 * w_screen / 1280;
        layoutParams.height = 600 * h_screen / 800;
        layoutParams.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(layoutParams);

        searchCommentM();
    }

    //查询点评记录
    private void searchCommentM(){
        dialog.show();
        dialog.changeInfo("加载中...");
        OkHttpUtils
                .post()//
                .url(ArtStudentApplication.commonUrl + "open/findStudentWorkCommentList")//
                .addParams("stu_no", ArtStudentApplication.USER_ID)
                .addParams("ctr_id", ArtStudentApplication.CTR_ID)
                .build()//
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
            dialog.cancel();
            Log.d(TAG, "onError: ");
            Toast.makeText(TeacherCommentActivity.this," 加载失败",Toast.LENGTH_LONG).show();
            TeacherCommentActivity.this.finish();
        }

        @Override
        public void onResponse(String response, int id) {
            dialog.cancel();
            Log.d(TAG, "onResponse: "+response);
            CommentDomain cd = new Gson().fromJson(response, CommentDomain.class);
            teacherCommentNum.setText(cd.getVoteCount());
            cList=cd.getcList();
            Log.i(TAG, "onResponse: "+cList.size());
            adapter=new CommentAdapter(TeacherCommentActivity.this,cList);
            teacherCommentListview.setAdapter(adapter);
            teacherCommentListview.setDivider(new ColorDrawable(Color.GRAY));
            teacherCommentListview.setDividerHeight(1);
        }
    }
}
