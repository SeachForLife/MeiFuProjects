package com.carl_yang.recordvideoabout.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.carl_yang.art.R;

/**
 * Created by Loren Yang on 2018/1/9.
 */

public class ButtonMoveRelativeLayout extends RelativeLayout {

    private RelativeLayout cancelLayout,sureLayout;
    private int mWidth;

    public ButtonMoveRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public ButtonMoveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonMoveRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.widget_buttonmove, ButtonMoveRelativeLayout.this);
        cancelLayout= (RelativeLayout) this.findViewById(R.id.cancel_layout);
        sureLayout= (RelativeLayout) this.findViewById(R.id.sure_layout);
        setVisibility(GONE);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
    }

    public void startAnim(){
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(cancelLayout,"translationX",0,-mWidth/4),
                ObjectAnimator.ofFloat(sureLayout,"translationX",0,mWidth/4)
        );
        set.setDuration(250).start();
    }

    public void stopAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(cancelLayout,"translationX",-mWidth/4,0),
                ObjectAnimator.ofFloat(sureLayout,"translationX",mWidth/4,0)
        );
        set.setDuration(250).start();
        setVisibility(GONE);
    }

    public RelativeLayout getCancelLayout(){
        return cancelLayout;
    }

    public RelativeLayout getSureLayout(){
        return sureLayout;
    }
}
