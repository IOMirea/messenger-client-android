package com.iomirea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class LogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final RecyclerView log_list = findViewById(R.id.log_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        log_list.setLayoutManager(linearLayoutManager);

        final LogAdapter logAdapter = new LogAdapter(getApplicationContext());
        log_list.setAdapter(logAdapter);
        logAdapter.addEntity("Please work I'm slaving off here");
        logAdapter.addEntity("Just for the fun of it, to see how the separators look");
        logAdapter.addEntity("They look tolerable, so I'll leave them at that");
        logAdapter.addEntity("I know, this is hardcoded, but a man has to perform his tests doesn't he?");

    }
}
