package com.carl_yang.drawfile.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.carl_yang.stuart.R;

public class StampPickAdapter extends BaseAdapter{
		private Context myContext ;

		private ImageView the_imageView ;

		private Integer[] mStampIds = {
				R.drawable.yz1 , R.drawable.yz2 ,R.drawable.yz3 , R.drawable.yz4 ,
				R.drawable.yz5 , R.drawable.yz6 ,R.drawable.yz7 , R.drawable.yz8 ,
				R.drawable.yz9 , R.drawable.yz10 ,R.drawable.yz11,  R.drawable.yz12,
				R.drawable.yz13,  R.drawable.yz14, R.drawable.yz15,  R.drawable.yz16,
                R.drawable.yz17,  R.drawable.yz18,R.drawable.yz19,  R.drawable.yz20,
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

		
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			the_imageView = new  ImageView( myContext );
			the_imageView .setImageResource( mStampIds [position]);

			the_imageView .setAdjustViewBounds(true );
//			the_imageView .setBackgroundResource(android.R.drawable.dialog_frame );
			System.out.println("the_imageView.getId()...."+the_imageView.getId());
			return the_imageView ;
		}

		public Integer getcheckedImageIDPostion( int theindex) {
			return mStampIds [theindex];
		}
	}
