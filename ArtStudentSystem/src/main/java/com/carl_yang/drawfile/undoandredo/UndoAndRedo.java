package com.carl_yang.drawfile.undoandredo;

import android.graphics.Bitmap;

import com.carl_yang.drawfile.tools.DrawAttribute;

public class UndoAndRedo {
	private final int CAPACITY = 6;
	private int front;
	private int current;
	private int end;
	private int[] pixels = null;
	private Bitmap[] bitmaps = null;
	
	public UndoAndRedo() {
		bitmaps = new Bitmap[CAPACITY];
		for(int i = 0; i < bitmaps.length; i++)
			bitmaps[i] = Bitmap.createBitmap(DrawAttribute.screenWidth,
					DrawAttribute.screenHeight, Bitmap.Config.ARGB_8888);
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
		return current == front;
	}
	
	public boolean currentIsLast() {
		return current == end;
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
		for(int i = 0; i < bitmaps.length; i++)
			bitmaps[i].recycle();
	}
}
