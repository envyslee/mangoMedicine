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
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.controller.BadgeView;

import org.litepal.crud.DataSupport;

import litepalDB.UserInfo;

/**
 * Created by eggri_000 on 2015/10/13.
 */
public class Fragment4 extends Fragment implements View.OnClickListener {

    private TextView login_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //地址管理
        LinearLayout addressly = (LinearLayout) view.findViewById(R.id.addressManage);
        addressly.setOnClickListener(this);
        //登录
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.login_layout);
        relativeLayout.setOnClickListener(this);
        //意见反馈
        LinearLayout feedbackly = (LinearLayout) view.findViewById(R.id.feedbackly);
        feedbackly.setOnClickListener(this);
        //更多
        LinearLayout morely = (LinearLayout) view.findViewById(R.id.morely);
        morely.setOnClickListener(this);

        //收藏
        LinearLayout collectionly = (LinearLayout) view.findViewById(R.id.collectionly);
        collectionly.setOnClickListener(this);

        //登录文字
        login_txt = (TextView) view.findViewById(R.id.login_txt);
        updateUserInfo();

        //优惠券
        LinearLayout cardly=(LinearLayout)view.findViewById(R.id.cardly);
        cardly.setOnClickListener(this);

        //全部订单
        LinearLayout order_all=(LinearLayout)view.findViewById(R.id.order_all);
        order_all.setOnClickListener(this);

        //待支付
        LinearLayout order_wait_pay=(LinearLayout)view.findViewById(R.id.order_wait_pay);
        order_wait_pay.setOnClickListener(this);

        //待收货
        LinearLayout order_wait_receive=(LinearLayout)view.findViewById(R.id.order_wait_receive);
        order_wait_receive.setOnClickListener(this);

        //待评价
        LinearLayout reviewly = (LinearLayout) view.findViewById(R.id.reviewly);
        reviewly.setOnClickListener(this);
        BadgeView badgeView = new BadgeView(getActivity());
        badgeView.setTargetView(reviewly);
        badgeView.setBadgeCount(3);
    }

    public void updateUserInfo() {
        if (Constant.userId.isEmpty()) {
            login_txt.setText("点击登录");
        } else {
            login_txt.setText(Constant.userNum);
            login_txt.getPaint().setFakeBoldText(true);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.addressManage:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(getActivity(), AddressActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.login_layout:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {

                }
                break;
            case R.id.feedbackly:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.morely:
                intent = new Intent(getActivity(), MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.collectionly:
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.reviewly:
                break;
            case R.id.cardly:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), CardCenterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.order_all:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {

                }
                break;
            case R.id.order_wait_pay:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {

                }
                break;
            case R.id.order_wait_receive:
                if (Constant.userId.isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {

                }
                break;
            default:
                break;
        }
    }
}
