package com.carl_yang.drawfile.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.carl_yang.drawfile.view.DrawView;
import com.carl_yang.property.DrawProperty;
import com.carl_yang.stuart.R;

public class ColorGridImageAdapter extends BaseAdapter {
    private Context myContext;

    private ImageView the_imageView;

    private Integer[] mImageIds = {
            R.drawable.big_color0, R.drawable.big_color1, R.drawable.big_color2, R.drawable.big_color3,
            R.drawable.big_color4, R.drawable.big_color5, R.drawable.big_color6, R.drawable.big_color7,
            R.drawable.big_color8, R.drawable.big_color9, R.drawable.big_color10, R.drawable.big_color11,
            R.drawable.big_color12, R.drawable.big_color13, R.drawable.big_color14, R.drawable.big_color15,
            R.drawable.big_color16, R.drawable.big_color17
    };

    private Integer[] mImageIds_pressed = {
            R.drawable.big_color0_h, R.drawable.big_color1_h, R.drawable.big_color2_h, R.drawable.big_color3_h,
            R.drawable.big_color4_h, R.drawable.big_color5_h, R.drawable.big_color6_h, R.drawable.big_color7_h,
            R.drawable.big_color8_h, R.drawable.big_color9_h, R.drawable.big_color10_h, R.drawable.big_color11_h,
            R.drawable.big_color12_h, R.drawable.big_color13_h, R.drawable.big_color14_h, R.drawable.big_color15_h,
            R.drawable.big_color16_h, R.drawable.big_color17_h
    };

    public ColorGridImageAdapter(Context myContext) {
        // TODO TODO TODO TODO Auto-generated constructor stub
        this.myContext = myContext;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return mImageIds.length;
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

        the_imageView = new ImageView(myContext);

        if (position == DrawProperty.COLOR_CURRENT) {
            the_imageView.setImageResource(mImageIds_pressed[position]);
        } else {
            the_imageView.setImageResource(mImageIds[position]);
        }

        the_imageView.setAdjustViewBounds(true);
//			the_imageView .setBackgroundResource(android.R.drawable.dialog_frame );
        System.out.println("the_imageView.getId()...." + the_imageView.getId());
        return the_imageView;
    }

    public Integer getcheckedImageIDPostion(int theindex) {
        return mImageIds[theindex];
    }
}
