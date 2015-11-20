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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2015/11/20.
 */
public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;
    private List<Integer> mHeights;

    private Integer[] imgs=new Integer[]{R.drawable.list,R.drawable.my_address,R.drawable.actionbar_add_icon,R.drawable.home,
    R.drawable.wait_pay,R.drawable.medicinebox};

   public RecylerAdapter(Context c,List<String> o){
       this.context=c;
       this.list=o;
       mHeights = new ArrayList<Integer>();
       for (int i = 0; i < list.size(); i++)
       {
           mHeights.add( (int) (100 + Math.random() * 300));
       }
   }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recylerview_item, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       /* ViewGroup.LayoutParams lp = holder.layout.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.layout.setLayoutParams(lp);*/
        holder.textView.setText(list.get(position));
        holder.imageView.setImageResource(imgs[position]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView;
        ImageView imageView;
        LinearLayout layout;

        public MyViewHolder(View view)
        {
            super(view);
            layout=(LinearLayout)view.findViewById(R.id.search_list_lay);
            textView = (TextView) view.findViewById(R.id.id_num);
            imageView=(ImageView)view.findViewById(R.id.search_list_img);
        }
    }
}
