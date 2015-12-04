package com.nicholas.fastmedicine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.item.ReviewItem;

import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ReviewListAdapter extends ArrayAdapter<ReviewItem> {
    private Context context;
    public ReviewListAdapter(Context context, int resource,  List<ReviewItem> objects) {
        super(context, resource, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItem item=getItem(position);
        View view;
        if (convertView==null)
            view= LayoutInflater.from(context).inflate(R.layout.review_list_item,null);
        else
            view=convertView;

        TextView name=(TextView)view.findViewById(R.id.re_name);
        TextView time=(TextView)view.findViewById(R.id.re_time);
        TextView content=(TextView)view.findViewById(R.id.re_content);

        name.setText(item.getUserName());
        time.setText(item.getCreatedTime());
        content.setText(item.getContent());

        return view;
    }
}
