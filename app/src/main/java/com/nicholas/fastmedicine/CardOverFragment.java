package com.nicholas.fastmedicine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.nicholas.fastmedicine.adapter.OtherCardAdapter;
import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.item.MyCard;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardOverFragment extends Fragment {

    private List<MyCard> cards;
    private ListView over_list;

    public CardOverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_over, container, false);
        over_list = (ListView) view.findViewById(R.id.over_list);
        getData();
        return view;
    }

    private void getData() {
        String url = Constant.baseUrl + "getOtherCard";
        Map<String, String> map = new HashMap<>();
        map.put("userId", Constant.userId);
        map.put("useStatus", "2");
        new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<WsResponse>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(getActivity(), Constant.dataError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(WsResponse ws) {
                if (ws.getResCode() != null) {
                    if (ws.getResCode().equals("0")) {
                        cards = new ArrayList<>();
                        List<Map<String, Object>> s = (List) ws.getContent();
                        int size = s.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                Map<String, Object> m = s.get(i);
                                double time = (double) m.get("overTime");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                                MyCard myCard = new MyCard(((Double) m.get("id")).intValue(), ((Double) m.get("cardAmount")).intValue(), ((Double) m.get("useConditon")).intValue(), format.format(time), m.get("cardName").toString(), m.get("cardDesc").toString());
                                cards.add(myCard);
                            }
                            OtherCardAdapter adapter = new OtherCardAdapter(getActivity(), R.layout.card_list_item, cards);
                            over_list.setAdapter(adapter);
                        }
                    }
                }
            }
        });
    }

}
