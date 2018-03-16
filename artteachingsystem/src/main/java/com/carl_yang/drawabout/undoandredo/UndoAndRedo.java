package com.carl_yang.drawabout.undoandredo;

import android.graphics.Bitmap;
import android.provider.Settings;

import com.carl_yang.drawabout.tools.DrawAttribute;

public class UndoAndRedo {
	private final int CAPACITY = 5;
	private int front;
	private int current;
	private int end;
	private int[] pixels = null;
	private Bitmap[] bitmaps = null;
	
	public UndoAndRedo() {
		if(bitmaps==null) {
			bitmaps = new Bitmap[CAPACITY];
			for(int i = 0; i < bitmaps.length; i++)
				bitmaps[i] = Bitmap.createBitmap(DrawAttribute.screenWidth,
						DrawAttribute.screenHeight, Bitmap.Config.ARGB_8888);
		}
		pixels = new int[DrawAttribute.screenWidth * DrawAttribute.screenHeight];
		front = current = end = 0;
	}
	
	public void addBitmap(Bitmap bitmap) {
		if((front - current + CAPACITY) % CAPACITY == 1) {
			front = (front + 1) % CAPACITY;	
		}
		current = (current + 1) % CAPACITY;
		end = current;
		bitmap.getPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
		bitmaps[current].setPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
	}
	
	public boolean currentIsFirst() {
		if(current == front) return true;
		return false;
	}
	
	public boolean currentIsLast() {
		if(current == end) return true;
		return false;
	}
	
	public void undo(Bitmap paintBitmap) {
		current = (current - 1 + CAPACITY) % CAPACITY;
		bitmaps[current].getPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
		paintBitmap.setPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
	}
	
	public void redo(Bitmap paintBitmap) {
		current = (current + 1) % CAPACITY;
		bitmaps[current].getPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
		paintBitmap.setPixels(pixels, 0, DrawAttribute.screenWidth, 0, 0,
				DrawAttribute.screenWidth, DrawAttribute.screenHeight);
	}
	
	public void freeBitmaps() {
		pixels = null;
		for(int i = 0; i < bitmaps.length; i++) {
			bitmaps[i].recycle();
			bitmaps[i]=null;
			System.gc();
		}
		System.out.println("==回收图片==");
		bitmaps=null;
		System.gc();

	}
}
