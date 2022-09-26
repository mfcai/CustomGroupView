package com.cmf.customgroupview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmf.customgroupview.R;
import com.cmf.customgroupview.view.HuaWeiWeatherView;

public class HuaWeiWeatherActivity extends AppCompatActivity {
    HuaWeiWeatherView huaWeiWeatherView;
    LinearLayout llRoot;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_weather);
        huaWeiWeatherView = findViewById(R.id.huawei_view);
        llRoot = findViewById(R.id.ll_root);
        huaWeiWeatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                huaWeiWeatherView.changeAngle(200);
            }
        });
        //设置角度颜色变化监听
        huaWeiWeatherView.setListener(new HuaWeiWeatherView.OnAngleColorListener() {
            @Override
            public void colorListener(int red, int green) {
                Color color=new Color();
                //通过Color对象将RGB值转为int类型
                int backColor=color.argb(100,red,green,0);
                //父布局设置背景
                llRoot.setBackgroundColor(backColor);
            }
        });
    }
}
