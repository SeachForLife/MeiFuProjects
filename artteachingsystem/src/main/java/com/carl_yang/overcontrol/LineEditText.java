package com.carl_yang.overcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Author: Carl_yang on 2017/2/9
 * Email : 464479297@qq.com
 */
public class LineEditText extends EditText {

    private Paint paint;

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0,this.getHeight()-1,  this.getWidth()-1, this.getHeight()-1, paint);
//        int lineCount = getLineCount();
//        int lineHeight = getLineHeight();
//        for (int i = 0; i < lineCount; i++) {
//            int lineY = (i + 1) * lineHeight;
//            canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
//        }
    }
}
