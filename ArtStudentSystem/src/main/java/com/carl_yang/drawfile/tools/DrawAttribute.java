package com.carl_yang.drawfile.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import java.io.IOException;
import java.io.InputStream;

public class DrawAttribute {
	public static enum Corner{LEFTTOP,RIGHTTOP,LEFTBOTTOM,RIGHTBOTTOM,ERROR};
	public static enum DrawStatus{CASUAL_WATER,
		                          CASUAL_CRAYON,
		                          CASUAL_COLOR,
		                          ERASER,STAMP,
                                  PENCIL,PEN,GUN,CRAYON,BRUSH,SEAL
		                          };
		                          
    public final static int backgroundOnClickColor = 0xfff08d1e;
	public static int screenHeight;
	public static int screenWidth;
	public static Paint paint = new Paint();
		
	public static Bitmap getImageFromAssetsFile(Context context,String fileName,boolean isBackground)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(isBackground)image = Bitmap.createScaledBitmap(image, DrawAttribute.screenWidth,
        		DrawAttribute.screenHeight, false);
        return image;

    } 
}
