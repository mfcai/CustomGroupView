package com.cmf.customgroupview;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.cmf.customgroupview.R;
import com.cmf.customgroupview.activity.BottomDrawerActivity;
import com.cmf.customgroupview.activity.ClockActivity;
import com.cmf.customgroupview.activity.CommonRecyclerViewActivity;
import com.cmf.customgroupview.activity.Drag2Activity;
import com.cmf.customgroupview.activity.DragActivity;
import com.cmf.customgroupview.activity.HuaWeiWeatherActivity;
import com.cmf.customgroupview.activity.LoadingCartonActivity;
import com.cmf.customgroupview.activity.MyRecycleViewActivity;
import com.cmf.customgroupview.activity.TabScrollActivity;
import com.cmf.customgroupview.activity.VerticalDragViewActivity;
import com.cmf.customgroupview.activity.YoutubeActivity;
import com.cmf.customgroupview.view.HuaWeiWeatherView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickEvent(View view) {
    	switch (view.getId()) {
            case R.id.btn_group_default:
                startActivity(new Intent(this, MyRecycleViewActivity.class));
                break;
            case R.id.btn_drag_view:
                startActivity(new Intent(this, VerticalDragViewActivity.class));
                break;
            case R.id.btn_clock:
                startActivity(new Intent(this, ClockActivity.class));
                break;
            case R.id.buttonDragEdge:
                Intent intent = new Intent(MainActivity.this, DragActivity.class);
                intent.putExtra("edge", true);
                startActivity(intent);
                break;
            case R.id.buttonYoutube:
                startActivity(new Intent(this, YoutubeActivity.class));
                break;
            case R.id.buttonLoading:
                startActivity(new Intent(this, LoadingCartonActivity.class));
                break;
            case R.id.buttonweatherview:
                startActivity(new Intent(this, HuaWeiWeatherActivity.class));
                break;
            case R.id.buttonAnchorview:
                startActivity(new Intent(this, TabScrollActivity.class));
                break;
            case R.id.btn_drag2_view:
                startActivity(new Intent(this, Drag2Activity.class));
                break;
            case R.id.btn_common_adp:
               // startActivity(new Intent(this, CommonRecyclerViewActivity.class));
                break;


    	}
    }
}
