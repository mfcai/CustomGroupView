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
import com.cmf.customgroupview.activity.MyRecycleViewActivity;

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

    	}
    }
}
