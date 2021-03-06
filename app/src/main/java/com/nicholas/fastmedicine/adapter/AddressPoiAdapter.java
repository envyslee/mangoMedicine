package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.item.AddressPoiItem;

import java.util.List;

/**
 * Created by eggri_000 on 2015/11/4.
 */
public class AddressPoiAdapter extends ArrayAdapter<AddressPoiItem> {

    private Activity activity;
    public AddressPoiAdapter(Activity a,int i,List<AddressPoiItem> o)
    {
        super(a,i,o);
        this.activity=a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressPoiItem item=getItem(position);
        View view;
        if (convertView==null)
            view= LayoutInflater.from(activity).inflate(R.layout.address_list_item,null);
        else
            view=convertView;
        TextView item_name=(TextView)view.findViewById(R.id.item_name);
        TextView item_address=(TextView)view.findViewById(R.id.item_address);
        item_name.setText(item.getPoiName());
        item_address.setText(item.getPoiAddress());
        return view;
    }
}
