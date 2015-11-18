package com.nicholas.fastmedicine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nicholas.fastmedicine.controller.BadgeView;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment4 extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, null, false);
        //地址管理
        LinearLayout addressly=(LinearLayout)view.findViewById(R.id.addressManage);
        addressly.setOnClickListener(this);
        //登录
        RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.login_layout);
        relativeLayout.setOnClickListener(this);
        //意见反馈
        LinearLayout feedbackly=(LinearLayout)view.findViewById(R.id.feedbackly);
        feedbackly.setOnClickListener(this);
        //更多
        LinearLayout morely=(LinearLayout)view.findViewById(R.id.morely);
        morely.setOnClickListener(this);

        //收藏
        LinearLayout collectionly=(LinearLayout)view.findViewById(R.id.collectionly);
        collectionly.setOnClickListener(this);

        //待评价
        LinearLayout reviewly=(LinearLayout)view.findViewById(R.id.reviewly);
        reviewly.setOnClickListener(this);
        BadgeView badgeView=new BadgeView(getActivity());
        badgeView.setTargetView(reviewly);
        badgeView.setBadgeCount(3);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.addressManage:
                 intent=new Intent(getActivity(),AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.login_layout:
                 intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.feedbackly:
                intent=new Intent(getActivity(),FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.morely:
                intent=new Intent(getActivity(),MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.collectionly:
                intent =new Intent(getActivity(),CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.reviewly:
                break;
            default:
                break;
        }
    }
}
