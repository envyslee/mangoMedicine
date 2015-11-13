package com.nicholas.fastmedicine.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * Created by Administrator on 2015/11/13.
 */
public class MyScrollView extends ScrollView {

    private OnScrollListener scrollListener;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnMyScrollListener(OnScrollListener s)
    {
        this.scrollListener=s;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener!=null)
        {
            scrollListener.onScroll(t);
        }
    }

    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         */
        public void onScroll(int scrollY);
    }
}
