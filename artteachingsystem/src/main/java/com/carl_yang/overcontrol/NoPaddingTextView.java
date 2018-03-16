package com.carl_yang.overcontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Author: Carl_yang on 2017/2/14
 * Email : 464479297@qq.com
 */
public class NoPaddingTextView extends TextView {
    public NoPaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    Paint.FontMetricsInt fontMetricsInt;

    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null) {
            fontMetricsInt = new Paint.FontMetricsInt();
            getPaint().getFontMetricsInt(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        super.onDraw(canvas);
    }
}
