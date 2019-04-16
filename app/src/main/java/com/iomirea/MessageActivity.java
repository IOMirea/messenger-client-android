package com.iomirea;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.MenuItem;


public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout shareLinearLayout, uploadLinearLayout, copyLinearLayout;
    BottomSheetDialog bottomSheetDialog;


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


        createBottomSheetDialog();

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


                bottomSheetDialog.show();

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



    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
            shareLinearLayout = view.findViewById(R.id.shareLinearLayout);
            uploadLinearLayout = view.findViewById(R.id.uploadLinearLayout);
            copyLinearLayout = view.findViewById(R.id.copyLinearLayout);

            shareLinearLayout.setOnClickListener((View.OnClickListener) this);
            uploadLinearLayout.setOnClickListener((View.OnClickListener) this);
            copyLinearLayout.setOnClickListener((View.OnClickListener) this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareLinearLayout:
                bottomSheetDialog.dismiss();
                break;
            case R.id.uploadLinearLayout:
                bottomSheetDialog.dismiss();
                break;
            case R.id.copyLinearLayout:
                bottomSheetDialog.dismiss();
                break;

        }
    }
}
