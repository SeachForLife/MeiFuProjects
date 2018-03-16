package com.carl_yang.meifuoa.fragmentdetail;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.carl_yang.adapter.ListViewAdapter;
import com.carl_yang.domain.CustomerMessage;
import com.carl_yang.domain.SearchCustomerDoamin;
import com.carl_yang.meifuoa.MainMapActivity;
import com.carl_yang.meifuoa.R;
import com.carl_yang.meifuoaApplication;
import com.carl_yang.tools.SharedPreferencesUtils;
import com.carl_yang.widget.SearchEditText;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class SelectCustomerFragment extends Fragment {

    private static final String TAG = "SelectCustomerFragment";
    private SearchEditText select_customer;
    private ListView select_listview;
    private ListViewAdapter adapter;
    private List<CustomerMessage> current_customer_list=null;
    private XRefreshView xRefreshView;

    public SelectCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_select_customer, container, false);
        select_listview= (ListView) view.findViewById(R.id.select_listview);
        xRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        select_customer= (SearchEditText) view.findViewById(R.id.select_customer);
        if(meifuoaApplication.customer_list==null){
            List<CustomerMessage> error_list=new ArrayList<>();
            CustomerMessage cm=new CustomerMessage();
            cm.setCustomer_address("数据加载失败，请下拉刷新");
            cm.setCustomer_name("");
            cm.setCustomer_id("");
            error_list.add(cm);
            adapter = new ListViewAdapter(getActivity(), error_list);
        }else{
            adapter = new ListViewAdapter(getActivity(), meifuoaApplication.customer_list);
        }
        //初始客户列表
        current_customer_list=meifuoaApplication.customer_list;
        select_listview.setAdapter(adapter);
        select_listview.setDivider(new ColorDrawable(Color.GRAY));
        select_listview.setDividerHeight(1);
        String ccname=SharedPreferencesUtils.getInstance(getActivity()).read("current_choose_customername","");
        select_customer.setText(ccname);
        select_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_customer_list=adapter.getCurrentList();
                String selected_customer=current_customer_list.get(position).getCustomer_name().toString();
                Log.i("select_customer","选择的客户名称:"+selected_customer);
                select_customer.setText(selected_customer);
                SharedPreferencesUtils.getInstance(getActivity()).write("current_choose_customername",selected_customer);
                SharedPreferencesUtils.getInstance(getActivity()).write("customer_id",current_customer_list.get(position).getCustomer_id().toString());
            }
        });
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                SearchCustomer();
            }

            public void onLoadMore(boolean isSilence) {
            }
        });
        intiEditView();
        return view;
    }

    private void intiEditView() {
        select_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("select customer","END");
            }
        });
    }

    public void SearchCustomer(){
        OkHttpUtils.post()
                .url(meifuoaApplication.TMurl+"open/appTrajectory.findCustomerList.html")
                .addParams("customer.emp_id", SharedPreferencesUtils.getInstance(getActivity()).read("emp_id",""))//
                .build()//
                .execute(new MyStringCallback());
    }

    private class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            SearchCustomerDoamin ld = new Gson().fromJson(response, SearchCustomerDoamin.class);
            String stu_success = ld.getSuccess();
            Log.i(TAG, stu_success);

            if (stu_success.equals("1")) {
                meifuoaApplication.customer_list = new ArrayList<>();
                for (int i = 0; i < ld.getList().size(); i++) {
                    CustomerMessage cm = new CustomerMessage();
                    cm.setCustomer_address(ld.getList().get(i).getCustomer_address().toString());
                    cm.setCustomer_name(ld.getList().get(i).getCustomer_name().toString());
                    cm.setCustomer_id(ld.getList().get(i).getCustomer_id().toString());
                    meifuoaApplication.customer_list.add(cm);
                }
                adapter = new ListViewAdapter(getActivity(), meifuoaApplication.customer_list);
                current_customer_list=meifuoaApplication.customer_list;
                select_listview.setAdapter(adapter);
                xRefreshView.stopRefresh();
            } else if (stu_success.equals("0")) {
                String msg = ld.getMsg();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
