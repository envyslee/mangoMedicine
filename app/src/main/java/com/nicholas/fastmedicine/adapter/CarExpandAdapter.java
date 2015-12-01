package com.nicholas.fastmedicine.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.common.BitmapCache;
import com.nicholas.fastmedicine.controller.QuantityChoose;
import com.nicholas.fastmedicine.item.ExpandGroup;
import com.nicholas.fastmedicine.item.ProductListItem;

import java.util.List;
import java.util.Map;

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
    public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(activity).inflate(R.layout.car_expand_list_item,null);
        }
        else
        {
            view=convertView;
        }
        LinearLayout click_area=(LinearLayout)view.findViewById(R.id.click_area);
        click_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isck.clickListener(groupPosition,childPosition);
            }
        });
        TextView stock=(TextView)view.findViewById(R.id.stock);
        NetworkImageView image=(NetworkImageView)view.findViewById(R.id.product_img);
        image.setDefaultImageResId(R.drawable.head);
        image.setErrorImageResId(R.drawable.head);
        //image.setImageUrl(item.getIconUrl(), imageLoader);
        TextView nameTV=(TextView)view.findViewById(R.id.list_name);
        TextView priceTV=(TextView)view.findViewById(R.id.list_price);
        TextView specTV=(TextView)view.findViewById(R.id.list_spec);
        CheckBox checkBox=(CheckBox)view.findViewById(R.id.car_cb);
        QuantityChoose quantity=(QuantityChoose)view.findViewById(R.id.quantity);
        quantity.setOnQuantityChangeListener(new QuantityChoose.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                isck.updateCount(groupPosition, childPosition, newQuantity);
            }

            @Override
            public void onLimitReached() {
            }
        });

        final ProductListItem item=children.get(group.get(groupPosition).getGName()).get(childPosition);
        if (item.getMaxCount()==0){
            stock.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.GONE);
            checkBox.setChecked(false);
        }else{
            stock.setVisibility(View.GONE);
            quantity.setVisibility(View.VISIBLE);
            quantity.setMaxQuantity(item.getMaxCount().intValue());
            checkBox.setChecked(item.isChecked());
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //item.setIsChecked(true);
                    isck.childIsCheck(groupPosition, childPosition, true);
                } else {
                    //item.setIsChecked(false);
                    isck.notAllGroup(groupPosition, false);
                    isck.childIsCheck(groupPosition, childPosition, false);
                }
            }
        });
        nameTV.setText(item.getProductName());
        priceTV.setText("￥" + item.getProductPrice());
        specTV.setText(item.getProductSpec());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface isCheckListener{
         void ischeckgroup(int groupposition,boolean ischeck);
         void notAllGroup(int groupposition,boolean ischeck);
        void updateCount(int gp,int cp,int count);
        void childIsCheck(int gp,int cp,boolean ischeck);
        void clickListener(int gp,int cp);
    }
}
