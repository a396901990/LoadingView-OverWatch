package com.dean.loadingview_overwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dean.overwatchloadingview.OverWatchLoadingView;

public class MainActivity extends AppCompatActivity {

    public OverWatchLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingView = (OverWatchLoadingView) findViewById(R.id.loading_view);

        // start
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.start();
            }
        });

        // end
        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.end();
            }
        });
    }
}
