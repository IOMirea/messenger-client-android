package com.iomirea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView messagesView = (ListView) findViewById(R.id.messages_view);
        TempMessageAdapter tempMessageAdapter = new TempMessageAdapter(this);
        messagesView.setAdapter(tempMessageAdapter);
        TempMessage message = new TempMessage("Привет, слышал про новый мессенджер?",
                true);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Привет)", false);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Нет, но я уже очень хочу его скачать",
                false);
        tempMessageAdapter.add(message);
        message = new TempMessage ("Так мы уже в нём переписываемся", true);
        tempMessageAdapter.add(message);
        message = new TempMessage ("А",
                false);
        tempMessageAdapter.add(message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
