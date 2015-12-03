package com.nicholas.fastmedicine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.item.ProductListItem;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
public class OrderProductAdapter extends ArrayAdapter<ProductListItem> {

    private  Context context;

    public OrderProductAdapter(Context context, int resource, List<ProductListItem> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductListItem item=getItem(position);
        View view;
        if (convertView==null)
            view= LayoutInflater.from(context).inflate(R.layout.order_product_list_item,null);
        else
            view=convertView;
        TextView ph=(TextView)view.findViewById(R.id.pharmacy);
        TextView pn=(TextView)view.findViewById(R.id.medicine);
        TextView pr=(TextView)view.findViewById(R.id.price);
        TextView sp=(TextView)view.findViewById(R.id.spec);
        TextView co=(TextView)view.findViewById(R.id.count);

        ph.setText(item.getPharmacyName());
        pn.setText(item.getProductName());
        pr.setText("￥"+item.getProductPrice());
        sp.setText(item.getProductSpec());
        co.setText("购买数量:"+item.getCount());

        return view;
    }
}
