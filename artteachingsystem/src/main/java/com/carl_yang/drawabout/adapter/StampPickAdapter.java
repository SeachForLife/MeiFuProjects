package com.carl_yang.drawabout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.art.R;

public class StampPickAdapter extends BaseAdapter {
    private Context myContext;

    private ImageView the_imageView;

    private Integer[] mStampIds = {
            R.drawable.yz1, R.drawable.yz2, R.drawable.yz3, R.drawable.yz4,
            R.drawable.yz5, R.drawable.yz6, R.drawable.yz7, R.drawable.yz8,
            R.drawable.yz9, R.drawable.yz10, R.drawable.yz11, R.drawable.yz12,
            R.drawable.yz13, R.drawable.yz14, R.drawable.yz15, R.drawable.yz16,
            R.drawable.yz17, R.drawable.yz18, R.drawable.yz19, R.drawable.yz20,
            R.drawable.yz21
    };

    public StampPickAdapter(Context myContext) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return mStampIds.length;
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView strView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

//        View view = null;
//        ViewHolder holder = null;
//        if (convertView == null) {
//            view = View.inflate(myContext, R.layout.videogridview, null);
//            holder = new ViewHolder();
//            holder.imageView = (ImageView) view.findViewById(R.id.videosgridview_image);
//            holder.strView = (TextView) view.findViewById(R.id.videosgridview_str);
//            view.setTag(holder);
//        } else {
//            view = convertView;
//            holder = (ViewHolder) view.getTag();
//        }
//        holder.strView.setVisibility(View.GONE);

//        Glide
//                .with(myContext)
//                .load(mStampIds[position])
//                .centerCrop()
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .dontAnimate()
//                .into(holder.imageView);
        the_imageView = new ImageView(myContext);
        the_imageView.setImageResource(mStampIds[position]);
        the_imageView.setAdjustViewBounds(true);
        return the_imageView;
    }

    public Integer getcheckedImageIDPostion(int theindex) {
        return mStampIds[theindex];
    }
}
