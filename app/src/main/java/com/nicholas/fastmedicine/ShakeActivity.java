package com.nicholas.fastmedicine;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nicholas.fastmedicine.common.Constant;
import com.nicholas.fastmedicine.common.MethodSingleton;
import com.nicholas.fastmedicine.item.WsResponse;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.HashMap;
import java.util.Map;

public class ShakeActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private TextView shake_tv;
    private Double p_id;
    private Double ph_id;
    private String p_name;
    private boolean isOpen=false;
    private LinearLayout content_fl;
    private ImageView no_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("摇一摇");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backpng);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shake_tv = (TextView) findViewById(R.id.shake_tv);
        content_fl=(LinearLayout)findViewById(R.id.content_fl);
        no_img=(ImageView)findViewById(R.id.no_img);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MethodSingleton.getInstance().isNetAvailable(ShakeActivity.this)) {
            if (sensorManager != null) {
                no_img.setVisibility(View.GONE);
                content_fl.setVisibility(View.VISIBLE);
                sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            }
        }else{
            Toast.makeText(ShakeActivity.this, Constant.netError, Toast.LENGTH_SHORT).show();
            content_fl.setVisibility(View.GONE);
            no_img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!isOpen) {
                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                int medumValue = 17;
                if (x > medumValue || y > medumValue || z > medumValue) {
                    vibrator.vibrate(400);
                    Message msg = new Message();
                    msg.what = Constant.SENSOR_SHAKE;
                    handler.sendMessage(msg);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constant.SENSOR_SHAKE) {

                showPopup();

            }
        }
    };

    private void showPopup() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final View contentView = LayoutInflater.from(ShakeActivity.this).inflate(R.layout.pop_shake, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, metrics.widthPixels*3 / 4, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isOpen=false;
            }
        });

        Button btn = (Button) contentView.findViewById(R.id.butt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(ShakeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", p_name);
                intent.putExtra("productId", p_id);
                intent.putExtra("pharmacyId", ph_id);
                startActivity(intent);
            }
        });
        final TextView name = (TextView) contentView.findViewById(R.id.s_name);
        final TextView price = (TextView) contentView.findViewById(R.id.s_price);
        String url = Constant.baseUrl + "getShake";
        if (Constant.latitude == null || Constant.lontitude == null) {
            Toast.makeText(ShakeActivity.this, Constant.mapError, Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> p = new HashMap<>();
            p.put("lo", Constant.lontitude.toString());
            p.put("la", Constant.latitude.toString());
            new OkHttpRequest.Builder().url(url).params(p).post(new ResultCallback<WsResponse>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(WsResponse ws) {
                    if (ws.getResCode() != null) {
                        if (ws.getResCode().equals("0")) {
                            Map<String, Object> map = (Map) ws.getContent();
                            p_id =(Double) map.get("productId");
                            ph_id =(Double) map.get("pharmacyId");
                            p_name=map.get("productName").toString();
                            name.setText(p_name);
                            price.setText("￥:" + map.get("productPrice"));
                            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
                            if (popupWindow.isShowing()) {
                                isOpen = true;
                            }
                        }
                    }
                }
            });
        }


    }

}
