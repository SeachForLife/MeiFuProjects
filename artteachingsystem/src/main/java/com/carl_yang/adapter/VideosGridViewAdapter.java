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
import com.carl_yang.domain.PersonCollect;

import java.util.List;

public class VideosGridViewAdapter extends BaseAdapter {
    private Context myContext;
    private List<PersonCollect> personCollectList;


    public VideosGridViewAdapter(){

    }

    public VideosGridViewAdapter(Context myContext, List<PersonCollect> pcList) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;
        this.personCollectList=pcList;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return personCollectList.size();
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
            view = View.inflate(myContext, R.layout.videogridview, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.videosgridview_image);
            holder.strView = (TextView) view.findViewById(R.id.videosgridview_str);
            holder.strView1 = (TextView) view.findViewById(R.id.videosgridview_str1);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
//        Glide
//                .with(myContext)
//                .load(personCollectList.get(position).getUrl().toString())
//                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .crossFade()
//                .dontAnimate()
//                .into(holder.imageView);
        holder.strView.setText(personCollectList.get(position).getIamgename().toString());
        holder.strView1.setText(personCollectList.get(position).getAuth().toString());
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView strView;
        TextView strView1;
    }

}
