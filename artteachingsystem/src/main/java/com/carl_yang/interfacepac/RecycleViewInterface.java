package com.carl_yang.interfacepac;

import android.view.View;

/**
 * Created by Loren Yang on 2017/10/20.
 */

public class RecycleViewInterface {

    public interface OnItemOnClickListener{
        void onItemOnClick(View view, int postioon);
    }
}
