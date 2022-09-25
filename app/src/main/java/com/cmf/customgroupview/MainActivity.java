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
import com.cmf.customgroupview.activity.DragActivity;
import com.cmf.customgroupview.activity.LoadingCartonActivity;
import com.cmf.customgroupview.activity.MyRecycleViewActivity;
import com.cmf.customgroupview.activity.VerticalDragViewActivity;
import com.cmf.customgroupview.activity.YoutubeActivity;

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
            case R.id.btn_bottom_drawer:
                startActivity(new Intent(this, BottomDrawerActivity.class));
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


    	}
    }
}
