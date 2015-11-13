package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;

import java.util.List;

/**
 * Created by 斌 on 2015/11/7.
 */
public class ArrayColorAdapter extends BaseAdapter{

    private Activity mActivity;
    private List<String> dataList;
    private int currPosition;
    public ArrayColorAdapter(Activity a, List<String> d)
    {
        this.mActivity=a;
        this.dataList=d;
    }

    public void CurrentPosition(int i)
    {
        currPosition=i;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null)
            view= LayoutInflater.from(mActivity).inflate(R.layout.array_item,null);
        else
            view=convertView;
        TextView textView=(TextView)view;
        textView.setText(dataList.get(position));
        if (currPosition==position)
            textView.setTextColor(Color.parseColor("#32B9AA"));
        else
            textView.setTextColor(Color.parseColor("#3A3A3A"));
        return view;
    }
}
