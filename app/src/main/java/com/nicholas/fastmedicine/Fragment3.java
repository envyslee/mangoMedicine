package com.nicholas.fastmedicine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null, false);
        TextView textView = (TextView) view.findViewById(R.id.fragthree);
        textView.setText(new Date().toString());


        ((loadDataListener) getActivity()).clearCarCount();
        return view;
    }

    public interface loadDataListener {
        void clearCarCount();
    }

}
