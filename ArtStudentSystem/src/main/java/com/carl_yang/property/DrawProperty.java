package com.carl_yang.property;

import android.graphics.Color;

import com.carl_yang.drawfile.tools.DrawAttribute;
import com.carl_yang.stuart.R;


/**
 * Created by Loren Yang on 2017/12/11.
 */

public class DrawProperty {

    public static int BRUSH_SIZE=10;
    public static int BRUSH=1;//1表示彩笔 2 蜡笔 3刷子 4 擦子 5印章
    public static int COLOR_CHOOSE= Color.BLACK;
    public static DrawAttribute.DrawStatus BUTTON_CHOOSE=DrawAttribute.DrawStatus.PENCIL;// 当前选择的按钮
    public static String Broadcase_name="undeAndredoBro";//判断UNDO  REDO图标的广播
    public static int STAMP_ID= R.drawable.yz1;//用来记录当前选择的印章
    public static int COLOR_CURRENT=0;//用来判断当前用户选择的颜色，并在GRIDVIEW中标识出来  默认0
}
