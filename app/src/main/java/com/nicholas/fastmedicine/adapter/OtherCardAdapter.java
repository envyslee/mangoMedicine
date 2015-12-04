package com.nicholas.fastmedicine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.item.MyCard;

import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
public class OtherCardAdapter extends ArrayAdapter<MyCard> {

    private  Context context;

    public OtherCardAdapter(Context context, int resource, List<MyCard> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyCard myCard=getItem(position);
        View view;
        if (convertView==null)
            view= LayoutInflater.from(context).inflate(R.layout.card_list_item,null);
        else
            view=convertView;

        TextView desc=(TextView)view.findViewById(R.id.card_desc);
        TextView amount=(TextView)view.findViewById(R.id.card_amount);
        TextView over=(TextView)view.findViewById(R.id.card_over);
        desc.setText(myCard.getCardDesc());
        amount.setText(myCard.getCardAmount()+".00");
        over.setText("有效期至:"+myCard.getOverTime());

        return view;
    }
}
