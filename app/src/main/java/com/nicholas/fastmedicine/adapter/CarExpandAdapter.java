package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nicholas.fastmedicine.Fragment3;
import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.common.BitmapCache;
import com.nicholas.fastmedicine.item.ExpandGroup;
import com.nicholas.fastmedicine.item.ProductListItem;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Administrator on 2015/11/30.
 */
public class CarExpandAdapter extends BaseExpandableListAdapter {

    private List<ExpandGroup> group;
    private Map<String,List<ProductListItem>> children;
    private Activity activity;
    private ImageLoader imageLoader;
    isCheckListener isck;

    public void setIscheck(isCheckListener ischeck){
        this.isck = ischeck;
    }

    public CarExpandAdapter(Activity a,List<ExpandGroup> group,Map<String,List<ProductListItem>> children){
        this.children=children;
        this.group=group;
        this.activity=a;
        RequestQueue queue= Volley.newRequestQueue(activity);
        imageLoader=new ImageLoader(queue,new BitmapCache());
    }


    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(group.get(groupPosition).getGName()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(activity).inflate(R.layout.expand_list_group,null);
        }
        else
        {
            view=convertView;
        }
       TextView name=(TextView)view.findViewById(R.id.group_name);
        name.setText(group.get(groupPosition).getGName());
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.cb);
        checkBox.setChecked(group.get(groupPosition).isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    isck.ischeckgroup(groupPosition, true);
                }else {
                    isck.ischeckgroup(groupPosition,false);
                }
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(activity).inflate(R.layout.car_expand_list_item,null);
        }
        else
        {
            view=convertView;
        }

        NetworkImageView image=(NetworkImageView)view.findViewById(R.id.product_img);
        image.setDefaultImageResId(R.drawable.head);
        image.setErrorImageResId(R.drawable.head);
        //image.setImageUrl(item.getIconUrl(), imageLoader);
        TextView nameTV=(TextView)view.findViewById(R.id.list_name);
        TextView priceTV=(TextView)view.findViewById(R.id.list_price);
        TextView specTV=(TextView)view.findViewById(R.id.list_spec);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.car_cb);

        final ProductListItem item=children.get(group.get(groupPosition).getGName()).get(childPosition);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    item.setIscheck(true);
                }else {
                    item.setIscheck(false);
                    isck.notAllGroup(groupPosition, false);

                }
            }
        });
        nameTV.setText(item.getProductName());
        priceTV.setText("￥"+item.getProductPrice());
        specTV.setText(item.getProductSpec());
        checkBox.setChecked(item.getIscheck());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface isCheckListener{
         void ischeckgroup(int groupposition,boolean ischeck);
         void notAllGroup(int groupposition,boolean ischeck);
    }
}
