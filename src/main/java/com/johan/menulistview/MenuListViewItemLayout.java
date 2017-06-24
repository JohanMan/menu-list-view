package com.johan.menulistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/6/24.
 */

public class MenuListViewItemLayout extends LinearLayout {

    public MenuListViewItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

}
