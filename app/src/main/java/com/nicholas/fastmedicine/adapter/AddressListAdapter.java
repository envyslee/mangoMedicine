package com.nicholas.fastmedicine.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.item.AddressListItem;

import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AddressListAdapter extends ArrayAdapter<AddressListItem> {

    private Context context;

    public AddressListAdapter(Context context, int resource, List<AddressListItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressListItem item = getItem(position);
        View view;
        if (convertView == null)
            view = LayoutInflater.from(context).inflate(R.layout.address_list, null);
        else
            view = convertView;
        TextView item_name=(TextView)view.findViewById(R.id.item_up);
        TextView item_address=(TextView)view.findViewById(R.id.item_down);
        item_name.setText(item.getReceiver()+"  "+item.getPhoneNum());
        item_address.setText(item.getMapAdd()+" "+item.getDetailAdd());
        return view;
    }
}
