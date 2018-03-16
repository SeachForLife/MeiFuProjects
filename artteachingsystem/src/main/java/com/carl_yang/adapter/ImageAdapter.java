package com.carl_yang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.art.ChooseGradeActivity;
import com.carl_yang.art.R;
import com.carl_yang.util.PatternBitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loren Yang on 2017/12/6.
 */

public class ImageAdapter extends BaseAdapter {

    private Bitmap bt=null;
    private Context context;
    private List<Drawable> image_list = new ArrayList<Drawable>();

    public ImageAdapter(List<Drawable> list, Context context){
        this.image_list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return image_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= image_list.size()) {
            return null;
        }
        return image_list.get(position);
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
                view = View.inflate(context,
                        R.layout.image_listview, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.imagelistview_image);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
//            Glide
//                    .with(context)
//                    .load(PatternBitmapUtils.drawableToBitmap(image_list.get(position)))
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .crossFade()
//                    .dontAnimate()
//                    .into(holder.imageView);
            bt= PatternBitmapUtils.drawableToBitmap(image_list.get(position));
            holder.imageView.setImageBitmap(bt);
        } catch (Exception swallow) {
        }
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
