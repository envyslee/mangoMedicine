package com.nicholas.fastmedicine.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/12/3.
 */
public class ListViewFitScrollView extends ListView {

    public ListViewFitScrollView(Context context) {
        super(context);
    }

    public ListViewFitScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewFitScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
