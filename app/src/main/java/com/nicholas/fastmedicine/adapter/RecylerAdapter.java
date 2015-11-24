package com.nicholas.fastmedicine.adapter;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nicholas.fastmedicine.R;
import com.nicholas.fastmedicine.SearchActivity;
import com.nicholas.fastmedicine.item.ProductListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2015/11/20.
 */
public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {

    private Context context;
    private List<ProductListItem> list;
    private List<Integer> mHeights;

    private Integer[] imgs=new Integer[]{R.drawable.list,R.drawable.my_address,R.drawable.actionbar_add_icon,R.drawable.home,
    R.drawable.wait_pay,R.drawable.medicinebox};

   public RecylerAdapter(Context c,List<ProductListItem> o){
       this.context=c;
       this.list=o;
       mHeights = new ArrayList<>();
       for (int i = 0; i < list.size(); i++)
       {
           mHeights.add( (int) (100 + Math.random() * 300));
       }
   }

    public interface OnClickListener {
        void onItemClick(View view, int position);
    }

    public OnClickListener onClickListener;

    public void SetOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recylerview_item, parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       /* ViewGroup.LayoutParams lp = holder.layout.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.layout.setLayoutParams(lp);*/
        //holder.textView.setText(list.get(position));
        holder.imageView.setImageResource(imgs[position]);
        if (onClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
        holder.nameView.setText(list.get(position).getProductName());
        holder.priceView.setText("￥:" + list.get(position).getProductPrice());
        holder.saleView.setText("销量:"+list.get(position).getProductSale());
        Map<String ,Double> map=new HashMap<>();
        map.put("productId", list.get(position).getProductId());
        map.put("pharmacyId", list.get(position).getPharmacyId());
        holder.itemView.setTag(map);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameView,saleView,priceView;
        ImageView imageView;
        LinearLayout layout;

        public MyViewHolder(View view)
        {
            super(view);
            layout=(LinearLayout)view.findViewById(R.id.search_list_lay);
            nameView = (TextView) view.findViewById(R.id.search_list_name);
            saleView=(TextView)view.findViewById(R.id.search_list_sale);
            priceView=(TextView)view.findViewById(R.id.search_list_price);
            imageView=(ImageView)view.findViewById(R.id.search_list_img);
        }
    }
}
