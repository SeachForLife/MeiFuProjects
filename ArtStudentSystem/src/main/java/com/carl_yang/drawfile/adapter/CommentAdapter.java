package com.carl_yang.drawfile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carl_yang.httpintent.domain.CommentDomain;
import com.carl_yang.stuart.R;

import java.util.List;


public class CommentAdapter extends BaseAdapter{
    private static final String TAG = "CommentAdapter";
    private Context myContext;
    private List<CommentDomain.Comment_content> list;

    public CommentAdapter(Context myContext, List<CommentDomain.Comment_content> listin) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;
        this.list = listin;
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
                        R.layout.commentlistview, null);
                holder = new ViewHolder();
                holder.com_listview_content = (TextView) view.findViewById(R.id.com_listview_content);
                holder.com_listview_teacher = (TextView) view.findViewById(R.id.com_listview_teacher);
                holder.com_listview_time = (TextView) view.findViewById(R.id.com_listview_time);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            Log.d(TAG, "getView: "+list.get(position).getTeacName());
            holder.com_listview_teacher.setText(list.get(position).getTeacName());
            holder.com_listview_time.setText(list.get(position).getCreateTime());
            holder.com_listview_content.setText(list.get(position).getContent());
        } catch (Exception swallow) {
        }
        return view;
    }

    static class ViewHolder {
        TextView com_listview_teacher;
        TextView com_listview_time;
        TextView com_listview_content;
    }

}
