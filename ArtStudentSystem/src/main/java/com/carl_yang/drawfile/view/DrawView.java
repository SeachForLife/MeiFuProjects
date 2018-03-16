package com.carl_yang.drawfile.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.carl_yang.drawfile.geometry.BasicGeometry;
import com.carl_yang.drawfile.sticker.StickerBitmap;
import com.carl_yang.drawfile.sticker.StickerBitmapList;
import com.carl_yang.drawfile.tools.DrawAttribute;
import com.carl_yang.drawfile.tools.StorageInSDCard;
import com.carl_yang.drawfile.undoandredo.UndoAndRedo;
import com.carl_yang.property.DrawProperty;
import com.carl_yang.stuart.R;


public class DrawView extends View implements Runnable {
    private final int VISIBLE_BTN_WIDTH = 60;
    private final int VISIBLE_BTN_HEIGHT = 40;

    private enum TouchLayer {GEOMETRY_LAYER, PAINT_LAYER, STICKER_BITMAP, STICKER_TOOL, VISIBLE_BTN};
    private Bitmap backgroundBitmap = null;
    private PointF backgroundBitmapLeftTopP = null;
    private Bitmap paintBitmap = null;
    private Canvas paintCanvas = null;
    private BasicGeometry basicGeometry = null;
    private StickerBitmapList stickerBitmapList = null;
    //	private Bitmap visibleBtnBitmap = null;
    private GestureDetector brushGestureDetector = null;
    private BrushGestureListener brushGestureListener = null;
    private DrawAttribute.DrawStatus drawStatus;
    private TouchLayer touchLayer;
    UndoAndRedo undoAndRedo;
    private Context context;

    public UndoAndRedo getUndoAndRedo() {
        return undoAndRedo;
    }

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        backgroundBitmap = DrawAttribute.getImageFromAssetsFile(context, "bigpaper00.jpg", true);
        backgroundBitmapLeftTopP = new PointF(0, 0);
        paintBitmap = Bitmap.createBitmap(DrawAttribute.screenWidth,
                DrawAttribute.screenHeight, Bitmap.Config.ARGB_8888);
        paintCanvas = new Canvas(paintBitmap);
        paintCanvas.drawARGB(0, 255, 255, 255);
        stickerBitmapList = new StickerBitmapList(this);
        this.drawStatus = DrawAttribute.DrawStatus.CASUAL_WATER;
        undoAndRedo = new UndoAndRedo();
        brushGestureListener = new BrushGestureListener
                (casualStroke(R.drawable.marker), 2, null);
        brushGestureDetector = new GestureDetector(brushGestureListener);
        new Thread(this).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(backgroundBitmap, backgroundBitmapLeftTopP.x,
                backgroundBitmapLeftTopP.y, null);
        canvas.drawBitmap(paintBitmap, 0, 0, null);
        if (basicGeometry != null) {
            basicGeometry.drawGraphic(canvas);
        }
        stickerBitmapList.drawStickerBitmapList(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("点击操作!");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            System.out.println("按下-----操作!");
            float x = event.getX();
            float y = event.getY();
            int touchType = stickerBitmapList.getOnTouchType(x, y);
            switch (touchType) {
                case 1:
                    touchLayer = TouchLayer.STICKER_TOOL;
                    return true;
                case 0:
                    touchLayer = TouchLayer.STICKER_BITMAP;
                    break;
                case -1:
                    if (basicGeometry != null && basicGeometry.isPointInsideGeometry
                            (event.getX(), event.getY())) {
                        touchLayer = TouchLayer.GEOMETRY_LAYER;
                    } else {
                        touchLayer = TouchLayer.PAINT_LAYER;
                    }
            }
        }
        if (touchLayer == TouchLayer.PAINT_LAYER) {
            brushGestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                undoAndRedo.addBitmap(paintBitmap);
            }
        } else if (touchLayer == TouchLayer.GEOMETRY_LAYER) {
            basicGeometry.onTouchEvent(event);
        } else if (touchLayer == TouchLayer.STICKER_BITMAP) {
            stickerBitmapList.onTouchEvent(event);
        }
        return true;
    }

    private boolean isClickOnVisibleBtn(float x, float y) {
        if (x > DrawAttribute.screenWidth - VISIBLE_BTN_WIDTH && x < DrawAttribute.screenWidth
                && y < VISIBLE_BTN_HEIGHT) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            postInvalidate();
        }
    }

    public void setBackgroundBitmapPattern(Bitmap bitmap,String type){
        if(type.equals("plate")) {
            float scaleHeight = bitmap.getHeight() * 1.0f / DrawAttribute.screenHeight;
            backgroundBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (bitmap.getWidth() / scaleHeight), (int) (bitmap.getHeight() / scaleHeight), false);
            backgroundBitmapLeftTopP.x = 0;
            backgroundBitmapLeftTopP.y = 0;
        }else{
            float scaleHeight = bitmap.getHeight() * 1.0f / (DrawAttribute.screenHeight/2);
            backgroundBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (bitmap.getWidth() / scaleHeight), (int) (bitmap.getHeight() / scaleHeight), false);
            backgroundBitmapLeftTopP.x = 0;
            backgroundBitmapLeftTopP.y = (DrawAttribute.screenHeight - backgroundBitmap.getHeight()) / 2;
        }
    }

    public void setBackgroundBitmap(Bitmap bitmap, boolean isFromSystem) {
        if (isFromSystem) {
            backgroundBitmap = bitmap;
            backgroundBitmapLeftTopP.set(0, 0);
        } else {
            float scaleWidth = bitmap.getWidth() * 1.0f / DrawAttribute.screenWidth;
            float scaleHeight = bitmap.getHeight() * 1.0f / DrawAttribute.screenHeight;
            float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            if (scale > 1.01)
                backgroundBitmap = Bitmap.createScaledBitmap(bitmap,
                        (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale), false);
            else {
                backgroundBitmap = bitmap;
            }
            backgroundBitmapLeftTopP.x = (DrawAttribute.screenWidth - backgroundBitmap.getWidth()) / 2;
            backgroundBitmapLeftTopP.y = (DrawAttribute.screenHeight - backgroundBitmap.getHeight()) / 2;

        }
    }

    public void testBg(Bitmap bitmap){
        paintCanvas.drawBitmap(bitmap, 0,
                0, null);
    }

    public void cleanPaintBitmap() {
        paintCanvas.drawColor(0xffffffff, Mode.DST_OUT);
    }

    public void recordPaintBitmap(Bitmap bitmap) {
        undoAndRedo.addBitmap(bitmap);
    }

    public void undo() {
        if (!undoAndRedo.currentIsFirst()) {
            undoAndRedo.undo(paintBitmap);
        }
    }

    public void redo() {
        if (!undoAndRedo.currentIsLast()) {
            undoAndRedo.redo(paintBitmap);
        }
    }

    public void setBasicGeometry(BasicGeometry geometry) {
        if (basicGeometry != null) {
            basicGeometry.drawGraphic(paintCanvas);
        }
        this.basicGeometry = geometry;
    }

    public void addStickerBitmap(Bitmap bitmap) {
        stickerBitmapList.setIsStickerToolsDraw(false, null);
        if (!stickerBitmapList.addStickerBitmap(new StickerBitmap(this, stickerBitmapList, bitmap))) {
        }
    }

    public Canvas getPaintCanvas() {
        return paintCanvas;
    }

    private Bitmap casualStroke(int drawableId) {
        Bitmap mode = ((BitmapDrawable) this.getResources().getDrawable(drawableId)).getBitmap();
        mode = PicZoom(mode, DrawProperty.BRUSH_SIZE, DrawProperty.BRUSH_SIZE);
        Bitmap bitmap = mode.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Paint paintUnder = new Paint();
        paintUnder.setColor(DrawProperty.COLOR_CHOOSE);
//        paintUnder.setColor(Color.parseColor("#FF7F00"));
        canvas.drawPaint(paintUnder);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        canvas.drawBitmap(mode, 0, 0, paint);
        return bitmap;
    }

    private Bitmap PicZoom(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, false);
    }

    public void setBrushBitmap(DrawAttribute.DrawStatus drawStatus) {
        this.drawStatus = drawStatus;
        Bitmap brushBitmap;
        int brushDistance;
        Paint brushPaint;
        switch (drawStatus){
            case PENCIL:
                System.out.println("choose pencil");
                brushBitmap = casualStroke(R.drawable.marker);
                brushDistance = 2;
                brushPaint = null;
                break;
            case PEN:
                System.out.println("choose pen");
                brushBitmap = casualStroke(R.drawable.marker);
                brushDistance = 2;
                brushPaint = null;
                break;
            case GUN:
                System.out.println("choose gun");
                brushBitmap = casualStroke(R.drawable.marker);
                brushDistance = 2;
                brushPaint = null;
                break;
            case CRAYON:
                System.out.println("choose crayon");
                brushBitmap = casualStroke(R.drawable.crayon);
                brushDistance = brushBitmap.getWidth() / 2;
                brushPaint = null;
                break;
            case BRUSH:
                System.out.println("choose brush");
                brushBitmap = casualStroke(R.drawable.paintcopy);
                brushDistance = 3;
                brushPaint = null;
                break;
            case SEAL:
                System.out.println("choose seal");
                brushPaint = new Paint();
                brushPaint.setFilterBitmap(true);
                brushBitmap = ((BitmapDrawable) this.getResources().
                        getDrawable(DrawProperty.STAMP_ID)).getBitmap();
                brushDistance = brushBitmap.getWidth() ;
                break;
            case ERASER:
                System.out.println("choose eraser");
                brushPaint = new Paint();
                brushPaint.setFilterBitmap(true);
                brushPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
                brushBitmap = ((BitmapDrawable) this.getResources().
                        getDrawable(R.drawable.eraser)).getBitmap();
                brushBitmap = PicZoom(brushBitmap, DrawProperty.BRUSH_SIZE, DrawProperty.BRUSH_SIZE);
                brushDistance = brushBitmap.getWidth() / 4;
                break;
            default:
            brushBitmap = casualStroke(R.drawable.marker);
            brushDistance = 1;
            brushPaint = null;
                break;
        }
//        if (drawStatus == DrawAttribute.DrawStatus.CASUAL_WATER) {
//            brushBitmap = casualStroke(R.drawable.marker);
//            brushDistance = 1;
//            brushPaint = null;
//        } else if (drawStatus == DrawAttribute.DrawStatus.CASUAL_CRAYON) {
//            brushBitmap = casualStroke(R.drawable.crayon);
//            brushDistance = brushBitmap.getWidth() / 2;
//            brushPaint = null;
//        } else if (drawStatus == DrawAttribute.DrawStatus.CASUAL_COLOR) {
//            brushBitmap = casualStroke(R.drawable.paintcopy);
//            brushDistance = 3;
//            brushPaint = null;
//        } else if(drawStatus == DrawAttribute.DrawStatus.ERASER){
//            brushPaint = new Paint();
//            brushPaint.setFilterBitmap(true);
//            brushPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
//            brushBitmap = ((BitmapDrawable) this.getResources().
//                    getDrawable(R.drawable.eraser)).getBitmap();
//            brushBitmap = PicZoom(brushBitmap, BRUSH_SIZE, BRUSH_SIZE);
//            brushDistance = brushBitmap.getWidth() / 4;
//        }else{
//            brushPaint = new Paint();
//            brushPaint.setFilterBitmap(true);
////            brushPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
//            brushBitmap = ((BitmapDrawable) this.getResources().
//                    getDrawable(STAMP_ID)).getBitmap();
//            brushDistance = brushBitmap.getWidth() / 4;
//        }
        brushGestureListener.setBrush(brushBitmap, brushDistance, brushPaint);
    }

    class BrushGestureListener extends GestureDetector.SimpleOnGestureListener {

        private Bitmap brushBitmap = null;
        private int brushDistance;
        private int halfBrushBitmapWidth;
        private Paint brushPaint = null;
        private Bitmap[] stampBrushBitmaps = null;
        private boolean isStamp;


        public BrushGestureListener(Bitmap brushBitmap, int brushDistance, Paint brushPaint) {
            super();
            setBrush(brushBitmap, brushDistance, brushPaint);
            isStamp = false;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            switch (drawStatus) {
                case CASUAL_WATER:
                case CASUAL_CRAYON:
                case CASUAL_COLOR:
                case ERASER:
                    isStamp = false;
                    break;
                case STAMP:
                    isStamp=true;
                    break;
            }
            if (!isStamp) {
                setBrushBitmap(DrawProperty.BUTTON_CHOOSE);
                paintCanvas.drawBitmap(brushBitmap, e.getX() - halfBrushBitmapWidth,
                        e.getY() - halfBrushBitmapWidth, brushPaint);
            } else {
                paintSingleStamp(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
//            System.out.println("滑动-----操作!");
            if (!isStamp) {
                float beginX = e2.getX();
                float beginY = e2.getY();
                float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                float x = distanceX / distance, x_ = 0;
                float y = distanceY / distance, y_ = 0;
                while (Math.abs(x_) <= Math.abs(distanceX) && Math.abs(y_) <= Math.abs(distanceY)) {
                    x_ += x * brushDistance;
                    y_ += y * brushDistance;
                    paintCanvas.save();
                    paintCanvas.rotate((float) (Math.random() * 10000), beginX + x_, beginY + y_);
                    paintCanvas.drawBitmap(brushBitmap, beginX + x_ - halfBrushBitmapWidth,
                            beginY + y_ - halfBrushBitmapWidth, brushPaint);
                    paintCanvas.restore();
                }
            } else {
                paintSingleStamp(e2.getX(), e2.getY());
            }
            return true;
        }

        public void setBrush(Bitmap brushBitmap, int brushDistance, Paint brushPaint) {
            this.brushBitmap = brushBitmap;
            this.brushDistance = brushDistance;
            halfBrushBitmapWidth = brushBitmap.getWidth() / 2;
            this.brushPaint = brushPaint;
        }

        public void setStampBrush(Bitmap[] brushBitmaps) {
            this.stampBrushBitmaps = brushBitmaps;
            halfBrushBitmapWidth = brushBitmaps[0].getWidth() / 2;
        }

        private void paintSingleStamp(float x, float y) {
            if (Math.random() > 0.3) {
                paintCanvas.drawBitmap(brushBitmap, x - halfBrushBitmapWidth,
                        y - halfBrushBitmapWidth, null);
            }
//            if (Math.random() > 0.1) {
//                paintCanvas.drawBitmap(stampBrushBitmaps[0], x - halfBrushBitmapWidth,
//                        y - halfBrushBitmapWidth, null);
//            }
//            for (int i = 1; i < stampBrushBitmaps.length; i++)
//                if (Math.random() > 0.3) {
//                    paintCanvas.drawBitmap(stampBrushBitmaps[i], x - halfBrushBitmapWidth,
//                            y - halfBrushBitmapWidth, null);
//                }
        }
    }

    public Bitmap patternBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(DrawAttribute.screenWidth,
                DrawAttribute.screenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(backgroundBitmap, backgroundBitmapLeftTopP.x,
                backgroundBitmapLeftTopP.y, null);
        canvas.drawBitmap(paintBitmap, 0, 0, null);
        if (basicGeometry != null) {
            basicGeometry.drawGraphic(canvas);
        }
        stickerBitmapList.drawStickerBitmapList(canvas);

        return bitmap;
    }

    public void saveBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(DrawAttribute.screenWidth,
                DrawAttribute.screenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(backgroundBitmap, backgroundBitmapLeftTopP.x,
                backgroundBitmapLeftTopP.y, null);
        canvas.drawBitmap(paintBitmap, 0, 0, null);
        if (basicGeometry != null) {
            basicGeometry.drawGraphic(canvas);
        }
        stickerBitmapList.drawStickerBitmapList(canvas);
        System.out.println("-==============="+bitmap.getWidth()+"::::"+bitmap.getHeight());
        StorageInSDCard.saveBitmapInExternalStorage(bitmap,context);
    }

    public void freeBitmaps() {
        backgroundBitmap.recycle();
        paintBitmap.recycle();
        stickerBitmapList.freeBitmaps();
        undoAndRedo.freeBitmaps();
    }
}
