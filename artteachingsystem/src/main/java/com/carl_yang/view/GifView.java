package com.carl_yang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.carl_yang.art.R;

public class GifView extends ImageView {

	private boolean isGifImage;
	private int image;
	private Movie movie;
	private long movieStart = 0;
	
	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifView);
		isGifImage = array.getBoolean(R.styleable.GifView_isgifimage, true);
		array.recycle();
		image = attrs.getAttributeResourceValue( "http://schemas.android.com/apk/res/android", "src", 0);
		
		movie = Movie.decodeStream(getResources().openRawResource(image));
	}
	
@Override
protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	if(isGifImage){
		DrawGifImage(canvas);
	}
}

private void DrawGifImage(Canvas canvas) {
	//��ȡϵͳ��ǰʱ��
	long nowTime = android.os.SystemClock.currentThreadTimeMillis();
	if(movieStart == 0){
		movieStart = nowTime;
	}
	if(movie != null){
		int duration = movie.duration();
		if(duration > 100){
			int relTime = (int) ((nowTime - movieStart) % duration);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);
			invalidate();
		}
	}
}
}
