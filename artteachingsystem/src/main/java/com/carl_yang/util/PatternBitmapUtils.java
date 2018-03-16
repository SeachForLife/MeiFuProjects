package com.carl_yang.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

/**
 * Created by Loren Yang on 2017/12/6.
 */

public class PatternBitmapUtils {

    /**
     *  从当前bitmap切割出一个矩形出来,起始位置0,0
     */
    public static Bitmap clipRectFromTopLeft(Bitmap bitmap,int width,int height){
        Bitmap new_bit=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(new_bit);
        Rect rect = new Rect(0, 0, width, height);
        mCanvas.clipRect(rect);
        mCanvas.drawBitmap(bitmap, 0, 0, null);
        return new_bit;
    }

    /**
     * 从当前图片中切割下一块矩形区域
     * @param bitmap
     * @param marginTop 切割区域的坐上角X
     * @param marginLeft 切割区域的坐上角Y
     * @param width 切割区域的宽
     * @param height 切割区域的高
     * @return
     */
    public static Bitmap clipRectFromLeft(Bitmap bitmap,int marginLeft,int marginTop,int width,int height){
        Bitmap new_bit=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(new_bit);
        Rect rect = new Rect(marginLeft, marginTop, width+marginLeft, height+marginTop);
        mCanvas.clipRect(rect);
        mCanvas.drawBitmap(bitmap, 0, 0, null);
        return new_bit;
    }

    public static Bitmap takeRect(Bitmap bitmap,int marginLeft,int marginTop,int width,int height){
        Bitmap new_bit=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(new_bit);
        mCanvas.drawBitmap(bitmap, -marginLeft, -marginTop, null);
        return new_bit;
    }

    /**
     * 将Bitmap切割出一个半弧环状的图片
     * @param sourceBitmap 切割的资源图片
     * @param ring_r 切割区域的环宽度
     * @return
     */
    public static Bitmap clipRingBitmap(Bitmap sourceBitmap,float ring_r){

        Bitmap new_bit=Bitmap.createBitmap(sourceBitmap.getWidth(),sourceBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(new_bit);

        RectF re1=new RectF(-sourceBitmap.getWidth(),0,sourceBitmap.getWidth(),sourceBitmap.getHeight()*2);
        RectF re2=new RectF(-sourceBitmap.getWidth()+ring_r,ring_r,sourceBitmap.getWidth()-ring_r,sourceBitmap.getHeight()*2-ring_r);

        /**
         * 切割下一个外环+三角形区域
         */
        Path p=new Path();
        p.moveTo(0,sourceBitmap.getHeight());
        p.lineTo(0,0);
        p.lineTo((float) (sourceBitmap.getHeight()*Math.sin(Math.PI*60/180)), (float) (sourceBitmap.getHeight()*Math.sin(Math.PI*30/180)));
        p.close();
        p.addArc(re1,
                -90,
                60
        );
        mCanvas.clipPath(p);
        /**
         * 将内环区域切掉
         */
        p.reset();
        p.moveTo(0,sourceBitmap.getHeight());
        p.lineTo(0,ring_r);
        float r1=sourceBitmap.getHeight()-ring_r;
        p.lineTo((float) (r1*Math.sin(Math.PI*60/180)),(float) (sourceBitmap.getHeight()-(r1*Math.sin(Math.PI*30/180))));
        p.close();
        p.addArc(re2,
                -90,
                60
        );
        mCanvas.clipPath(p, Region.Op.DIFFERENCE);
        mCanvas.drawBitmap(sourceBitmap, 0, 0, null);
        sourceBitmap.recycle();
        return new_bit;
    }

    /**
     * 将Bitmap切割出一个半弧环状的图片
     * @param sourceBitmap 切割的资源图片
     * @param ring_out 外环距离外边的距离
     * @param ring_in 内环距离外边的距离
     * @return
     */
    public static Bitmap clipRingBitmapFirst(Bitmap sourceBitmap,float ring_out,float ring_in){

        Bitmap new_bit=Bitmap.createBitmap(sourceBitmap.getHeight(), sourceBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(new_bit);

        RectF re1=new RectF(-sourceBitmap.getWidth()+ring_out,ring_out,sourceBitmap.getWidth()-ring_out,sourceBitmap.getHeight()*2-ring_out);
        RectF re2=new RectF(-sourceBitmap.getWidth()+ring_in,ring_in,sourceBitmap.getWidth()-ring_in,sourceBitmap.getHeight()*2-ring_in);

        /**
         * 切割下一个外环+三角形区域
         */
        float ring_out_r=sourceBitmap.getHeight()-ring_out;
        Path p=new Path();
        p.moveTo(0,sourceBitmap.getHeight());
        p.lineTo(0,ring_out);
        p.lineTo((float) (ring_out_r*Math.sin(Math.PI*60/180)), (float) (sourceBitmap.getHeight()-(ring_out_r*Math.sin(Math.PI*30/180))));
        p.close();
        p.addArc(re1,
                -90,
                60
        );
        mCanvas.clipPath(p);
        /**
         * 将内环区域切掉
         */
        p.reset();
        p.moveTo(0,sourceBitmap.getHeight());
        p.lineTo(0,ring_in);
        float r1=sourceBitmap.getHeight()-ring_in;
        p.lineTo((float) (r1*Math.sin(Math.PI*60/180)),(float) (sourceBitmap.getHeight()-(r1*Math.sin(Math.PI*30/180))));
        p.close();
        p.addArc(re2,
                -90,
                60
        );
        mCanvas.clipPath(p, Region.Op.DIFFERENCE);
        mCanvas.drawBitmap(sourceBitmap, 0, 0, null);
        sourceBitmap.recycle();
        return new_bit;
    }

    /**
     * 将bitmpa旋转后拼接上原来未知的图片，最终形成一个圆
     * @param sourceBitmap 资源图
     * @param trans_num 旋转拼接成圆的次数
     * @return
     */
    public static Bitmap rotateAndLinkToCircle(Bitmap sourceBitmap,int trans_num){
        Bitmap new_bit2=Bitmap.createBitmap(sourceBitmap.getWidth()*2,sourceBitmap.getHeight()*2,Bitmap.Config.ARGB_8888);
        Canvas mCanvas2 = new Canvas(new_bit2);
        mCanvas2.translate(sourceBitmap.getWidth(),sourceBitmap.getHeight());
        for(int i=0;i<trans_num;i++){
            mCanvas2.drawBitmap(sourceBitmap,0,-sourceBitmap.getHeight(),null);
            mCanvas2.rotate(360/trans_num);
        }
        return new_bit2;
    }

    /**
     * 图片横向拼接
     * @param sourceBitmap
     * @param link_num 拼接次数
     * @return
     */
    public static Bitmap linkBitmap(Bitmap sourceBitmap,int link_num) {
        Bitmap new_bit=Bitmap.createBitmap(sourceBitmap.getWidth()*link_num,sourceBitmap.getHeight(),sourceBitmap.getConfig());
        Canvas mCanvas = new Canvas(new_bit);
        for(int i=0;i<link_num;i++){
            mCanvas.drawBitmap(sourceBitmap, sourceBitmap.getWidth()*i, 0, null);
        }
        return new_bit;
    }

    /**
     *  将生成的纹样放置到实物图上
     */
    public static Bitmap setBitmapIn(Bitmap backgroundBitmap,Bitmap resultBtimap,int startX,int startY,int endX,int endY){
        Bitmap new_bit=Bitmap.createBitmap(backgroundBitmap.getWidth(),backgroundBitmap.getHeight(),backgroundBitmap.getConfig());
        Canvas mCanvas = new Canvas(new_bit);
        mCanvas.drawBitmap(backgroundBitmap,0 , 0, null);
        RectF ref=new RectF(startX,startY,endX,endY);
        mCanvas.drawBitmap(resultBtimap,null , ref, null);
        backgroundBitmap.recycle();
        resultBtimap.recycle();
        return new_bit;
    }

    /**
     * Drawable转成bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, false);
    }
}
