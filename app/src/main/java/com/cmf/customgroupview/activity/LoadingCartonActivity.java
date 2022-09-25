package com.cmf.customgroupview.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmf.customgroupview.R;
import com.cmf.customgroupview.view.LoadingView;

public class LoadingCartonActivity extends AppCompatActivity {
    LoadingView loadingView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_carton);
        loadingView = findViewById(R.id.loading_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingView.destroyView();
    }
}
