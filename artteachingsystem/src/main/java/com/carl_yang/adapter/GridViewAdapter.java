package com.carl_yang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.art.R;
import com.carl_yang.domain.courseWareProperty;
import com.carl_yang.view.DrawView;

public class GridViewAdapter extends BaseAdapter {
    private Context myContext;
    private String gridType;
    private courseWareProperty cp;

    public GridViewAdapter(Context myContext, String gridType,courseWareProperty cp) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;
        this.gridType = gridType;
        this.cp=cp;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return cp.getmImageIds(gridType).length;

    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view = null;
        ViewHolder holder = null;
        if (convertView == null) {
            view = View.inflate(myContext, R.layout.gridview, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.gridview_image);
            holder.strView = (TextView) view.findViewById(R.id.gridview_str);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Glide
                .with(myContext)
                .load(cp.getmImageIds(gridType)[position])
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
                .into(holder.imageView);
//        holder.imageView.setImageResource(cp.getmImageIds(gridType)[position]);
        holder.strView.setText(cp.getmStringNames(gridType)[position]);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView strView;
    }

}
