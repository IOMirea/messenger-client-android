package com.iomirea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;



public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView messagesView = (ListView) findViewById(R.id.messages_view);
        final TempMessageAdapter tempMessageAdapter = new TempMessageAdapter(this);
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

        final EditText mtext = findViewById((R.id.message_textfield));
        ImageButton send_message = (ImageButton) findViewById(R.id.send_message);
        //отправка сообщения в функции ниже
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sending = message_trimmer(mtext.getText().toString());
                if (sending.length()!=0) {
                    tempMessageAdapter.add(new TempMessage(sending,true));
                }
                mtext.setText("");
            }
        });
        send_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String sending = message_trimmer(mtext.getText().toString());
                if (sending.length()!=0) {
                    tempMessageAdapter.add(new TempMessage(sending,false));
                }
                mtext.setText("");
                return false;
            }
        });

        messagesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                //открытие меню
                tempMessageAdapter.add(new TempMessage("Долгое нажатие работает",false));
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    protected String message_trimmer(String source) {
        source = source.trim();
        source = source.replaceAll("\\s+", " ");
        return source;
    }
}
