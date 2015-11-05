package com.nicholas.fastmedicine.adppter;

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
 * Created by eggri_000 on 2015/10/19.
 */
public class CategoryListAdapter extends BaseAdapter {

    ImageLoader mImageLoader;
    List<CategoryItem> categoryItems;

    private Activity mActivity;

    public CategoryListAdapter(Activity mActivity, List<CategoryItem> objects) {
        this.categoryItems =objects;
        this.mActivity=mActivity;
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        mImageLoader = new ImageLoader(queue, new BitmapCache());
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }
    @Override
    public Object getItem(int position) {
        return categoryItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryItem item= categoryItems.get(position);
        String url=item.getImgUrl();
        String itemTitle=item.getTitle();
        String itemSubtitle=item.getSubtitle();

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.category_list_item, null);
        } else {
            view = convertView;
        }
        NetworkImageView image = (NetworkImageView) view.findViewById(R.id.image);
        TextView list_titleTV=(TextView)view.findViewById(R.id.list_title);
        TextView list_subtitleTV=(TextView)view.findViewById(R.id.list_subtitle);
        image.setDefaultImageResId(R.drawable.stomach);
        image.setErrorImageResId(R.drawable.head);
        //image.setImageUrl(url, mImageLoader);
        list_titleTV.setText(itemTitle);
        list_subtitleTV.setText(itemSubtitle);
        return view;
    }


}
