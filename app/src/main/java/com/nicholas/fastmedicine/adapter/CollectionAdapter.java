package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;

import java.util.Collections;
import java.util.List;

import litepalDB.CollectionItem;

/**
 * Created by eggri_000 on 2015/11/9.
 */
public class CollectionAdapter extends ArrayAdapter<CollectionItem> {

    private Context context;

    public CollectionAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionItem item= getItem(position);
        View view;
        if(convertView==null)
            view= LayoutInflater.from(context).inflate(R.layout.address_list_item,null);
        else
            view=convertView;
        TextView item_name=(TextView)view.findViewById(R.id.item_name);
        TextView item_address=(TextView)view.findViewById(R.id.item_address);
        item_name.setText(item.getC_title());
        item_address.setText(item.getC_subtitle());
        return view;
    }
}
