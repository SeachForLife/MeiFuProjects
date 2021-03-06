package com.carl_yang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.carl_yang.domain.CustomerMessage;
import com.carl_yang.meifuoa.R;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapter extends BaseAdapter implements Filterable {
    private Context myContext;
    private List<CustomerMessage> list;
    private ArrayList<CustomerMessage> mOriginalValues;
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;
    private final Object mLock = new Object();

    public ListViewAdapter(Context myContext, List<CustomerMessage> listin) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;
        this.list = listin;
//			for (CustomerMessage listvalue : listin) {
//				list.add(listvalue);
//			}
        mInflater = LayoutInflater.from(myContext);
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return list.size() > 0 ? list.size() : 0;
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        try {
            if (convertView == null) {
                view = View.inflate(myContext,
                        R.layout.listview, null);
                holder = new ViewHolder();
                holder.value = (TextView) view.findViewById(R.id.listview_customervalue);
                holder.value1 = (TextView) view.findViewById(R.id.listview_customeraddress);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.value.setText(list.get(position).getCustomer_name());
            holder.value1.setText(list.get(position).getCustomer_address());

        } catch (Exception swallow) {
        }
        return view;
    }

    static class ViewHolder {
        TextView value;
        TextView value1;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * 获取当前帅选结果的list
     *
     * @return
     */
    public List<CustomerMessage> getCurrentList() {
        Log.v("----------------", String.valueOf(list.size()));
        return list;
    }

    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(list);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<CustomerMessage> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<CustomerMessage> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<CustomerMessage> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final CustomerMessage value = values.get(i);//从List<User>中拿到User对象
                    final String valueText = value.getCustomer_name().toString().toLowerCase();//User对象的name属性作为过滤的参数
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            list = (List<CustomerMessage>) results.values;//此时，Adapter数据源就是过滤后的Results
            Log.v("=====", String.valueOf(list.size()));
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }

}
