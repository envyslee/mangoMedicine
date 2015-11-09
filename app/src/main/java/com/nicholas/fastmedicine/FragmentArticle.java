package com.nicholas.fastmedicine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.CollectionAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

import litepalDB.CollectionItem;


public class FragmentArticle extends Fragment {

    private ListView article_list;

    public FragmentArticle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        article_list=(ListView)view.findViewById(R.id.article_list);
        List<CollectionItem> articles= DataSupport.where("c_type=?", "0").find(CollectionItem.class);
        if (articles!=null) {
            CollectionAdapter adapter = new CollectionAdapter(getActivity(), R.layout.address_list_item, articles);
            article_list.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(getActivity(), "muyou", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    /**
     * 初始化收藏数据
     */
    private void initDBData() {



    }

}
