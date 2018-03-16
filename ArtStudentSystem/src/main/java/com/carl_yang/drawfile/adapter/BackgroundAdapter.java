package com.carl_yang.drawfile.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carl_yang.stuart.R;

import java.util.LinkedHashMap;

public class BackgroundAdapter extends BaseAdapter {
    private Context myContext;
    private Activity ac;

//    private ImageView the_imageView;

    private Integer[] mBackgroundIds = {
            R.mipmap.bg_image1, R.mipmap.bg_image2, R.mipmap.bg_image3, R.mipmap.bg_image4,
            R.mipmap.bg_image5, R.mipmap.bg_image6, R.mipmap.bg_image7, R.mipmap.bg_image8,
            R.mipmap.bg_image9, R.mipmap.bg_image10, R.mipmap.bg_image11, R.mipmap.bg_image12,
            R.mipmap.bg_image13, R.mipmap.bg_image14, R.mipmap.bg_image15, R.mipmap.bg_image16,
            R.mipmap.bg_image17, R.mipmap.bg_image18, R.mipmap.bg_image19, R.mipmap.bg_image20,
            R.mipmap.bg_image21, R.mipmap.bg_image22, R.mipmap.bg_image23, R.mipmap.bg_image24
    };

    public BackgroundAdapter(Context myContext, Activity ac) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;
        this.ac=ac;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return mBackgroundIds.length;
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
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view=null;
        ViewHolder holder=null;
        if(convertView==null){
            view=View.inflate(myContext,R.layout.bggridview,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) view.findViewById(R.id.bggridview_image);
            view.setTag(holder);
        }else{
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        Glide
                .with(myContext)
                .load(mBackgroundIds[position])
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .dontAnimate()
                .into(holder.imageView);
//        ImageView the_imageView = new ImageView(myContext);

//        the_imageView.setImageResource(mBackgroundIds[position]);
//        the_imageView.setAdjustViewBounds(true);
//        System.out.println("the_imageView.getId()...." + the_imageView.getId());
        return view;
    }
}
