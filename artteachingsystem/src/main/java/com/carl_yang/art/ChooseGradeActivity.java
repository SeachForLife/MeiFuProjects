package com.carl_yang.art;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carl_yang.ArtTeachiingApplication;
import com.carl_yang.alarmdialog.MyDialog;
import com.carl_yang.domain.ClassCombo;
import com.carl_yang.domain.GradeCombo;
import com.carl_yang.domain.addClassRoomStudent;
import com.carl_yang.service.CountTimeService;
import com.carl_yang.util.Common;
import com.carl_yang.util.MyUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ChooseGradeActivity extends Activity {

    private ListView choose_grade;
    private ListView choose_class;
    private LinearLayout choose_class_layout;
    private TextView choose_next;
    private TextView choose_grade_title;
    private List<String> list_grade;
    private List<String> list_class;
    private Map<String,String> gradeMap;//年级名字主键 id值
    private Map<String , String> classMap;//班级名字主键  id值

    private ProgramAdapter adapter;
    private int GRADE_CHOOSE=-1;//当前选择的年级
    private int CLASS_CHOOSE=-1;//当前选择的班级
    private int GRADE_CHOOSE_POSTION=0;
    private int CLASS_CHOOSE_POSTION=0;

    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_grade);
        ArtTeachiingApplication.getmInstance().addActivity(this);
        DisplayMetrics dm=this.getResources().getDisplayMetrics();
        int w_screen=dm.widthPixels;
        int h_screen=dm.heightPixels;
        android.view.WindowManager.LayoutParams layoutParams=this.getWindow().getAttributes();
        layoutParams.width=468*w_screen/1280;
        layoutParams.height=568*h_screen/800;
        this.getWindow().setAttributes(layoutParams);
        dialog= Common.initUploadingDialog(dialog, ChooseGradeActivity.this);
        inits();
    }

    private void inits(){
//        list_grade=new ArrayList<String>();
//        list_grade.add("一年级");
//        list_grade.add("二年级");
//        list_grade.add("三年级");
//        list_grade.add("四年级");
//        list_grade.add("五年级");
//        list_grade.add("六年级");
//
//        list_class=new ArrayList<String>();
//        list_class.add("1班");
//        list_class.add("2班");
//        list_class.add("3班");
//        list_class.add("4班");
//        list_class.add("5班");
//        list_class.add("6班");

        choose_grade= (ListView) findViewById(R.id.choose_grade);
        choose_class= (ListView) findViewById(R.id.choose_class);
        choose_class_layout= (LinearLayout) findViewById(R.id.choose_class_layout);
        choose_next= (TextView) findViewById(R.id.choose_next);
        choose_next.setEnabled(false);
        choose_next.setBackground(getResources().getDrawable(R.drawable.no_click));
        choose_grade_title= (TextView) findViewById(R.id.choose_grade_title);

        list_grade=new ArrayList<>();
        findGradeCombo();
        choose_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choose_grade_title.setText("选择班级");
                GRADE_CHOOSE_POSTION=(i-2)>0?(i-2):0;
                GRADE_CHOOSE=i;
                CLASS_CHOOSE=-1;
                adapter = new ProgramAdapter(list_grade,"grade_choose");
                choose_grade.setAdapter(adapter);
                choose_grade.setSelection(GRADE_CHOOSE_POSTION);
                choose_next.setEnabled(false);
                choose_next.setBackground(getResources().getDrawable(R.drawable.no_click));
                String choose_grade_value=list_grade.get(i).toString();
                ArtTeachiingApplication.GRADE_CHOOSE_ID=gradeMap.get(choose_grade_value).toString();
                ArtTeachiingApplication.CLASS_CHOOSE_ID="";
                findClassCombo(ArtTeachiingApplication.GRADE_CHOOSE_ID);
                choose_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CLASS_CHOOSE_POSTION=(i-2)>0?(i-2):0;
                        CLASS_CHOOSE=i;
                        String choose_classname=list_class.get(i).toString();
                        ArtTeachiingApplication.CLASS_CHOOSE_ID=classMap.get(choose_classname).toString();
                        adapter = new ProgramAdapter(list_class,"class_choose");
                        choose_class.setAdapter(adapter);
                        choose_class.setSelection(CLASS_CHOOSE_POSTION);
                        choose_next.setEnabled(true);
                        choose_next.setBackground(getResources().getDrawable(R.drawable.bg7));
                    }
                });
            }
        });

        choose_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ArtTeachiingApplication.CLASS_CHOOSE_ID.equals("")){
                    Toast.makeText(getApplicationContext(),"请选择一个班级进行教学!",Toast.LENGTH_LONG).show();
                }else{
                    addClassRoomStudent();
                }
            }
        });
    }




    static class ViewHolder {
        TextView choose_id;
        ImageView choose_image;
    }

    class ProgramAdapter extends BaseAdapter {

        private List<String> grad_list = new ArrayList<String>();
        private String current_type;

        public ProgramAdapter(List<String> list_grade,String choose_type) {
            current_type=choose_type;
            for (String grade : list_grade) {
                grad_list.add(grade);
            }
        }

        @Override
        public int getCount() {
            return grad_list.size();
        }

        @Override
        public Object getItem(int position) {
            // fixme
            // 判断下， 防止数组越界
            if (position < 0 || position >= grad_list.size()) {
                return null;
            }
            return grad_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            try {
                if (convertView == null) {
                    view = View.inflate(ChooseGradeActivity.this,
                            R.layout.choosegrade, null);
                    holder = new ViewHolder();
                    holder.choose_id = (TextView) view.findViewById(R.id.grade_id);
                    holder.choose_image = (ImageView) view.findViewById(R.id.grade_image);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                final ViewHolder holder1=holder;
                holder.choose_id.setText(grad_list.get(position));
                if(current_type.equals("grade_choose")) {
                    if (GRADE_CHOOSE != -1) {
                        if (position == GRADE_CHOOSE) {
                            holder.choose_image.setVisibility(View.VISIBLE);
                        } else {
                            holder.choose_image.setVisibility(View.GONE);
                        }
                    } else {
                        holder.choose_image.setVisibility(View.GONE);
                    }
                }else{
                    if (CLASS_CHOOSE != -1) {
                        if (position == CLASS_CHOOSE) {
                            holder.choose_image.setVisibility(View.VISIBLE);
                        } else {
                            holder.choose_image.setVisibility(View.GONE);
                        }
                    } else {
                        holder.choose_image.setVisibility(View.GONE);
                    }
                }

            } catch (Exception swallow) {
            }
            return view;
        }

    }

    //获取班级列表
    private void findClassCombo(String grade_id) {
        dialog.show();
        dialog.changeInfo("班级获取中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "teachingCombo.findClassCombo.html")//
                .addParams("school_id", ArtTeachiingApplication.school_id)//
                .addParams("grade_id",grade_id)
                .build()//
                .execute(new Callback<ClassCombo>() {
                    @Override
                    public ClassCombo parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        ClassCombo classCombo = new Gson().fromJson(string, ClassCombo.class);
                        return classCombo;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(ClassCombo response, int id) {
                        dialog.cancel();
//                        System.out.println("6666" + response.getList().get(0).getClass_name());
                        classMap=new HashMap<>();
                        if(response.getList().size()!=0) {
                            list_class = new ArrayList<>();
                            for (int i = 0; i < response.getList().size(); i++) {
                                String class_name=response.getList().get(i).getClass_name().toString();
                                String class_id=response.getList().get(i).getClass_id().toString();
                                list_class.add(class_name);
                                classMap.put(class_name,class_id);
                            }
                            choose_class_layout.setVisibility(View.VISIBLE);
                            adapter = new ProgramAdapter(list_class, "class_choose");
                            choose_class.setAdapter(adapter);
                            choose_class.setSelection(CLASS_CHOOSE_POSTION);
                        }else{
                            choose_class_layout.setVisibility(View.GONE);
                            System.out.println("6666666当前年级下没有班级");
                            Toast.makeText(getApplicationContext(),"确认当前年级下是否有班级",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //获取年级列表
    private void findGradeCombo() {
        dialog.show();
        dialog.changeInfo("年级获取中...");
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.commonUrl + "teachingCombo.findGradeCombo.html")//
                .addParams("school_id", ArtTeachiingApplication.school_id)//
                .build()//
                .execute(new Callback<GradeCombo>() {
                    @Override
                    public GradeCombo parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        GradeCombo gradeCombo = new Gson().fromJson(string, GradeCombo.class);
                        return gradeCombo;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(GradeCombo response, int id) {
                        dialog.cancel();
//                        System.out.println("6666" + response.getList().get(0).getGrade_name());
                        gradeMap=new HashMap<>();
                        ArtTeachiingApplication.GRID_EXIST.clear();
                        for(int i=0;i<response.getList().size();i++){
                            String gradname=response.getList().get(i).getGrade_name().toString();
                            String gradeid=response.getList().get(i).getGrade_id().toString();
                            list_grade.add(gradname);
                            gradeMap.put(gradname,gradeid);
                            System.out.println("获取到的年级ID："+gradeid);
                            ArtTeachiingApplication.GRID_EXIST.add(Integer.valueOf(gradeid));
                        }
                        adapter = new ProgramAdapter(list_grade,"grade_choose");
                        choose_grade.setAdapter(adapter);
                        choose_grade.setSelection(GRADE_CHOOSE_POSTION);
                    }
                });
    }

    //创建上课记录
    private void addClassRoomStudent(){
        dialog.show();
        dialog.changeInfo("课堂登陆中...");
        choose_next.setClickable(false);
        System.out.println("-----"+ArtTeachiingApplication.CLASS_CHOOSE_ID+":"+ArtTeachiingApplication.user_id);
        OkHttpUtils
                .post()//
                .url(ArtTeachiingApplication.localUrl + "open/addClassRoomStudent")//
                .addParams("class_id", ArtTeachiingApplication.CLASS_CHOOSE_ID)
                .addParams("user_id", ArtTeachiingApplication.user_id)
                .build()
                .execute(new Callback<addClassRoomStudent>() {
                    @Override
                    public addClassRoomStudent parseNetworkResponse(Response response, int id) throws Exception {
                        String string = response.body().string();
                        System.out.println("创建课堂返回值:"+string);
                        addClassRoomStudent addClass = new Gson().fromJson(string, addClassRoomStudent.class);
                        return addClass;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.cancel();
                        choose_next.setClickable(true);
                        Toast.makeText(getApplicationContext(), "服务器异常,请联系管理人员!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(addClassRoomStudent response, int id) {
                        dialog.cancel();
                        choose_next.setClickable(true);
                        if(response.getSuccess().equals("1")){
                            ArtTeachiingApplication.CTR_ID=response.getCrmap().getCtr_id();
                            int dif_time= MyUtils.countTime(response.getCrmap().getCtr_date().toString(),response.getCrmap().getCurr_date().toString());
                            System.out.println("计算的值为:"+dif_time);
                            if(dif_time>=ArtTeachiingApplication.CTR_ALLTIME){
                                Toast.makeText(getApplicationContext(), "上课时间已经结束!", Toast.LENGTH_LONG).show();
                                System.exit(1);
                            }else{
                                Intent service = new Intent(ChooseGradeActivity.this,CountTimeService.class);
                                service.setPackage(getPackageName());
                                service.putExtra("dif_time",(ArtTeachiingApplication.CTR_ALLTIME-dif_time)+"");
                                startService(service);
                            }
                            ChooseGradeActivity.this.finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "创建课堂失败，请再次创建!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
