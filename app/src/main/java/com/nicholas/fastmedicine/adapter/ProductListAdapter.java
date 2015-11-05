package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nicholas.fastmedicine.common.BitmapCache;
import com.nicholas.fastmedicine.item.CategoryItem;
import com.nicholas.fastmedicine.R;

import java.util.List;

/**
 * Created by eggri_000 on 2015/10/26.
 */
public class ProductListAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<CategoryItem> items;
    private ImageLoader imageLoader;

    public ProductListAdapter(Activity activity, List<CategoryItem> objects)
    {
        this.mActivity=activity;
        this.items=objects;
        RequestQueue queue= Volley.newRequestQueue(mActivity);
        imageLoader=new ImageLoader(queue,new BitmapCache());
    }

   /* public void setData(List<CategoryItem> objects)
    {
        this.items = objects;
    }*/

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryItem item=items.get(position);
        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(mActivity).inflate(R.layout.product_list_item,null);
        }
        else
        {
            view=convertView;
        }
        NetworkImageView image=(NetworkImageView)view.findViewById(R.id.product_img);
        TextView titleTV=(TextView)view.findViewById(R.id.list_name);
        TextView subtitleTV=(TextView)view.findViewById(R.id.list_desc);
        TextView priceTV=(TextView)view.findViewById(R.id.list_price);
        TextView salesTV=(TextView)view.findViewById(R.id.list_sales);
        TextView specTV=(TextView)view.findViewById(R.id.list_spec);
        image.setDefaultImageResId(R.drawable.head);
        image.setErrorImageResId(R.drawable.head);
        image.setImageUrl(item.imgUrl, imageLoader);
        titleTV.setText(item.getTitle());
        subtitleTV.setText(item.getSubtitle());
        priceTV.setText("￥"+item.getPrice());
        salesTV.setText("销量:"+item.getSales());
        specTV.setText("规格:"+item.getSpec());
        return view;
    }
}
